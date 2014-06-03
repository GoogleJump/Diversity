package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {

		super.onCreate();		

		// must register the ParseObjects
		ParseObject.registerSubclass(Level.class);
		ParseObject.registerSubclass(Puzzle.class);
		ParseUser.registerSubclass(Person.class);
		ParseObject.registerSubclass(Item.class);
		ParseObject.registerSubclass(Character.class);

		// Add your initialization code here
		Parse.initialize(this, "xpqgrjmUYorIPfo8bUJ3pzj5Idz830HPpqiIT33g",
				"akCZNpnFr9dV21PiUkuTflZw2vYyMS5ZnA9rp1Hb");
	}

}
