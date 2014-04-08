package

import com.parse.ParseObject;
import com.parse.ParseClassName;

/**
* User is a read and write object. How do we integrate this with ParseUser??? VERY UNFINISHED
*/

@ParseClassName("User")
public class User extends ParseObject {

	// For more information about ParseUser:
	// https://parse.com/docs/android/api/com/parse/ParseUser.html
	// Should the signing up exist outside of this class?
	public ParseUser createUser(String username, String password, String email, Puzzle puzzle) {
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.put("points", 0);
		user.put("levelAt", 1);
		user.put("puzzleAt", puzzle);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e==null) {
					// User is signed up and logged in
				}
				else {
					// sign up didn't succeed. ParseException
				}
			}
		});
		return user; 
	}

	// wrapper for isAuthenticated() in ParseUser
	public boolean isAuthenticated() {
		ParseUser user = ParseUser.getCurrentUser();
		return user.isAuthenticated();
	}

	public boolean isRegistered() {
		// should this exist outside the class? 
	}

	public String getLocation() {
		return getString("location");
	}

	public int getLevel() {
		return getInt("levelAt");
	}

	public Puzzle getPuzzle() {
		return getObject("puzzleAt"); // Is getObject correct?
	}

	public void updatePoints() {
		//ParseUser user = ParseUser.getCurrentUser();
		int levelPoints = getObject("levelAt").getInt("points"); // Check syntax with level class
		increment("points", levelPoints);
		saveInBackground();
	}

}