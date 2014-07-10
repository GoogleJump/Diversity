package com.parse.starter;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Material")
public class Material extends ParseObject {

	public Material() {
	}

	public String getName() {
		return getString("name");
	}

	public List<String> getSearchTerms() {
		return getList("searchTerms");
	}

}
