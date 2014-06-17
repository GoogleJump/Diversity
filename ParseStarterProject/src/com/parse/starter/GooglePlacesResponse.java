package com.parse.starter;

import java.util.ArrayList;

public class GooglePlacesResponse {
	private ArrayList<String> html_attributions;
	private ArrayList<GooglePlace> results;
	
	public ArrayList<GooglePlace> getResults() {
		return results;
	}
}
