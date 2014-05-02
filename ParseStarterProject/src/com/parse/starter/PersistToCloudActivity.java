package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseGeoPoint;

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

		// Persist Level object
		Level levelObject = new Level();
		levelObject.put("levelNumber", 1);
		levelObject.put("points", 5);
		levelObject.put("puzzleId", 12);
		levelObject.saveInBackground();

		// Persist Puzzle object
		Puzzle puzzleObject = new Puzzle();
		puzzleObject.put("riddle", "What's is Stephanie's favorite animal?");
		puzzleObject.put("answer", "Pandas");
		puzzleObject.put("points", 5);
		puzzleObject.put("location", new ParseGeoPoint(23, 23));
		puzzleObject.saveInBackground();
	}

}
