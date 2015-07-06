package com.lvwallpaper.model;


public class Photo {

	
	public String photoId;
	public String secret;
	public String server;
	public String farm;
	public String license;
	public String originalsecret;
	public String originalformat;
	public String nsid;
	public String username;
	public String realname;
	public String title;
	public String description;
	public String locality;
	public String region;
	public String country;
	public String url;
	public String iconServer;
	public String iconFarm;
	public boolean isFavorite;
	public String postfixnew;

	public Photo() {

	}

	public Photo(String id, String secret, String server, String farm,
			String license, String originalsecret, String originalformat,
			String nsid, String username, String realname, String title,
			String description, String locality, String region, String country,
			String url, String iconServer, String iconFarm, boolean isFavorite,
			String postFix) {
		super();
		this.photoId = id;
		this.secret = secret;
		this.server = server;
		this.farm = farm;
		this.license = license;
		this.originalsecret = originalsecret;
		this.originalformat = originalformat;
		this.nsid = nsid;
		this.username = username;
		this.realname = realname;
		this.title = title;
		this.description = description;
		this.locality = locality;
		this.region = region;
		this.country = country;
		this.url = url;
		this.iconFarm = iconFarm;
		this.iconServer = iconServer;
		this.isFavorite = isFavorite;
		this.postfixnew = postFix;
	}

	public String getPathThumb() {

		if (postfixnew == null||postfixnew.length()==0) {

			return "http://farm" + this.farm + ".staticflickr.com/"
					+ this.server + "/" + this.photoId + "_" + this.secret
					+ "_b.jpg";
		}

		return "http://farm" + this.farm + ".staticflickr.com/" + this.server
				+ "/" + this.photoId + "_" + this.secret + "_" + postfixnew
				+ ".jpg";
	}

//	public String getPathLarge() {
	//	return "http://farm" + this.farm + ".staticflickr.com/" + this.server
			//	+ "/" + this.photoId + "_" + this.secret + "_b.jpg";
	//}

	public String getPathOrginal() {
		if (originalsecret.length() > 0) {
			return "http://farm" + this.farm + ".staticflickr.com/"
					+ this.server + "/" + this.photoId + "_"
					+ this.originalsecret + "_o." + originalformat;
		} else {
			return getPathThumb();
		}
	}

	public String getAvatarPath() {
		if (iconFarm.length() > 0) {
			return "http://farm" + iconFarm + ".staticflickr.com/" + iconServer
					+ "/buddyicons/" + nsid + ".jpg";
		}
		return "";
	}

	public String getLocation() {
		if (locality.length() > 0 || region.length() > 0
				|| country.length() > 0) {
			return "Location: " + locality + " " + region + " " + country;
		}
		return "Location: " + "Somewhere on Earth";
	}

	public String getOwnerName() {
		if (realname.length() > 0) {
			return realname;
		}
		return username;
	}

	public String getLicense() {
		int i = Integer.parseInt(license);

		switch (i) {
		case 0:
			return "All Rights Reserved";
		case 4:
			return "Creative Commons License";
		case 6:
			return "Creative Commons License";
		default:
			break;
		}

		return "";
	}


}
