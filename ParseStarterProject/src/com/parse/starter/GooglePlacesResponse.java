package com.parse.starter;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GooglePlacesResponse {

	@SerializedName("html_attributions")
	List<String> html_attributions;

	@SerializedName("next_page_token")
	String next_page_token;

	@SerializedName("results")
	List<GooglePlace> results;

	@SerializedName("status")
	String status;

	public List<GooglePlace> getResults() {
		return results;
	}

	public List<String> getHtmlResponse() {
		return html_attributions;
	}

}