package com.parse.starter;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("UserInfo")
public class UserInfo extends ParseObject {

	// A default constructor is required
	public UserInfo() {
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
	
	public int getPuzzleID() {
		return getInt("puzzleID");
	}

	public void setPuzzleID(int puzzleID) {
		put("puzzleID", puzzleID);
		saveInBackground();
	}

}
