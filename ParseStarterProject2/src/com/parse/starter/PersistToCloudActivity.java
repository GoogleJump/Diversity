package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseGeoPoint;

public class PersistToCloudActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
