package com.parse.starter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * TrophiesActivity.java displays the trophies page view: currently, the
 * trophies view is defined by trophies.xml The trophies page displays the
 * current user's items collected thus far if the main_menu_button Button is
 * pressed, the view changes to the Main Menu view
 */

public class TrophiesActivity extends Activity {

	private Button mainMenu;
	private Button photoAlbum;
	private Button inventory;
	private TextView currentItem = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.trophies);
		setTitle(R.string.trophies_view_name);
		LinearLayout lView = (LinearLayout) findViewById(R.id.trophies_list);

		// get current user's list of collected characters to display
		User currentUser = null;
		if (User.getCurrentUser() instanceof User)
			currentUser = ((User) User.getCurrentUser());
		ArrayList<String> itemsCollected = null;
		if (currentUser != null) {
			itemsCollected = currentUser.getItemsCollected();
		} else { // display login page
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}

		// display collected items as strings
		if (itemsCollected != null) {
			for (int i = 0; i < itemsCollected.size(); i++) {
				currentItem = new TextView(this);
				currentItem.setText(itemsCollected.get(i));
				lView.addView(currentItem);
			}
		}

		addListenerOnMainMenuButton();
		addListenerOnPhotosButton();
		addListenerOnInventoryButton();
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
	
	/**
	 * When the Inventory Button is pressed,
	 * 		changes to Inventory view, where all materials collected by the current user is displayed 
	 */
	private void addListenerOnInventoryButton() {
		inventory = (Button) findViewById(R.id.inventory_button_trophies);
		inventory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), InventoryActivity.class);
				startActivity(i);
			}
		});
	}
}
