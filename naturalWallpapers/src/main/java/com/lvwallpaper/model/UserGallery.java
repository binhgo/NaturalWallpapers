package com.lvwallpaper.model;

/**
 * Created by VuPhan on 6/22/15.
 */
public class UserGallery implements Comparable<UserGallery>
{

    private String id;
    private String title;
    public String imageLink;
    public String numOfPhoto;

    public UserGallery(String id, String title)
    {
        this.id = id;
        this.title = title;
    }

    @Override
    public int compareTo(UserGallery userGallery)
    {

        return userGallery.title.compareTo(this.title);
    }

    public String getId()
    {

        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
