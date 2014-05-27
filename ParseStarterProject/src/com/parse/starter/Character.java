package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Character")
public class Character extends ParseObject {
    
    // A default constructor is required
    public Character() {}
    
	public String getCharacterName() {
		return getString("characterName");
	}
    
    public int getCharacterID() {
    	return getInt("characterID");
    }
    
    // store the objectIDs of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getIngredients() {
		return (ArrayList<String>) get("characterItems");
	}
	
}