package com.parse.starter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseGeoPoint;

/**
 * This class is an example of how to persist data to cloud
 * and link a view to an activity. For testing purposes only. 
 *
 */
public class PersistToCloudActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set view to the persist_to_cloud.xml
		setContentView(R.layout.persist_to_cloud);
		setTitle(R.string.app_name);
		
		// Persist Item object
		Item itemObject = new Item();
		itemObject.put("name", "coffee mug");
		itemObject.put("ID", 10);
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("water");
		ingredients.add("coffee");
		ingredients.add("milk");
		ingredients.add("sugar");
		ingredients.add("filter");
		itemObject.put("ingredients", ingredients);
		itemObject.saveInBackground();
		

		// Persist Level object
		Level levelObject = new Level();
		levelObject.put("levelNumber", 1);
		levelObject.put("points", 5);
		levelObject.put("puzzleId", 12);
		levelObject.saveInBackground();

		// Persist Puzzle object
		Puzzle puzzleObject = new Puzzle();
		puzzleObject.put("riddle", "What's is Ashley's favorite animal?");
		puzzleObject.put("answer", "Yorkiepoos");
		puzzleObject.put("points", 5);
		puzzleObject.put("location", new ParseGeoPoint(23, 23));
		puzzleObject.saveInBackground();

	}

}
