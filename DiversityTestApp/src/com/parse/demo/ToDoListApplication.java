package com.parse.demo;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ToDoListApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		ParseObject.registerSubclass(Level.class);
		ParseObject.registerSubclass(Puzzle.class);

		Parse.initialize(this, "zfrqKuj3sHXZN5PzKBgQ6HSmK4SUR0nuTnFuMxAv", "3V4Ts4GRkS3R69kYpU1rplNHalACqjR2kcIV1Ws3");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		ParseACL.setDefaultACL(defaultACL, true);
	}

}
