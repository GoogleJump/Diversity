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
 * InventoryActivity.java displays the inventory page view:
 * 		currently, the inventory view is defined by inventory.xml
 * 		The inventory page displays the current user's materials collected thus far.
 * 		if the main_menu_button Button is pressed, the view changes to the Main Menu view
 *		if the materials collected create an item, that item is added to the trophy shelf
 *      and then those materials disappear from the inventory	(TO DO)
 */

public class InventoryActivity extends Activity {
	
	
	private Button mainMenu;
	private Button photoAlbum;
	private Button trophyShelf;
	private TextView currentMaterial = null;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.inventory);
		setTitle(R.string.inventory_view_name);
		LinearLayout lView = (LinearLayout) findViewById(R.id.inventory_list);
		
		// get current user's list of collected characters to display
		User currentUser = null;
		if (User.getCurrentUser() instanceof User)
			currentUser = ((User) User.getCurrentUser());
		ArrayList<String> materialsCollected = null;
		if (currentUser != null) {
			materialsCollected = currentUser.getMaterialsCollected();
		}
		else { // display login page
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}
				
		
		// display collected items as strings
		for (int i = 0; i < materialsCollected.size(); i++){
			currentMaterial = new TextView(this);
			currentMaterial.setText(materialsCollected.get(i));
			lView.addView(currentMaterial);
			
		}
		
		addListenerOnMainMenuButton();
		addListenerOnPhotosButton();
		addListenerOnTrophiesButton();
	}
		
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_inventory);
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
		photoAlbum = (Button) findViewById(R.id.photos_button_inventory);
		photoAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), PhotosActivity.class);
				startActivity(i);
				
			}
		});
	}
	
	/**
	 * When the trophies Button is pressed, view changes to Trophy Shelf
	 */
	private void addListenerOnTrophiesButton() {
		trophyShelf = (Button) findViewById(R.id.trophies_button_inventory);
		trophyShelf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), TrophiesActivity.class);
				startActivity(i);
				
			}
		});
	}
}
