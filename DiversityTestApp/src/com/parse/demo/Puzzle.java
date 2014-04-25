package com.parse.demo;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Puzzle")
public class Puzzle extends ParseObject{
    
    // A default constructor is required
    public Puzzle() {}
		
	public String getRiddle(){
		return getString("riddle");
	}
	
	public String getAnswer(){
		return getString("answer");
	}
	
	public int getPoints(){
		return getInt("points");
	}
	
	public ParseGeoPoint getLocation() {
	    return getParseGeoPoint("location");
	}

}
