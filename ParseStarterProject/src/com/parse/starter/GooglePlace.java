package com.parse.starter;

import java.util.ArrayList;

public class GooglePlace {
	private String formatted_address;
	private Geometry geometry;
	private String icon;
	private String id;
	private String name;
	private double rating;
	private String reference;
	private ArrayList<String> types;

	public static class Geometry {
		public LocationPoint location;
		
		public LocationPoint getLocation() {
			return location;
		}
	}

	public static class LocationPoint {
		public double lat;
		public double lng;
		
		public double getLat() {
			return lat;
		}
		
		public double getLng() {
			return lng;
		}
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
}
