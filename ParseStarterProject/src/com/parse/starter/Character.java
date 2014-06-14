package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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

	// store the names of the items that relate to the character
	@SuppressWarnings("unchecked")
	public ArrayList<String> getItems() {
		return (ArrayList<String>) get("items");
	}

}