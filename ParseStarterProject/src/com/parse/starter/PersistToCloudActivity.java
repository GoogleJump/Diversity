package com.parse.starter;

import android.app.Activity;
import java.util.ArrayList;
import android.os.Bundle;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
 * This class is an example of how to persist data to cloud
 * and link a view to an activity.  
 *
 */
public class PersistToCloudActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set view to the persist_to_cloud.xml
		setContentView(R.layout.persist_to_cloud);
		setTitle(R.string.app_name);

//		// Persist Level object
//		Level levelObject = new Level();
//		levelObject.put("levelNumber", 1);
//		levelObject.put("points", 5);
//		levelObject.put("puzzleId", 12);
//		levelObject.saveInBackground();
//
//		// Persist Puzzle object
//		Puzzle puzzleObject = new Puzzle();
//		puzzleObject.put("riddle", "What's is Stephanie's favorite animal?");
//		puzzleObject.put("answer", "Pandas");
//		puzzleObject.put("points", 5);
//		puzzleObject.put("location", new ParseGeoPoint(23, 23));
//		puzzleObject.saveInBackground();
//		
//		// Persist User object
//		////User user = new User("username", "password", "email@example.com", 100);
//		User user = new User();
//		user.put("points", 0);
//		user.put("levelAt", 1);
//		user.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
//		user.put("puzzleID", 101);
//		user.put("currentItem", "coffee");
//		user.put("currentIngredient", "water");
//		user.saveInBackground();
		
		// Persist Item object
		Item itemObject = new Item();
		itemObject.put("itemName", "coffee mug");
		itemObject.put("itemID", 10);
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("water");
		ingredients.add("coffee");
		ingredients.add("milk");
		ingredients.add("sugar");
		ingredients.add("filter");
		itemObject.put("itemIngredients", ingredients);
		itemObject.saveInBackground();
		
	}

}
