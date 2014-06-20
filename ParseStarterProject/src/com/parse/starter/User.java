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
	public User() {}
	
	// constructor for our wrapper class for ParseUser
	public User(String username, String password, String puzzle) {
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
	
	public void incrementPoints(int points) {
		increment("points", points);
		//saveInBackground();
	}

	public void incrementLevel() {
		increment("levelAt");
		//saveInBackground();
	}
	
	public void incrementState() {
		increment("stateAt");
		//saveInBackground();
	}
	
	public void setPuzzle(String puzzle) {
		this.put("puzzleID", puzzle);
		//saveInBackground();
	}
	
	public void setMaterial(String material) {
		this.put("currentMaterial", material);
		//saveInBackground();
	}
	
	public void setItem(String item) {
		this.put("currentItem", item);
		//saveInBackground();
	}
	
	public void addItemCollected(String item) {
		this.add("itemsCollected", item);
		//saveInBackground();
	}
	
	public void addCharacterCollected(String character) {
		this.add("charactersCollected",  character);
		//saveInBackground();
	}
	
	public void addMaterialCollected(String material) {
		this.add("materialsCollected",  material);
		//saveInBackground();
	}
	
	
	/**
	 * resets the user's credentials when they decide to restart a game
	 */
	public void restart() {
		this.put("points", 0);
		this.put("levelAt", 1);
		this.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
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
		
		//i get an error when i try to do this...
		//saveInBackground();
	}
	
	/**
	 * Gives the current user a new ingredient from the same item
	 */
	public void getNewIngredientShuffleStyle() {
		final User currentUser = this;
		final String currentIngredient = this.getMaterial();
		final String currentItem = this.getItem();
		final ArrayList<String> collectedIngredients = this.getMaterialsCollected();
		
		ParseQuery<Item> query = Item.getQuery();
		
		// NOTE: Have to match value type EXACTLY
		query.whereEqualTo("name", currentItem);
		
		// choosing a random puzzle from the query
		query.findInBackground(new FindCallback<Item>() {
			@Override
			public void done(List<Item> items, ParseException e) {
				if (e == null && items.size() == 1) {
					ArrayList<String> itemMaterials = items.get(0).getMaterials();
					Collections.shuffle(itemMaterials);
					for (String material : itemMaterials) {
						if ((material != currentIngredient) && !collectedIngredients.contains(material)) {
							currentUser.setMaterial(material);
							break;
						}
					}
				}
			}
		});
	}
	
	public String getNewIngredientSolvedStyle() {
		final User currentUser = this;
		final String currentIngredient = this.getMaterial();
		final String currentItem = this.getItem();
		final String currentChar = this.getCurrentCharacter();
		final ArrayList<String> collectedIngredients = this.getMaterialsCollected();
		final ArrayList<String> collectedItems = this.getItemsCollected();
		
		currentUser.addMaterialCollected(currentIngredient);

		ParseQuery<Item> query = Item.getQuery();
		
		// NOTE: Have to match value type EXACTLY
		query.whereEqualTo("name", currentItem);
		
		// choosing a random puzzle from the query
		query.findInBackground(new FindCallback<Item>() {
			@Override
			public void done(List<Item> items, ParseException e) {
				if (e == null && items.size() == 1) {
					ArrayList<String> itemMaterials = items.get(0).getMaterials();
					Collections.shuffle(itemMaterials);
					boolean allFound = true;
					for (String material : itemMaterials) {
						if ((material != currentIngredient) && !collectedIngredients.contains(material)) {
							currentUser.setMaterial(material);
							allFound = false;
							break;
						}
					}
					
					if (allFound) {
						
						
						ParseQuery<Character> characterQuery = Character.getQuery();
						
						characterQuery.whereEqualTo("name", currentChar);
						
						characterQuery.findInBackground(new FindCallback<Character>() {
							@Override
							public void done(List<Character> characters, ParseException e) {
								if (e == null && characters.size() == 1) {
									ArrayList<String> characterItems = characters.get(0).getItems();
									for (String cItem : characterItems) {
										if ((cItem != currentItem) && !collectedItems.contains(cItem)) {
											currentUser.setItem(cItem);
											
											ParseQuery<Item> Iquery = Item.getQuery();
											
											Iquery.whereEqualTo("name", cItem);
											
											// choosing a random puzzle from the query
											Iquery.findInBackground(new FindCallback<Item>() {
												@Override
												public void done(List<Item> items, ParseException e) {
													if (e == null && items.size() == 1) {
														ArrayList<String> itemMaterials = items.get(0).getMaterials();
														currentUser.setMaterial(itemMaterials.get(0));
													}
												}
														
											});
											
											break;
										}
									}
								}
							}
						});
					}
					
					
				}
			}
		});
		if ((currentUser.getMaterial() == currentIngredient) && currentUser.getItem() == currentItem) {
			return "FINISHED";
		}
		else{
			return null;
		}
	}
}

