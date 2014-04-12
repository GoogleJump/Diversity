package com.parse.starter;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Level")
public class Level extends ParseObject{
	public void onCreate(int level_number, String objectID, String clientKey) {
		Parse.initialize(this, objectID, clientKey);
		ParseObject Level = new ParseObject("Level");
		Level.put("levelNumber", level_number);
		Level.put("points", 10); //getPoints(level_number);
	
	}
	
	public int getLevelNumber(){
		return getInt("levelNumber");
	}
	
	public int getPoints(){
		return getInt("points");
	}
	
	public ParseObject getPuzzle(){
		ParseObject currentPuzzle = new ParseObject("Puzzle");
		int levelNumber = this.getLevelNumber();
		currentPuzzle.put("levelNumber",levelNumber);
		return currentPuzzle;
	}
}
