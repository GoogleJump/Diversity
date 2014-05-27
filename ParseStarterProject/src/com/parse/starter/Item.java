package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Item")
public class Item extends ParseObject {
    
    // A default constructor is required
    public Item() {}
    
	public String getItemName() {
		return getString("itemName");
	}
    
    public int getItemID() {
    	return getInt("itemID");
    }
    
	@SuppressWarnings("unchecked")
	public ArrayList<String> getIngredients() {
		return (ArrayList<String>) get("itemIngredients");
	}
	
}
