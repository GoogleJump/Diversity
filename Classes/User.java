ParseObject user = new ParseObject("User");
user.put("id", "Carolyn");
user.put("points", 0);
user.put("levelAt", 1);
user.put("registered", true);
user.saveInBackground();

String objectID = user.getObjectID();

// Retrieve the user by id
query.getInBackground(objectID, new GetCallback<ParseObject>() {
	public void done(ParseObject user, ParseExeption e) {
		if (e == null) {
			// update user with new data
			user.put("points", points);
			user.put("levelAt", levelAt);
			// etc....
			user.saveInBackground();
		}
	}
});

user.increment("levelAt");
user.saveInBackground();
user.increment("points", 5);
user.saveInBackground();

ParseUser user = ParseUser.getCurrentUser();