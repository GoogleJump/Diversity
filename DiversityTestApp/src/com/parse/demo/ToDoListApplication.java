package com.parse.demo;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ToDoListApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "zfrqKuj3sHXZN5PzKBgQ6HSmK4SUR0nuTnFuMxAv", "3V4Ts4GRkS3R69kYpU1rplNHalACqjR2kcIV1Ws3");


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		// Optionally enable public read access.
		// defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
