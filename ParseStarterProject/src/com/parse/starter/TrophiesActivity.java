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

public class TrophiesActivity extends Activity {
	
	
	private Button mainMenu;
	private Button photoAlbum;
	private TextView currentItem = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.trophies);
		setTitle(R.string.trophies_view_name);
		LinearLayout lView = (LinearLayout) findViewById(R.id.trophies_list);
		
		// get current user's list of collected characters to display
		User currentUser = ((User) User.getCurrentUser());
		ArrayList<String> itemsCollected = null;
		if (currentUser != null) {
			itemsCollected = currentUser.getItemsCollected();
		}
		else { // display login page
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}
				
		
		// display collected items as strings
		for (int i = 0; i < itemsCollected.size(); i++){
			currentItem = new TextView(this);
			currentItem.setText(itemsCollected.get(i));
			lView.addView(currentItem);
			
		}
		
		addListenerOnMainMenuButton();
		addListenerOnPhotosButton();
	}
		
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_trophies);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
	
	/**
	 * When the photos Button is pressed, view changes to Photo Album
	 */
	private void addListenerOnPhotosButton() {
		photoAlbum = (Button) findViewById(R.id.photos_button_trophies);
		photoAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), PhotosActivity.class);
				startActivity(i);
				
			}
		});
	}
}
