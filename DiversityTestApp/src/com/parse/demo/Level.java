package com.parse.demo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Level")
public class Level extends ParseObject {
    
    // A default constructor is required
    public Level() {}
	
	public int getLevelNumber() {
		return getInt("levelNumber");
	}
	
	public int getPoints() {
		return getInt("points");
	}
	
	public int getPuzzleId() {
		return getInt("puzzleId");
	}
}
