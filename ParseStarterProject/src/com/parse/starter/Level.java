package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Level")
public class Level extends ParseObject {

	// A default constructor is required
	public Level() {
	}

	public int getLevelNumber() {
		return getInt("levelNumber");
	}

	public int getPoints() {
		return getInt("points");
	}

	public int getPuzzleId() {
		return getInt("puzzleId");
	}

	public static ParseQuery<Level> getQuery() {
		return ParseQuery.getQuery(Level.class);
	}
}
