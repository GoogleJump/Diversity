package com.parse.starter;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Puzzle")
public class Puzzle extends ParseObject{
	public void onCreate(int level_number, String objectID, String clientKey){
		Parse.initialize(this,  objectID,  clientKey);
		ParseObject Puzzle = new ParseObject("Puzzle");
		Puzzle.put("levelNumber",  level_number);
		Puzzle.put("riddle", "What is 1+1?");
		Puzzle.put("answer", "2");
		Puzzle.put("point",  1);
	}
	
	
	public int getLevelNumber(){
		return getInt("levelNumber");
	}
	
	public String getRiddle(){
		return getString("riddle");
	}
	
	public String getAnswer(){
		return getString("answer");
	}
	
	public int getPoint(){
		return getInt("point");
	}

}
