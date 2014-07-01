package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseUser;
//import android.util.Log;

/**
* User is a read and write object.
*/

@ParseClassName("_User")
public class User extends ParseUser {

	// For more information about ParseUser:
	// https://parse.com/docs/android/api/com/parse/ParseUser.html

	// empty constructor is required
	public User() {
	}

	// constructor for our wrapper class for ParseUser
	public User(String username, String password, String puzzle) {
		this.setUsername(username);
		this.setPassword(password);
		this.put("points", 0);
		this.put("levelAt", 1);
		this.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not
								// gps, 2 = at location
		this.put("puzzleID", puzzle);
		this.put("currentItem", "coffee"); // hardcoded for now
		this.put("currentMaterial", "water"); // hardcoded for now
		ArrayList<String> itemsCollected = new ArrayList<String>();
		this.put("itemsCollected", itemsCollected);
		this.put("currentCharacter", "Pusheen"); // hardcoded for now
		ArrayList<String> charactersCollected = new ArrayList<String>();
		this.put("charactersCollected", charactersCollected);
		ArrayList<String> materialsCollected = new ArrayList<String>();
		this.put("materialsCollected", materialsCollected);
		ArrayList<String> materialsSolved = new ArrayList<String>();
		this.put("materialsSolved", materialsSolved);
		ArrayList<String> itemsSolved = new ArrayList<String>();
		this.put("itemsSolved", itemsSolved);
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

	public String getPuzzle() {
		return getString("puzzleID");
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

	public String getCurrentCharacter() {
		return getString("currentCharacter");
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

	@SuppressWarnings("unchecked")
	public ArrayList<String> getItemsSolved() {
		return (ArrayList<String>) get("itemsSolved");
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getMaterialsSolved() {
		return (ArrayList<String>) get("materialsSolved");
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

	public void setPuzzle(String puzzle) {
		this.put("puzzleID", puzzle);
		saveInBackground();
	}

	public void setMaterial(String material) {
		this.put("currentMaterial", material);
		saveInBackground();
	}

	public void setItem(String item) {
		this.put("currentItem", item);
		saveInBackground();
	}
	
	public void setCurrentCharacter(String character) {
		this.put("currentCharacter", character);
//		saveInBackground();
	}

	public void addItemCollected(String item) {
		this.add("itemsCollected", item);
		saveInBackground();
	}

	public void addCharacterCollected(String character) {
		this.add("charactersCollected", character);
		saveInBackground();
	}

	public void addMaterialCollected(String material) {
		this.add("materialsCollected", material);
		saveInBackground();
	}

	public void addMaterialSolved(String material) {
		this.add("materialsSolved", material);
		saveInBackground();
	}

	public void addItemSolved(String item) {
		this.add("itemsSolved", item);
		saveInBackground();
	}

}

