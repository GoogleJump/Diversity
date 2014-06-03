package com.parse.starter;

import java.util.ArrayList;

import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
* User is a read and write object.
*/

@ParseClassName("Person")
public class Person extends ParseUser {

	// For more information about ParseUser:
	// https://parse.com/docs/android/api/com/parse/ParseUser.html

	private ParseUser user; 

	// empty constructor is required
	public Person() {}
	
	// constructor for our wrapper class for ParseUser
	public Person(String username, String password, String email, 
		int puzzle) {
		user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.put("points", 0);
		user.put("levelAt", 1);
		user.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
		user.put("puzzleID", puzzle);
		user.put("currentItem", "coffee"); // hardcoded for now
		user.put("currentIngredient", "water"); // hardcoded for now
//		String[] items = new String[20]; // hardcoded at 20 items for now
//		user.put("itemsCollected", items);
		ParseACL acl = new ParseACL();
		acl.setPublicReadAccess(true);
		user.setACL(acl);
	}

	// wrapper for isAuthenticated() in ParseUser
	public boolean isAuthenticated() {
		return user.isAuthenticated();
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
	
	public String getIngredient() {
		return getString("currentIngredient");
	}
	
	
    // store the objectIDs of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCollectedItems() {
		return (ArrayList<String>) get("itemsCollected");
	}
	
	// store the objectIDs of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCollectedCharacters() {
		return (ArrayList<String>) get("charactersCollected");
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
		user.put("puzzleID", puzzle);
		saveInBackground();
	}
	
	// somehow these methods do not work when I wrap them
	
	public void addCollectedItem(String item) {
	//
		
	}
	
	public void addCollectedCharacter(String character) {
		user.add("charactersCollected",  character);
		saveInBackground();
		
	}


}
