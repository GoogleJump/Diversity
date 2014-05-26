package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
* User is a read and write object.
*/

@ParseClassName("User")
public class User extends ParseUser {

	// For more information about ParseUser:
	// https://parse.com/docs/android/api/com/parse/ParseUser.html

	private ParseUser user; 

	// constructor for our wrapper class for ParseUser
	public User(String username, String password, String email, 
		int puzzle) {
		user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.put("points", 0);
		user.put("levelAt", 1);
		user.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
		user.put("puzzleID", puzzle);
	}

	// wrapper for isAuthenticated() in ParseUser
	public boolean isAuthenticated() {
		return user.isAuthenticated();
	}

	public int getPoints() {
		return getInt("points");
	}

	public int getLevel() {
		return getInt("levelAt");
	}

	public int getPuzzle() {
		return getInt("puzzleID"); 
	}

	public void incrementPoints(int points) {
		increment("points", points);
		saveInBackground();
	}

	public void incrementLevel() {
		increment("levelAt");
		saveInBackground();
	}
	
	public void incrementState() {
		increment("stateAt");
		saveInBackground();
	}


}

