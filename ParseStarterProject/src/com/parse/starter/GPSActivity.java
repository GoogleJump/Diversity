package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseGeoPoint;

/**
 * GPSActivity displays the GPS view:
 * 		currently, the puzzle view is defined by gps.xml
 * 		responsible for checking if client is in the correct location (to get points)
 * 		if submit is pressed while the client is in the correct location, changes to Main Menu view
 * @author Jennifer
 * @author Jennifer
 *
 */
public class GPSActivity extends Activity {

	private Button mainMenu;
	private Button submit;
	private ParseGeoPoint location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gps);
		setTitle(R.string.gps_view_name);
		
		// Persist Level object
		// We may want to add a Level number view later as well
		Level levelObject = new Level();
		levelObject.put("levelNumber", 100);
		levelObject.put("points", 100);
		levelObject.put("puzzleID", 10);
		levelObject.saveInBackground();
		

		// Persist Puzzle object
		Puzzle puzzleObject = new Puzzle();
		puzzleObject.put("riddle", "What is the favorite animal of Jennifer?");
		puzzleObject.put("answer", "Pusheen");
		puzzleObject.put("points", 100);
		puzzleObject.put("location", new ParseGeoPoint(23, 23));
		puzzleObject.saveInBackground();
		
		location = puzzleObject.getLocation(); 
		
		addListenerOnMainMenuButton();
		addListenerOnSubmitButton();

	}
	
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	public void addListenerOnMainMenuButton(){
		mainMenu = (Button) findViewById(R.id.main_menu_button_gps);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
	
	/**
	 * When the submit Button is pressed
	 * 		if the client is in the correct location, view changes to MainMenuView
	 * 		otherwise, view stays the same
	 */
	public void addListenerOnSubmitButton(){
		submit = (Button) findViewById(R.id.submit_button_gps);
		submit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				// check somehow if GPS matches location
				// if (matches){
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				//}
				
				// if it doesn't match, stay on this view
			}
		});
	}
}
