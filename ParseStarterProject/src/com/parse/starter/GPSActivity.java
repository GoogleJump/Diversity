package com.parse.starter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
 */
public class GPSActivity extends Activity {

	private Button mainMenu;
	private Button submit;
	private ParseGeoPoint location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gps);
		setTitle(R.string.gps_view_name);
		
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
	
	protected void onStart() {
		super.onStart();
		
		// The activity is either being restarted or started for the first time
	    // so this is where we should make sure that GPS is enabled
	    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    
	    if (!gpsEnabled) {
	        // Create a dialog here that requests the user to enable GPS, and use an intent
	        // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
	        // to take the user to the Settings screen to enable GPS when they click "OK"
	    }
	    
	}
	
	protected void onRestart() {
		super.onRestart();
	}
		
	protected void onResume() {
		super.onResume();
	}
	
	protected void onPause() {
		super.onPause();
	}
	
	protected void onStop() {
		super.onStop();
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}

	
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
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
	private void addListenerOnSubmitButton() {
		submit = (Button) findViewById(R.id.submit_button_gps);
		submit.setOnClickListener(new OnClickListener() {
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
