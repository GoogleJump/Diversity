package com.parse.starter;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Character")
public class Character extends ParseObject {

	// A default constructor is required
	public Character() {
	}

	public String getName() {
		return getString("name");
	}

	public int getID() {
		return getInt("ID");
	}

	public List<String> getItems() {
		return getList("items");
	}
	
	public static ParseQuery<Character> getQuery() {
		return ParseQuery.getQuery(Character.class);
	}

}