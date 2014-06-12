package com.parse.starter;

import java.util.ArrayList;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Material")
public class Material extends ParseObject {
    
    // A default constructor is required
    public Material() {}
    
	public String getName() {
		return getString("name");
	}
    
    public int geID() {
    	return getInt("ID");
    }
	
}

