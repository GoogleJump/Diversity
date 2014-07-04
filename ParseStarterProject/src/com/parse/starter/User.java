package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
	public User(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
		this.restart();
		saveInBackground();
	}

	/**
	 * resets the user's credentials when they decide to restart a game
	 */
	public void restart() {
		this.put("puzzleID", "");
		this.put("currentItem", "JENNIFER"); // hardcoded for now
		this.put("currentMaterial", "water"); // hardcoded for now
		this.put("shuffledWord", "tewar");
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

	/**
	 * The User gets a new puzzle based on its current material. Either puzzleID
	 * is an empty string or shuffledWord is an empty string if there is a
	 * currentMaterial, otherwise, both are empty strings
	 */
	private void getNewPuzzle() {
		final String currentMaterial = this.getMaterial();
		if (!currentMaterial.equals("")) {

			ParseQuery<Puzzle> query = Puzzle.getQuery();
			query.whereEqualTo("material", currentMaterial);

			// choosing a random puzzle from the query
			try {
				List<Puzzle> potentialPuzzles = query.find();

				if (potentialPuzzles.size() > 0) {

					Random randomizer = new Random();
					Puzzle puzzle = potentialPuzzles.get(randomizer
							.nextInt(potentialPuzzles.size()));
					this.setPuzzle(puzzle.getObjectId());
					this.setShuffledWord("");
				} else {
					this.setPuzzle("");
					this.setShuffledWord(makeAnagram(currentMaterial));
				}
			} catch (ParseException e) {
				this.setPuzzle("");
				this.setShuffledWord(makeAnagram(currentMaterial));
			}

		} else {
			this.setPuzzle("");
			this.setShuffledWord("");
		}
	}

	/**
	 * Shuffles string to make an anagram
	 * 
	 * @param string
	 *            String to be shuffled
	 * @return a shuffled version of string, where all characters in string are
	 *         in the shuffled version
	 */
	private String makeAnagram(String string) {
		List<String> splitString = Arrays.asList(string.split(""));
		Collections.shuffle(splitString);

		String shuffled = "";
		for (String letter : splitString) {
			shuffled += letter;
		}
		return shuffled;
	}

	/**
	 * Gives the current user a new item and material (if possible), and the
	 * associated puzzle or shuffledWord called only if all materials that make
	 * up currentItem are completely solved for (not necessarily found) current
	 * material of the user should be the empty string
	 */
	public void getNewItemAndMaterial() {
		final String currentItem = this.getItem();
		final String currentChar = this.getCurrentCharacter();
		final ArrayList<String> solvedItems = this.getItemsSolved();
		final ArrayList<String> collectedItems = this.getItemsCollected();

		this.addItemSolved(currentItem);
		this.setItem("");
		ParseQuery<Character> query = Character.getQuery();

		// NOTE: Have to match value type EXACTLY
		query.whereEqualTo("name", currentChar);

		try {
			List<Character> characters = query.find();
			if (characters.size() == 1) {
				ArrayList<String> charItems = characters.get(0).getItems();
				Collections.shuffle(charItems);

				// newItem is changed if not all items have been found
				for (String item : charItems) {
					if (!collectedItems.contains(item)
							&& !solvedItems.contains(item)) {
						this.setItem(item);
						break;
					}
				}

				// give user new material if not assigned a new item
				this.getNewMaterialShuffleStyle();

			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public int getState() {
		return getInt("stateAt");
	}
	
	/**
	 * Gives the current user a new material, and the associated puzzle or
	 * shuffledWord from the same item
	 * 
	 * @return true if there are no more materials to find for a given item, and
	 *         false otherwise
	 */
	public boolean getNewMaterialShuffleStyle() {
		final User currentUser = this;
		final String currentMaterial = currentUser.getMaterial();
		final String currentItem = currentUser.getItem();
		final ArrayList<String> collectedMaterials = currentUser
				.getMaterialsCollected();
		final ArrayList<String> solvedMaterials = currentUser
				.getMaterialsSolved();

		if (!currentItem.equals("")) {
			ParseQuery<Item> query = Item.getQuery();

			query.whereEqualTo("name", currentItem);

			// choosing a random material from the query
			try {
				List<Item> items = query.find();
				if (items.size() == 1) {
					ArrayList<String> itemMaterials = items.get(0)
							.getMaterials();
					Collections.shuffle(itemMaterials);
					for (String material : itemMaterials) {
						if (!(material.equals(currentMaterial))
								&& !collectedMaterials.contains(material)
								&& !solvedMaterials.contains(material)) {
							currentUser.setMaterial(material);
							currentUser.getNewPuzzle();
							break;
						}
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (currentUser.getMaterial().equals(currentMaterial)) {
				return true;
			}
		}
		return false;
	}

	
	// GETTERS AND SETTERS BELOW!!!
	public String getPuzzle() {
		return getString("puzzleID");
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

	public String getShuffledWord() {
		return getString("shuffledWord");
	}

	// store the names of the items
	@SuppressWarnings("unchecked")
	public ArrayList<String> getMaterialsSolved() {
		return (ArrayList<String>) get("materialsSolved");
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

	// store the names of the materials
	@SuppressWarnings("unchecked")
	public ArrayList<String> getItemsSolved() {
		return (ArrayList<String>) get("itemsSolved");
	}

	public void incrementPoints(int points) {
		increment("points", points);
		saveInBackground();
	}

	public void setPuzzle(String puzzle) {
		this.put("puzzleID", puzzle);
		// saveInBackground();
	}

	public void incrementState() {
		increment("stateAt");
		saveInBackground();
	}

	public void setPuzzle(int puzzle) {
		this.put("puzzleID", puzzle);
		saveInBackground();
	}

	public void setMaterial(String material) {
		this.put("currentMaterial", material);
		// saveInBackground();
	}

	public void setItem(String item) {
		this.put("currentItem", item);
		// saveInBackground();
	}

	public void setShuffledWord(String word) {
		this.put("shuffledWord", word);
		// saveInBackground();
	}

	public void addItemCollected(String item) {
		this.add("itemsCollected", item);
		// saveInBackground();
	}

	public void addCharacterCollected(String character) {
		this.add("charactersCollected", character);
		saveInBackground();
		// saveInBackground();
	}

	public void addMaterialCollected(String material) {
		this.add("materialsCollected", material);
		saveInBackground();
	}

	public void addMaterialSolved(String material) {
		this.add("materialsSolved", material);
		// saveInBackground();
	}

	public void addItemSolved(String item) {
		this.add("itemsSolved", item);
		// //saveInBackground();
	}

}
