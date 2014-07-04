package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Item")
public class Item extends ParseObject {

	// A default constructor is required
	public Item() {
	}

	public String getName() {
		return getString("name");
	}

	public int geID() {
		return getInt("ID");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getMaterials() {
		return (ArrayList<String>) get("materials");
	}
	
	public static ParseQuery<Item> getQuery() {
		return ParseQuery.getQuery(Item.class);
	}

}
