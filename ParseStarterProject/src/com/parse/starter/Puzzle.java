package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
	
	public static ParseQuery<Puzzle> getQuery() {
	    return ParseQuery.getQuery(Puzzle.class);
	}

}
