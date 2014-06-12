package com.parse.starter;

import java.util.ArrayList;

//import android.util.Log;



import android.content.Intent;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
* User is a read and write object.
*/

@ParseClassName("_User")
public class User extends ParseUser {

	// For more information about ParseUser:
	// https://parse.com/docs/android/api/com/parse/ParseUser.html

	// empty constructor is required
	public User() {}
	
	// constructor for our wrapper class for ParseUser
	public User(String username, String password, int puzzle) {
		this.setUsername(username);
		this.setPassword(password);
		this.put("points", 0);
		this.put("levelAt", 1);
		this.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
		this.put("puzzleID", puzzle);
		this.put("currentItem", "coffee"); // hardcoded for now
		this.put("currentMaterial", "water"); // hardcoded for now
		ArrayList<String> itemsCollected = new ArrayList<String>();
		this.put("itemsCollected", itemsCollected);
		this.put("currentCharacter", "Pusheen"); // hardcoded for now
		ArrayList<String> charactersCollected = new ArrayList<String>();
		this.put("charactersCollected", charactersCollected);
		saveInBackground();
	}

	// wrapper for isAuthenticated() in ParseUser
	public boolean isAuthenticated() {
		return this.isAuthenticated();
	}
	
	public static ParseUser getCurrentUser() {
		return ParseUser.getCurrentUser();
	}

	public int getPoints() {
		return getInt("points");
	}

	public int getLevel() {
		return getInt("levelAt");
	}

	public int getPuzzle() {
		return getInt("puzzleID"); 
	}
	
	public int getState() {
		return getInt("stateAt");
	}
	
	public String getItem() {
		return getString("currentItem");
	}
	
	public String getMaterial() {
		return getString("currentMaterial");
	}
	
	
    // store the names of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getItemsCollected() {
		return (ArrayList<String>) get("itemsCollected");
	}
	
	// store the names of the characters
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCharactersCollected() {
		return (ArrayList<String>) get("charactersCollected");
	}
	
	// store the names of the materials
	@SuppressWarnings("unchecked")
	public ArrayList<String> getMaterialsCollected() {
		return (ArrayList<String>) get("materialsCollected");
	}

	public void incrementPoints(int points) {
		increment("points", points);
		saveInBackground();
	}

	public void incrementLevel() {
		increment("levelAt");
		saveInBackground();
	}
	
	public void incrementState() {
		increment("stateAt");
		saveInBackground();
	}
	
	public void setPuzzle(int puzzle) {
		this.put("puzzleID", puzzle);
		saveInBackground();
	}
	
	
	public void addItemCollected(String item) {
		this.add("itemsCollected", item);
		saveInBackground();
	}
	
	public void addCharacterCollected(String character) {
		this.add("charactersCollected",  character);
		saveInBackground();
	}
	
	public void addMaterialCollected(String material) {
		this.add("materialsCollected",  material);
		saveInBackground();
	}


}

