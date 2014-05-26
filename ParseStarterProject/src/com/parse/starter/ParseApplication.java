package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		// must register the ParseObjects
		ParseObject.registerSubclass(Level.class);
		ParseObject.registerSubclass(Puzzle.class);
		ParseUser.registerSubclass(User.class);

		// Add your initialization code here
		Parse.initialize(this, "xpqgrjmUYorIPfo8bUJ3pzj5Idz830HPpqiIT33g", "akCZNpnFr9dV21PiUkuTflZw2vYyMS5ZnA9rp1Hb");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
//		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
