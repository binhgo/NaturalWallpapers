package com.lvwallpapers.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvstudio.wallpapers.naturalwallpapers.R;
import com.lvwallpaper.model.PhotoGallery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter
{

    private Context _mcontext;
    private LayoutInflater mInflater;

    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private int columWidth = 90;
    private List<PhotoGallery> lstPhotos = new ArrayList<PhotoGallery>();
    int actionBarHeigh = 96;

    public ImageAdapter(Context context, int columnwidth, List<PhotoGallery> lstPhotos, int actionBarHeight)
    {

        this._mcontext = context;
        this.mInflater = LayoutInflater.from(context);
        this.columWidth = columnwidth;
        this.lstPhotos = lstPhotos;
        this.actionBarHeigh = actionBarHeight;


        options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(false).cacheOnDisk(true).showImageOnFail(R.drawable.load_fail).bitmapConfig(Bitmap.Config.ARGB_8888).build();

    }

    public void setNotify(List<PhotoGallery> lstPhotos)
    {
        this.lstPhotos = lstPhotos;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        int count = (lstPhotos == null) ? 0 : lstPhotos.size();
        return count;
    }

    @Override
    public Object getItem(int position)
    {

        return lstPhotos.get(position);
    }

    @Override
    public long getItemId(int arg0)
    {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = this.mInflater.inflate(R.layout.row_image, null);
            holder = new ViewHolder();
            holder.imageViews = (ImageView) convertView.findViewById(R.id.imgImageRow);
            holder.roots = (LinearLayout) convertView.findViewById(R.id.roots);
            holder.imageViews.setLayoutParams(new RelativeLayout.LayoutParams(columWidth, columWidth));
            holder.txtPhotoName = (TextView) convertView.findViewById(R.id.txtPhotoName);
            holder.txtPhotoName.setTypeface(StaticFunction.getTypeface((Activity) _mcontext));
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
            holder.roots.setPadding(0, actionBarHeigh, 2, 2);
        }

        if (position == 0)
        {
            holder.roots.setPadding(0, 0, 2, 2);
        }
        else if (position == 1)
        {
            holder.roots.setPadding(2, 0, 0, 2);
        }
        else
        {

            if ((position % 2) == 0)
            {
                // even
                holder.roots.setPadding(0, 2, 2, 2);
            }
            else
            {
                // odd
                holder.roots.setPadding(2, 2, 0, 2);
            }
        }


        imageLoader.displayImage(lstPhotos.get(position).url_m, holder.imageViews, options);
        holder.txtPhotoName.setText(" " + lstPhotos.get(position).photoName);

        return convertView;
    }

    public class ViewHolder
    {

        LinearLayout roots;
        ImageView imageViews;
        TextView txtPhotoName;
    }

}
