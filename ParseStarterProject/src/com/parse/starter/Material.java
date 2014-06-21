package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Material")
public class Material extends ParseObject {
	
	public Material() {}
	
	public String getName() {
		return getString("name");
	}
	
	public ArrayList<String> getSearchTerms() {
		return (ArrayList<String>) get("searchTerms");
	}

}
