package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;


/**
 * TrophiesActivity.java displays the trophies page view:
 * 		currently, the trophies view is defined by trophies.xml
 * 		The trophies page displays the current user's items collected thus far
 * 		if the main_menu_button Button is pressed, the view changes to the Main Menu view
 */

public class PhotosActivity extends Activity {
	
	
	private Button mainMenu;
	private TextView currentCharacter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.photos);
		setTitle(R.string.photos_view_name);
		LinearLayout lView = (LinearLayout) findViewById(R.id.photos_list);
		
		Person user = new Person();
		user.put("points", 0);
		user.put("levelAt", 1);
		user.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
		user.put("puzzleID", 101);
		user.put("currentItem", "coffee");
		user.put("currentIngredient", "water");
		ArrayList<String> characters = new ArrayList<String>(Arrays.asList("BeachBoy", "Granny"));
		// tests addCollectedCharacter method (see if persisted)
		user.put("charactersCollected", characters);
		user.saveInBackground();
		//user.addCollectedCharacter("Pusheen");
		//user.addCollectedCharacter("Baby");
		user.add("charactersCollected", "Pusheen");
		user.add("charactersCollected", "Baby");
		user.saveInBackground();
		ArrayList<String> charactersCollected = user.getCollectedCharacters();
		
		// display collected items as strings
		for (int i = 0; i < charactersCollected.size(); i++){
			currentCharacter = new TextView(this);
			currentCharacter.setText(charactersCollected.get(i));
			lView.addView(currentCharacter);
		}
		
		addListenerOnMainMenuButton();
	}
		
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_photos);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
}
