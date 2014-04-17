package com.parse.starter;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		// must register the ParseObjects
		ParseObject.registerSubclass(Level.class);
		ParseObject.registerSubclass(Puzzle.class);

		// Add your initialization code here
		Parse.initialize(this, "xpqgrjmUYorIPfo8bUJ3pzj5Idz830HPpqiIT33g", "akCZNpnFr9dV21PiUkuTflZw2vYyMS5ZnA9rp1Hb");
		
//		// Testing Level and Puzzle Object
//		Level levelObject = new Level();
//		levelObject.put("levelNumber", 1);
//		levelObject.put("points", 5);
//		levelObject.put("puzzleId", 12);
//		levelObject.saveInBackground();
//		
//		Puzzle puzzleObject = new Puzzle();
//		puzzleObject.put("riddle", "What's is Stephanie's favorite animal?");
//		puzzleObject.put("answer", "Pandas");
//		puzzleObject.put("points", 5);
//		puzzleObject.put("location", new ParseGeoPoint(23, 23));
//		puzzleObject.saveInBackground();

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
