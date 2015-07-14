/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lvstudio.wallpapers.naturalwallpapers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lvwallpaper.model.Photo;
import com.lvwallpaper.model.PhotoGallery;
import com.lvwallpapers.utils.WebServiceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;


public class ScreenSlidePageFragment extends Fragment
{
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for
     * {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private List<String> lstPhoto, listPhotoURL;
    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //private ProgressDialog progressDialog;
    private Photo photo;
    private boolean isFavorite;



    /**
     * Factory method for this fragment class. Constructs a new fragment for the
     * given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber, ArrayList<String> argLstPhoto, ArrayList<String> argLstPhotoURL)
    {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putStringArrayList("listPhoto", argLstPhoto);
        args.putStringArrayList("listPhotoURL", argLstPhotoURL);
        fragment.setArguments(args);
        return fragment;
    }



    public ScreenSlidePageFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        mPageNumber = getArguments().getInt(ARG_PAGE);
        lstPhoto = getArguments().getStringArrayList("listPhoto");
        listPhotoURL = getArguments().getStringArrayList("listPhotoURL");
        options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).showImageOnFail(R.drawable.load_fail).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_8888).build();

    }

    private boolean checkFavorite()
    {

        try
        {
            List<PhotoGallery> lstG = PhotoGallery.find(PhotoGallery.class, "photo_id =?", lstPhoto.get(mPageNumber));
            Log.e("MPAGER", mPageNumber + "  " + lstPhoto.get(mPageNumber));
            if (lstG != null && lstG.size() > 0)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            Log.e("ed", e.getMessage());
        }
        return false;
    }

    ImageView imgPhoto, imgAvatar, imgFavorite, imgExpander;
    TextView txtDecription, txtLicense, txtOwnerName, txtOwnerUrl, txtLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_photodetails, container, false);

        imgPhoto = (ImageView) rootView.findViewById(R.id.imgPhoto);
        imgAvatar = (ImageView) rootView.findViewById(R.id.imgAvatar);


        txtDecription = (TextView) rootView.findViewById(R.id.txtDescription);
        txtLicense = (TextView) rootView.findViewById(R.id.txtLicense);
        txtOwnerName = (TextView) rootView.findViewById(R.id.txtOwnerName);
        txtOwnerUrl = (TextView) rootView.findViewById(R.id.txtOwnerUrl);
        txtLocation = (TextView) rootView.findViewById(R.id.txtLocation);
        imgFavorite = (ImageView) rootView.findViewById(R.id.imgFavorite);
        imgExpander = (ImageView) rootView.findViewById(R.id.imgExpander);
        imgPhoto.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), PhotoZoomActivity.class);
                intent.putExtra("URL", listPhotoURL.get(mPageNumber));
                startActivity(intent);
            }
        });

        if (photo == null)
        {
            new LoadDataDobackground(lstPhoto.get(mPageNumber)).execute();
        }
        else
        {
            imageLoader.displayImage(listPhotoURL.get(mPageNumber), imgPhoto, options);

            imageLoader.displayImage(photo.getAvatarPath(), imgAvatar, options);

            //txtTitle.setText(photo.title);
            txtDecription.setText(Html.fromHtml(photo.description));
            txtLicense.setText(photo.getLicense());
            txtOwnerName.setText(photo.getOwnerName());
            txtOwnerUrl.setText(photo.url);
            txtLocation.setText(photo.getLocation());

            isFavorite = checkFavorite();
            if (isFavorite)
            {
                imgFavorite.setVisibility(View.VISIBLE);
            }
            else
            {
                imgFavorite.setVisibility(View.GONE);
            }

        }

        imgAvatar.setVisibility(View.VISIBLE);

        txtDecription.setVisibility(View.GONE);
        txtLocation.setVisibility(View.GONE);
        imgExpander.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (txtDecription.getVisibility() == View.GONE)
                {
                    txtDecription.setVisibility(View.VISIBLE);
                    txtLocation.setVisibility(View.VISIBLE);
                    imgExpander.setImageResource(R.drawable.ic_find_previous_holo_dark);
                }
                else
                {
                    txtDecription.setVisibility(View.GONE);
                    txtLocation.setVisibility(View.GONE);
                    imgExpander.setImageResource(R.drawable.ic_find_next_holo_dark);
                }
            }
        });

        //txtTitle.setTypeface(StaticFunction.getTypeface(getActivity()));
        //txtLicense.setTypeface(StaticFunction.getTypeface(getActivity()));

        return rootView;
    }

    public class LoadDataDobackground extends AsyncTask<String, String, String>
    {

        String photoSetId;

        public LoadDataDobackground(String photoSetId)
        {
            this.photoSetId = photoSetId;
        }

        @Override
        protected void onPreExecute()
        {
            //progressDialog = ProgressDialog.show(getActivity(), null, "Loading");
        }

        @Override
        protected String doInBackground(String... arg0)
        {

            photo = WebServiceUtils.handleResult(photoSetId);
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            //progressDialog.dismiss();

            if (photo != null)
            {
                imageLoader.displayImage(listPhotoURL.get(mPageNumber), imgPhoto, options);
                imageLoader.displayImage(photo.getAvatarPath(), imgAvatar, options);
                //txtTitle.setText(photo.title);
                txtDecription.setText(Html.fromHtml(photo.description));
                txtLicense.setText(photo.getLicense());
                txtOwnerName.setText(photo.getOwnerName());
                txtOwnerUrl.setText(photo.url);
                txtLocation.setText(photo.getLocation());
                isFavorite = checkFavorite();
                if (isFavorite)
                {
                    imgFavorite.setVisibility(View.VISIBLE);
                }
                else
                {
                    imgFavorite.setVisibility(View.GONE);
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Can not load data. Please check your network", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();

        }
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber()
    {
        return mPageNumber;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        photo = null;
        listPhotoURL = null;
        lstPhoto = null;
        imageLoader = null;
    }
}
