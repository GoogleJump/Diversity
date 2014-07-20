package com.parse.starter;

import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * User is a read and write object.
 */
@ParseClassName("_User")
public class User extends ParseUser {

	// empty constructor is required
	public User() {
	}

	// constructor for our wrapper class for ParseUser
	public User(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
		saveEventually();
	}

	public UserInfo getUserInfo() {
		String usernmae = getString("username");
		ParseQuery<UserInfo> query = ParseQuery.getQuery(UserInfo.class);
		query.whereEqualTo("username", usernmae);
		try {
			List<UserInfo> results = query.find();
			return results.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}