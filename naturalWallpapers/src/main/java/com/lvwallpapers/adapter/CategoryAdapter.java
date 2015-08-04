package com.lvwallpapers.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvstudio.wallpapers.naturalwallpapers.MainActivity;
import com.lvstudio.wallpapers.naturalwallpapers.R;
import com.lvwallpaper.model.Category;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.staticfunction.StaticFunction;

import java.util.List;


/**
 * Created by user on 6/24/2015.
 */
public class CategoryAdapter extends BaseAdapter
{

    private List<Category> listImage;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    int desireHeight = 200;

    public CategoryAdapter(Activity activity, List<Category> listImage, int height)
    {
        this.listImage = listImage;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        desireHeight = height;
        options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).displayer(new RoundedBitmapDisplayer((int) (5 * StaticFunction.getDensity(activity)))).showImageOnFail(R.drawable.load_fail).showImageOnLoading(R.drawable.loading).build();
    }

    @Override
    public int getCount()
    {
        return listImage.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listImage.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = this.layoutInflater.inflate(R.layout.row_category, null);
            holder = new ViewHolder();
            holder.rltRoot = (RelativeLayout) convertView.findViewById(R.id.root);
            holder.heightLayout = (RelativeLayout) convertView.findViewById(R.id.heighLayout);
            holder.imvCate = (ImageView) convertView.findViewById(R.id.imvCategory);
            holder.txtNameCate = (TextView) convertView.findViewById(R.id.txtNameCate);
            holder.txtPhoto = (TextView) convertView.findViewById(R.id.txtPhoto);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, desireHeight);

        holder.heightLayout.setLayoutParams(layout_description);


        holder.rltRoot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("CategoryName", listImage.get(position).categoryName);
                activity.startActivity(intent);

            }
        });

//        holder.imvCate.setImageResource(listImage.get(position));
        imageLoader.displayImage(listImage.get(position).imageLink, holder.imvCate, options);
        holder.txtNameCate.setTypeface(StaticFunction.getTypeface(activity));
        holder.txtPhoto.setTypeface(StaticFunction.getTypeface(activity));

        holder.txtNameCate.setText(" " + listImage.get(position).categoryName);
        holder.txtPhoto.setText("  " + listImage.get(position).numOfPhotos + " photos");

        return convertView;
    }

    public class ViewHolder
    {
        RelativeLayout rltRoot;
        RelativeLayout heightLayout;
        ImageView imvCate;
        TextView txtNameCate;
        TextView txtPhoto;
    }
}
