package com.parse.demo;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseGeoPoint;

/**
 * This class tests whether we can persist data to the cloud. 
 * Until we get querying working, we will have to manually check 
 * if the data was successfully saved in the cloud. 
 *
 */
public class PersistToCloudActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// temporary view for now
		setContentView(R.layout.persist_to_cloud);
		setTitle(R.string.create_todo);
		
		 // Testing Level and Puzzle Object
        Level levelObject = new Level();
        levelObject.put("levelNumber", 1);
        levelObject.put("points", 5);
        levelObject.put("puzzleId", 12);
        levelObject.saveInBackground();
        
        Puzzle puzzleObject = new Puzzle();
        puzzleObject.put("riddle", "What's is Stephanie's favorite animal?");
        puzzleObject.put("answer", "Pandas");
        puzzleObject.put("points", 5);
        puzzleObject.put("location", new ParseGeoPoint(23, 23));
        puzzleObject.saveInBackground();

	}

}
