package com.parse.starter;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GooglePlace {

	@SerializedName("geometry")
	Geometry geometry;

	@SerializedName("icon")
	String icon;

	@SerializedName("id")
	String id;

	@SerializedName("name")
	String name;

	@SerializedName("opening_hours")
	OpeningHours opening_hours;

	@SerializedName("reference")
	String reference;

	@SerializedName("types")
	List<String> types;

	@SerializedName("vicinity")
	String vicinity;

	@SerializedName("photos")
	List<Photo> photos;

	@SerializedName("rating")
	String rating;

	public static class Geometry {
		@SerializedName("location")
		LocationPoint location;

		public LocationPoint getLocation() {
			return location;
		}
	}

	public static class LocationPoint {
		@SerializedName("lat")
		double lat;
		@SerializedName("lng")
		double lng;

		public LocationPoint(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}

		public double getLat() {
			return lat;
		}

		public double getLng() {
			return lng;
		}
	}

	public class OpeningHours {
		@SerializedName("open_now")
		boolean open_now;
	}

	public String getName() {
		return name;
	}

	public double getLat() {
		return geometry.getLocation().getLat();
	}

	public double getLng() {
		return geometry.getLocation().getLng();
	}

	public class Photo {
		@SerializedName("height")
		int height;

		@SerializedName("html_attributions")
		List<String> html_attributions;

		@SerializedName("photo_reference")
		String photo_reference;

		@SerializedName("width")
		int width;
	}
}
