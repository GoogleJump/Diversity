package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {

	// A default constructor is required
	public UserInfo() {
	}

	public UserInfo(String username) {
		put("username", username);
		restart();
	}		

	public void restart() {
		put("currentMaterial", "");
		put("currentItem", "");
		put("currentCharacter", "");
		ArrayList<String> emptyList = new ArrayList<String>();
		put("materialsSolved", emptyList);
		put("materialsCollected", emptyList);
		put("itemsSolved", emptyList);
		put("itemsCollected", emptyList);
		put("charactersCollected", emptyList);
		put("shuffledWord", "");
		put("puzzleID", "1");
		saveInBackground();
	}

	// FIXME
	public void getNewItem() {
		// Chain of async calls, how to work around this?
		String currentCharacter = getCurrentCharacter();
		if (currentCharacter.length() == 0) {
			return;
		}
		ParseQuery<Character> query = ParseQuery.getQuery(Character.class);
		query.whereEqualTo("name", currentCharacter);
		try {
			List<Character> results = query.find();
			Character character = results.get(0);
			List<String> characterItems = character.getItems();
			Collections.shuffle(results);
			List<String> itemsSolved = getItemsSolved();
			for (int i = 0; i < characterItems.size(); i++) {
				if (!itemsSolved.contains(characterItems.get(i))) {
					System.out.println("HEREEEEEE");
					setCurrentItem(characterItems.get(i));
					return;
				}
			}
			// Solved all items
			// TODO: Handle this more gracefully
			setCurrentItem("FINISHED");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Gives the current user a new material, and the associated puzzle or
	 * shuffledWord from the same item
	 * 
	 * @return true if there are no more materials to find for a given item, and
	 *         false otherwise
	 */
	public boolean getNewMaterialShuffleStyle() {
		final String currentMaterial = getCurrentMaterial();
		final String currentItem = getCurrentItem();
		final List<String> collectedMaterials = getMaterialsCollected();
		final List<String> solvedMaterials = getMaterialsSolved();

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
							setCurrentMaterial(material);
							getNewPuzzle();
							break;
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (getCurrentMaterial().equals(currentMaterial)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The User gets a new puzzle based on its current material. Either puzzleID
	 * is an empty string or shuffledWord is an empty string if there is a
	 * currentMaterial, otherwise, both are empty strings
	 */
	private void getNewPuzzle() {
		final String currentMaterial = getCurrentMaterial();
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
					setPuzzleID(puzzle.getObjectId());
					this.setShuffledWord("");
				} else {
					setPuzzleID("");
					this.setShuffledWord(makeAnagram(currentMaterial));
				}
			} catch (ParseException e) {
				setPuzzleID("");
				this.setShuffledWord(makeAnagram(currentMaterial));
			}

		} else {
			setPuzzleID("");
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

	public void addItemCollected(String item) {
		add("itemsCollected", item);
		saveInBackground();
	}

	public void addCharacterCollected(String character) {
		add("charactersCollected", character);
		saveInBackground();
	}

	public void addMaterialCollected(String material) {
		add("materialsCollected", material);
		saveInBackground();
	}

	public void addMaterialSolved(String material) {
		add("materialsSolved", material);
		saveInBackground();
	}

	public void addItemSolved(String item) {
		add("itemsSolved", item);
		saveInBackground();
	}

	/*
	 * Getters and setters
	 */
	public String getUsername() {
		return getString("username");
	}

	public String getCurrentItem() {
		return getString("currentItem");
	}

	public void setCurrentItem(String currentItem) {
		put("currentItem", currentItem);
		saveInBackground();
	}

	public String getCurrentCharacter() {
		return getString("currentCharacter");
	}

	public void setCurrentCharacter(String currentCharacter) {
		put("currentCharacter", currentCharacter);
		saveInBackground();
	}

	public String getCurrentMaterial() {
		return getString("currentMaterial");
	}

	public void setCurrentMaterial(String currentMaterial) {
		put("currentMaterial", currentMaterial);
		saveInBackground();
	}

	public List<String> getCharactersCollected() {
		return getList("charactersCollected");
	}

	public void setCharactersCollected(List<String> newCharactersCollected) {
		put("charactersCollected", newCharactersCollected);
		saveInBackground();
	}

	public List<String> getMaterialsSolved() {
		return getList("materialsSolved");
	}

	public void setMaterialsSolved(List<String> materialsSolved) {
		put("materialsSolved", materialsSolved);
		saveInBackground();
	}

	public List<String> getMaterialsCollected() {
		return getList("materialsCollected");
	}

	public void setMaterialsCollected(List<String> materialsCollected) {
		put("materialsCollected", materialsCollected);
		saveInBackground();
	}

	public List<String> getItemsSolved() {
		return getList("itemsSolved");
	}

	public void setItemsSolved(List<String> itemsSolved) {
		put("itemsSolved", itemsSolved);
		saveInBackground();
	}

	public List<String> getItemsCollected() {
		return getList("itemsCollected");
	}

	public void setItemsCollected(List<String> itemsCollected) {
		put("itemsCollected", itemsCollected);
		saveInBackground();
	}

	public String getShuffledWord() {
		return getString("shuffledWord");
	}

	public void setShuffledWord(String shuffledWord) {
		put("shuffledWord", shuffledWord);
		saveInBackground();
	}

	public String getPuzzleID() {
		return getString("puzzleID");
	}

	public void setPuzzleID(String puzzleID) {
		put("puzzleID", puzzleID);
		saveInBackground();
	}

}
