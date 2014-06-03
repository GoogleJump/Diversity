package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// must register the ParseObjects
		ParseObject.registerSubclass(Level.class);
		ParseObject.registerSubclass(Puzzle.class);

		// Add your initialization code here
		Parse.initialize(this, "xpqgrjmUYorIPfo8bUJ3pzj5Idz830HPpqiIT33g",
				"akCZNpnFr9dV21PiUkuTflZw2vYyMS5ZnA9rp1Hb");
	}

}
