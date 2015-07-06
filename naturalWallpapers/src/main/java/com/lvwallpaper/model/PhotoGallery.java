package com.lvwallpaper.model;

import com.orm.SugarRecord;

public class PhotoGallery extends SugarRecord
{

    public String photoId;
    public String photoName;
    public String secret;
    public String server;
    public String farm;
    public String url_m;
    public String url_s;
    public String url_l;
    public String url_o;
    public String url_h;
    public String url_k;

    public PhotoGallery()
    {

    }

   /* public PhotoGallery(String photoId, String secret, String server, String farm, String url_m, String url_s, String url_l)
    {
        super();
        this.photoId = photoId;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.url_m = url_m;
        this.url_s = url_s;
        this.url_l = url_l;
    }*/

    public String getBestURL()
    {
        if (url_o.length() > 0)
        {
            return url_o;
        }
        else
        {
            if (url_k.length() > 0)
            {
                return url_k;
            }
            else
            {
                if (url_h.length() > 0)
                {
                    return url_h;
                }
                else
                {
                    if (url_l.length() > 0)
                    {
                        return url_l;
                    }
                    else
                    {
                        return url_m;
                    }
                }
            }
        }
    }

}
