package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Puzzle")
public class Puzzle extends ParseObject{
		
	public String getRiddle(){
		return getString("riddle");
	}
	
	public String getAnswer(){
		return getString("answer");
	}
	
	public int getPoint(){
		return getInt("point");
	}
	
	public ParseGeoPoint getLocation() {
	    return getParseGeoPoint("location");
	}

}
