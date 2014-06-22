package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseUser;

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
		this.put("currentIngredient", "water"); // hardcoded for now
		ArrayList<String> itemsCollected = new ArrayList<String>();
		this.put("itemsCollected", itemsCollected);
		this.put("currentCharacter", "Pusheen"); // hardcoded for now
		ArrayList<String> charactersCollected = new ArrayList<String>();
		this.put("charactersCollected", charactersCollected);
		
		ArrayList<String> materialsSolved = new ArrayList<String>();
		materialsSolved.add("bucket");
		materialsSolved.add("sand");
		materialsSolved.add("water");
		this.put("materialsSolved", materialsSolved);
		
		ArrayList<String> materialsCollected = new ArrayList<String>();
		this.put("materialsCollected", materialsCollected);
		
		
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
	
	public String getIngredient() {
		return getString("currentIngredient");
	}

	public ArrayList<String> getMaterialsSolved() {
		return (ArrayList<String>) get("materialsSolved"); 
	}
	
	public ArrayList<String> getMaterialsCollected() {
		return (ArrayList<String>) get("materialsCollected"); 
	}
	
    // store the objectIDs of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getItemsCollected() {
		return (ArrayList<String>) get("itemsCollected");
	}
	
	// store the objectIDs of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCharactersCollected() {
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
		this.put("puzzleID", puzzle);
		saveInBackground();
	}
	
	
	public void addCollectedItem(String item) {
		this.add("itemsCollected", item);
		saveInBackground();
	}
	
	public void addCollectedCharacter(String character) {
		this.add("charactersCollected",  character);
		saveInBackground();
	}

}

