// MAKE SURE TO UPDATE onClick in the addListenerOnStartContinueButton once
// everything is merged

package com.parse.starter;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseUser;

/**
 * The MainMenu view shows the main menu with a Start/Continue button that takes
 * the user to either the Puzzle View or the GPS View depending on their status
 * in the game. It also has a Log Out button that takes the User to the homepage
 */
public class MainMenuActivity extends Activity {

	private Button startContinue;
	private Button settings;
	private Button trophies;
	private Button photos;
	private Button inventory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mainmenu);
		setTitle(R.string.main_menu);

		addListenerOnStartContinueButton();
		addListenerOnSettingsButton();
		addListenerOnTrophiesButton();
		addListenerOnPhotosButton();
		addListenerOnInventoryButton();
	}

	/**
	 * When the Start/Continue Button is pressed, if the client is on the puzzle
	 * part, changes to Puzzle View if the client is on the GPS part, changes to
	 * GPS View
	 */
	private void addListenerOnStartContinueButton() {
		startContinue = (Button) findViewById(R.id.start_continue_button_mm);
		startContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// temporarily PuzzleActivity, will be changed to
				// CharacterActivity.class
				Intent i = new Intent(MainMenuActivity.this,
						PuzzleActivity.class);
				String character = ((User) User.getCurrentUser())
						.getCurrentCharacter();
				if (character != null) {
					i = new Intent(MainMenuActivity.this, MapActivity.class);
				}
				startActivity(i);
			}
		});
	}

	/**
	 * When the Settings Button is pressed, changes to Settings View
	 */
	private void addListenerOnSettingsButton() {
		settings = (Button) findViewById(R.id.settings_button_mm);
		settings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenuActivity.this,
						SettingsActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * When the Trophies Button is pressed, changes to trophies page, where all
	 * items collected by the current user is displayed
	 */
	private void addListenerOnTrophiesButton() {
		trophies = (Button) findViewById(R.id.trophies_button_mm);
		trophies.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainMenuActivity.this,
						TrophiesActivity.class);
				startActivity(i);
			}
		});
	}

	/**
	 * When the Photos Button is pressed, changes to Photo gallery, where all
	 * characters collected by the current user is displayed
	 */
	private void addListenerOnPhotosButton() {
		photos = (Button) findViewById(R.id.photos_button_mm);
		photos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), PhotosActivity.class);
				startActivity(i);
			}
		});
	}

	/**
	 * When the Inventory Button is pressed, changes to Inventory view, where
	 * all materials collected by the current user is displayed
	 */
	private void addListenerOnInventoryButton() {
		inventory = (Button) findViewById(R.id.inventory_button_mm);
		inventory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), InventoryActivity.class);
				startActivity(i);
			}
		});
	}

}