package

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
* User is a read and write object. How do we integrate this with ParseUser??? VERY UNFINISHED
*/

@ParseClassName("User")
public class User extends ParseObject {

	// For more information about ParseUser:
	// https://parse.com/docs/android/api/com/parse/ParseUser.html

	private ParseUser user; 

	// constructor for our wrapper class for ParseUser
	public User(String username, String password, String email, 
		Puzzle puzzle) {
		user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.put("points", 0);
		user.put("levelAt", 1);
		user.put("puzzleAt", puzzle);
	}

	// wrapper for isAuthenticated() in ParseUser
	public boolean isAuthenticated() {
		return user.isAuthenticated();
	}

	public ParseGeoPoint getLocation() {
		return getParseGeoPoint("location");
	}

	public int getPoints() {
		return getInt("points");
	}

	public int getLevel() {
		return getInt("levelAt");
	}

	public Puzzle getPuzzle() {
		return (Puzzle) getParseObject("puzzleAt"); 
	}

	public void incrementPoints(int points) {
		increment("points", points);
		saveInBackground();
	}
	
	public void incrementLevel() {
		increment("levelAt");
		saveInBackground();
	}

}

