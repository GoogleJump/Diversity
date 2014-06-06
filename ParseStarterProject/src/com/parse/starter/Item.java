package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Item")
public class Item extends ParseObject {
    
    // A default constructor is required
    public Item() {}
    
	public String getName() {
		return getString("name");
	}
    
    public int geID() {
    	return getInt("ID");
    }
    
	@SuppressWarnings("unchecked")
	public ArrayList<String> getIngredients() {
		return (ArrayList<String>) get("ingredients");
	}
	
}
