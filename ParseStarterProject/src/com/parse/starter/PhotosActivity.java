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
import com.parse.ParseUser;


/**
 * TrophiesActivity.java displays the trophies page view:
 * 		currently, the trophies view is defined by trophies.xml
 * 		The trophies page displays the current user's items collected thus far
 * 		if the main_menu_button Button is pressed, the view changes to the Main Menu view
 */

public class PhotosActivity extends Activity {
	
	
	private Button mainMenu;
	private Button trophyShelf;
	private TextView currentCharacter = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.photos);
		setTitle(R.string.photos_view_name);
		LinearLayout lView = (LinearLayout) findViewById(R.id.photos_list);
		
		
		// get current user's list of collected characters to display
		User currentUser = ((User) User.getCurrentUser());
		ArrayList<String> charactersCollected = null;
		if (currentUser != null) {
			charactersCollected = currentUser.getCharactersCollected();
		}
		else { // display login page
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}
		
		
		// display collected items as strings
		for (int i = 0; i < charactersCollected.size(); i++){
			currentCharacter = new TextView(this);
			currentCharacter.setText(charactersCollected.get(i));
			lView.addView(currentCharacter);
		}
		
		addListenerOnMainMenuButton();
		addListenerOnTrophiesButton();
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
	
	
	/**
	 * When the Trophies Button is pressed, view changes to Trophy Shelf
	 */
	private void addListenerOnTrophiesButton() {
		trophyShelf = (Button) findViewById(R.id.trophies_button_photos);
		trophyShelf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), TrophiesActivity.class);
				startActivity(i);
				
			}
		});
	}
}
