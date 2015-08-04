package com.lvwallpapers.utils;

import android.util.Log;

import com.lvwallpaper.model.Category;
import com.lvwallpaper.model.GalleryM;
import com.lvwallpaper.model.Photo;
import com.lvwallpaper.model.PhotoGallery;
import com.lvwallpaper.model.UserGallery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WebServiceUtils
{

    private static String KEY = "00b9829bb329898db472d38b41ea0356";
    private static String WEB_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&format=json&nojsoncallback=1";
    private static String WEB_URL_GALLERY = "https://api.flickr.com/services/rest/?method=flickr.galleries.getPhotos";
    private static String WEB_URL_GALLERIES = "https://api.flickr.com/services/rest/?method=flickr.galleries.getList";
    private static final String STAT_OK = "ok";

    private static String GALLERY_ID = "130242490-72157651551754178";
    private static String USER_ID = "134714375@N04";

    private static String getWeb_Url(String photoId)
    {
        return WEB_URL + "&api_key=" + KEY + "&photo_id=" + photoId + "&format=json&nojsoncallback=1";
    }

    private static String getWeb_UrlGallery(int page)
    {
        return WEB_URL_GALLERY + "&api_key=" + KEY + "&gallery_id=" + GALLERY_ID + "&extras=%2C+url_l+%2C+url_m%2C+url_s+%2C+url_o&format=json&nojsoncallback=1&page=" + page + "&per_page=6";
    }

    private static String getWeb_UrlGallery(String galleryId)
    {
        return WEB_URL_GALLERY + "&api_key=" + KEY + "&gallery_id=" + galleryId + "&extras=%2C+url_l+%2C+url_m%2C+url_s+%2C+url_o%2C+url_h%2C+url_k&format=json&nojsoncallback=1&page=" + 1 + "&per_page=6";
    }

    private static String getWebUrlGalleries()
    {
        return WEB_URL_GALLERIES + "&api_key=" + KEY + "&user_id=" + USER_ID + "&per_page=500&format=json&nojsoncallback=1";
    }

    public static GalleryM getGallery(int page)
    {

        GalleryM galleryM = new GalleryM();
        List<PhotoGallery> lstGalleryPhoto = new ArrayList<PhotoGallery>();

        try
        {

            Log.e("URL DSAHDKJA", getWeb_UrlGallery(page));

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(getWeb_UrlGallery(page));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.e("Message", result);
            if (result != null && result.length() > 0)
            {
                JSONObject jobject = new JSONObject(result);
                String jsonPhotos = jobject.getString("photos");

                JSONObject joPhotos = new JSONObject(jsonPhotos);

                int pageG = joPhotos.getInt("page");
                int pages = joPhotos.getInt("pages");

                galleryM.page = pageG;
                galleryM.pages = pages;

                String photos = joPhotos.getString("photo");

                JSONArray joarray = new JSONArray(photos);

                for (int i = 0; i < joarray.length(); i++)
                {
                    JSONObject jo = joarray.getJSONObject(i);

                    PhotoGallery photo = new PhotoGallery();
                    String id = jo.getString("id");
                    String secret = jo.getString("secret");
                    String server = jo.getString("server");
                    String farm = jo.getString("farm");
                    String url_m = (jo.has("url_m")) ? jo.getString("url_m") : "";
                    String url_s = (jo.has("url_s")) ? jo.getString("url_s") : "";
                    String url_o = (jo.has("url_o")) ? jo.getString("url_o") : "";
                    String url_l = (jo.has("url_l")) ? jo.getString("url_l") : url_o;

                    if (url_o.length() == 0 && url_l.length() == 0)
                    {
                        url_l = url_s;
                    }

                    photo.photoId = id;
                    photo.secret = secret;
                    photo.server = server;
                    photo.farm = farm;
                    photo.url_l = url_l;
                    photo.url_m = url_m;
                    photo.url_s = url_s;

                    lstGalleryPhoto.add(photo);
                }

                galleryM.lstPhotoGallery = lstGalleryPhoto;

            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }

        return galleryM;

    }

    public static GalleryM getGallery(String galleryId)
    {

        GalleryM galleryM = new GalleryM();
        List<PhotoGallery> lstGalleryPhoto = new ArrayList<PhotoGallery>();

        try
        {

            Log.e("URL DSAHDKJA", getWeb_UrlGallery(galleryId));

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(getWeb_UrlGallery(galleryId));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.e("Message", result);
            if (result != null && result.length() > 0)
            {
                JSONObject jobject = new JSONObject(result);
                String jsonPhotos = jobject.getString("photos");

                JSONObject joPhotos = new JSONObject(jsonPhotos);

                int pageG = joPhotos.getInt("page");
                int pages = joPhotos.getInt("pages");

                galleryM.page = pageG;
                galleryM.pages = pages;

                String photos = joPhotos.getString("photo");

                JSONArray joarray = new JSONArray(photos);

                for (int i = 0; i < joarray.length(); i++)
                {
                    JSONObject jo = joarray.getJSONObject(i);

                    PhotoGallery photo = new PhotoGallery();
                    String id = jo.getString("id");
                    String secret = jo.getString("secret");
                    String server = jo.getString("server");
                    String imgName = jo.getString("title");
                    String farm = jo.getString("farm");
                    String url_m = (jo.has("url_m")) ? jo.getString("url_m") : "";
                    String url_s = (jo.has("url_s")) ? jo.getString("url_s") : "";
                    String url_o = (jo.has("url_o")) ? jo.getString("url_o") : "";
                    String url_h = (jo.has("url_h")) ? jo.getString("url_h") : "";
                    String url_k = (jo.has("url_k")) ? jo.getString("url_k") : "";
                    String url_l = (jo.has("url_l")) ? jo.getString("url_l") : "";

                    if (url_l.length() == 0)
                    {
                        url_l = url_m;
                    }

                    photo.photoId = id;
                    photo.photoName = imgName;
                    photo.secret = secret;
                    photo.server = server;
                    photo.farm = farm;
                    photo.url_l = url_l;
                    photo.url_m = url_m;
                    photo.url_s = url_s;
                    photo.url_o = url_o;
                    photo.url_h = url_h;
                    photo.url_k = url_k;

                    lstGalleryPhoto.add(photo);
                }

                galleryM.lstPhotoGallery = lstGalleryPhoto;

            }
        }
        catch (Exception e)
        {


        }


        return galleryM;

    }

    public static Photo handleResult(String photoId)
    {

        String result = null;

        try
        {

            Log.e("URL DSAHDKJA", getWeb_Url(photoId));

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(getWeb_Url(photoId));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);

            if (result != null && result.length() > 0)
            {
                JSONObject jobject = new JSONObject(result);

                String state = jobject.getString("stat");

                if (state.equalsIgnoreCase(STAT_OK))
                {

                    // Photo Object
                    String photo = jobject.getString("photo");
                    JSONObject jobjectPhoto = new JSONObject(photo);

                    String id = jobjectPhoto.getString("id");
                    String secret = jobjectPhoto.getString("secret");
                    String server = jobjectPhoto.getString("server");
                    String farm = jobjectPhoto.getString("farm");
                    String license = jobjectPhoto.getString("license");

                    String originalsecret = "", originalformat = "";
                    if (jobjectPhoto.has("originalsecret"))
                    {
                        originalsecret = jobjectPhoto.getString("originalsecret");
                        originalformat = jobjectPhoto.getString("originalformat");
                    }

                    // Author Object
                    String owner = jobjectPhoto.getString("owner");
                    JSONObject jobjectOwner = new JSONObject(owner);

                    String nsid = jobjectOwner.getString("nsid");
                    String username = jobjectOwner.getString("username");
                    String realname = jobjectOwner.getString("realname");

                    String iconFarm = "", iconServer = "";
                    if (jobjectOwner.has("iconfarm"))
                    {
                        iconFarm = jobjectOwner.getString("iconfarm");
                    }
                    if (jobjectOwner.has("iconserver"))
                    {
                        iconServer = jobjectOwner.getString("iconserver");
                    }

                    // Title Object
                    String titleObject = jobjectPhoto.getString("title");
                    JSONObject jobjectTitle = new JSONObject(titleObject);

                    String title = jobjectTitle.getString("_content");

                    // Description Object
                    String desObject = jobjectPhoto.getString("description");
                    JSONObject jobjectDescription = new JSONObject(desObject);
                    String description = jobjectDescription.getString("_content");

                    // Local Object

                    String location = "", locality = "", region = "", country = "";

                    if (jobjectPhoto.has("location"))
                    {
                        location = jobjectPhoto.getString("location");
                        JSONObject jobjectLocation = new JSONObject(location);

                        // Locality
                        String localityObject = "";
                        if (jobjectLocation.has("locality"))
                        {
                            localityObject = jobjectLocation.getString("locality");
                            JSONObject jobjectlocality = new JSONObject(localityObject);
                            locality = jobjectlocality.getString("_content");
                        }

                        // Region
                        if (jobjectLocation.has("region"))
                        {
                            String regionObject = jobjectLocation.getString("region");
                            JSONObject jobjectregion = new JSONObject(regionObject);
                            region = jobjectregion.getString("_content");
                        }

                        // Country
                        if (jobjectLocation.has("country"))
                        {
                            String countryObject = jobjectLocation.getString("country");
                            JSONObject jobjectcountry = new JSONObject(countryObject);
                            country = jobjectcountry.getString("_content");
                        }

                    }

                    String url = "";
                    if (jobjectPhoto.has("urls"))
                    {
                        String urls = jobjectPhoto.getString("urls");
                        JSONObject jobjectUrls = new JSONObject(urls);

                        if (jobjectUrls.has("url"))
                        {

                            String arrayUrlObject = jobjectUrls.getString("url");
                            JSONArray jarraytUrl = new JSONArray(arrayUrlObject);

                            JSONObject jsonObjectUrl = jarraytUrl.getJSONObject(0);

                            if (jsonObjectUrl.has("_content"))
                                url = jsonObjectUrl.getString("_content");
                        }

                    }

                    Photo photoObject = new Photo(id, secret, server, farm, license, originalsecret, originalformat, nsid, username, realname, title, description, locality, region, country, url, iconServer, iconFarm, false, "");
                    return photoObject;
                }

            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        catch (JSONException e)
        {
            e.printStackTrace();

        }
        return null;

    }

    public static List<Category> getAllCategory()
    {
        List<Category> listCategory = new ArrayList<Category>();

        List<String> listContain = new ArrayList<>();

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(getWebUrlGalleries());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);

            if (result != null && result.length() > 0)
            {
                JSONObject jobject = new JSONObject(result);
                String jsonPhotos = jobject.getString("galleries");

                JSONObject joPhotos = new JSONObject(jsonPhotos);

                String photos = joPhotos.getString("gallery");

                JSONArray joarray = new JSONArray(photos);

                for (int i = joarray.length() - 1; i > 0; i--)
                {
                    JSONObject jo = joarray.getJSONObject(i);

                    String id = jo.getString("id");
                    String title = jo.getString("title");
                    String numOfPhotos = jo.getString("count_photos");
                    int farm = jo.getInt("primary_photo_farm");
                    String secret = jo.getString("primary_photo_secret");
                    String server = jo.getString("primary_photo_server");
                    String photoId = jo.getString("primary_photo_id");

                    JSONObject joTitle = new JSONObject(title);
                    String content = joTitle.getString("_content");


                    StringTokenizer stringTokenizer = new StringTokenizer(content, "_");
                    String categoryName = stringTokenizer.nextToken();

                    if (!listContain.contains(categoryName))
                    {
                        listContain.add(categoryName);
                        Category category = new Category();
                        category.categoryName = categoryName;
                        category.imageLink = "http://farm" + farm + ".staticflickr.com/" + server + "/" + photoId + "_" + secret + "_b.jpg";
                        category.numOfPhotos = category.numOfPhotos + Integer.parseInt(numOfPhotos);

                        listCategory.add(category);

                    }
                    else
                    {

                        for (int j = 0; j < listCategory.size(); j++)
                        {
                            if (listCategory.get(j).categoryName.equalsIgnoreCase(categoryName))
                            {
                                listCategory.get(j).numOfPhotos = listCategory.get(j).numOfPhotos + Integer.parseInt(numOfPhotos);
                                break;
                            }
                        }
                    }


                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }


        return listCategory;


    }

    public static List<UserGallery> getGalleries(String categoryName)
    {
        List<UserGallery> lstGallerys = new ArrayList<UserGallery>();


        try
        {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(getWebUrlGalleries());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity);
            Log.e("Message", result);
            if (result != null && result.length() > 0)
            {
                JSONObject jobject = new JSONObject(result);
                String jsonPhotos = jobject.getString("galleries");

                JSONObject joPhotos = new JSONObject(jsonPhotos);

                String photos = joPhotos.getString("gallery");

                JSONArray joarray = new JSONArray(photos);

                for (int i = 0; i < joarray.length(); i++)
                {
                    JSONObject jo = joarray.getJSONObject(i);
                    String id = jo.getString("id");
                    String title = jo.getString("title");
                    JSONObject joTitle = new JSONObject(title);
                    String content = joTitle.getString("_content");

                    if (content.startsWith(categoryName))
                    {
                        UserGallery userGallery = new UserGallery(id, content);
                        lstGallerys.add(userGallery);
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }


        return lstGallerys;

    }


}
