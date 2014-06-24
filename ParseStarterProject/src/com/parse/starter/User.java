package com.parse.starter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
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
		// this.put("itemsSolved", itemsSolved);
		//saveInBackground();
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
		// saveInBackground();
	}

	public void incrementLevel() {
		increment("levelAt");
		// saveInBackground();
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

	/**
	 * resets the user's credentials when they decide to restart a game
	 */
	public void restart() {
		this.put("points", 0);
		this.put("levelAt", 1);
		this.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not
								// gps, 2 = at location
		this.put("puzzleID", 0);
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
		// this.put("itemsSolved", itemsSolved);
		saveInBackground();
	}
	
	/**
	 * Gives the current user a new material from the same item
	 * 
	 * @return true if there are no more materials to find for a given item, and
	 *         false otherwise
	 */
	public boolean getNewMaterialShuffleStyle() {
		final User currentUser = this;
		final String currentMaterial = this.getMaterial();
		final String currentItem = this.getItem();
		final ArrayList<String> collectedMaterials = this
				.getMaterialsCollected();
		final ArrayList<String> solvedMaterials = this.getMaterialsSolved();

		if (currentItem != "") {
			ParseQuery<Item> query = Item.getQuery();

			// NOTE: Have to match value type EXACTLY
			query.whereEqualTo("name", currentItem);

			// choosing a random puzzle from the query
			query.findInBackground(new FindCallback<Item>() {
				@Override
				public void done(List<Item> items, ParseException e) {
					if (e == null && items.size() == 1) {
						ArrayList<String> itemMaterials = items.get(0)
								.getMaterials();
						Collections.shuffle(itemMaterials);
						for (String material : itemMaterials) {
							if ((material != currentMaterial)
									&& !collectedMaterials.contains(material)
									&& !solvedMaterials.contains(material)) {
								currentUser.setMaterial(material);
								break;
							}
						}
					}
				}
			});
			if (currentUser.getMaterial() == currentMaterial) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gives the current user a new item and material (if possible) called only
	 * if all materials that make up currentItem are completely solved for (not
	 * necessarily found) current material of the user should be “”
	 */
	public void getNewItemAndMaterial() {
		final User currentUser = this;
		final String currentItem = this.getItem();
		final String currentChar = this.getCurrentCharacter();
		final ArrayList<String> solvedItems = this.getItemsSolved();
		final ArrayList<String> collectedItems = this.getItemsCollected();

		currentUser.addItemSolved(currentItem);
		currentUser.setItem("");
		ParseQuery<Character> query = Character.getQuery();

		// NOTE: Have to match value type EXACTLY
		query.whereEqualTo("name", currentChar);

		query.findInBackground(new FindCallback<Character>() {
			@Override
			public void done(List<Character> characters, ParseException e) {
				if (e == null && characters.size() == 1) {
					ArrayList<String> charItems = characters.get(0).getItems();
					Collections.shuffle(charItems);

					// indicates if all items have been found
					String newItem = "";
					for (String item : charItems) {
						if (!collectedItems.contains(item)
								&& !solvedItems.contains(item)) {
							newItem = item;
							currentUser.setItem(item);
							break;
						}
					}

					// give the user a new material if not assigned a new item
					if (newItem != "") {
						ParseQuery<Item> itemQuery = Item.getQuery();
						itemQuery.whereEqualTo("name", newItem);

						itemQuery.findInBackground(new FindCallback<Item>() {
							@Override
							public void done(List<Item> items, ParseException e) {
								if (e == null && items.size() == 1) {
									ArrayList<String> itemMaterials = items
											.get(0).getMaterials();
									currentUser.setMaterial(itemMaterials
											.get(0));
								}
							}
						});
					}
				}
			}
		});
	}
}
