package com.parse.starter;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	private Button mainMenu;
	private Button restart;
	private Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);
		setTitle(R.string.settings);

		addListenerOnRestartButton();
		addListenerOnLogOutButton();
		addListenerOnMainMenuButton();
	}

	/**
	 * When the Restart Button is pressed, resets the current user's 
	 * credentials and changes to MainMenuView
	 */
	private void addListenerOnRestartButton() {
		restart = (Button) findViewById(R.id.restart_button_set);
		restart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				User.getCurrentUser();
				if (User.getCurrentUser() instanceof User) {
					User currentUser = ((User) User.getCurrentUser());
					currentUser.restart();
					// Start and intent for the dispatch activity
					Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
					startActivity(intent);
				}
				else {
					// not sure what goes here yet
				}
			}
		});
	}
	
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_set);
		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * When the Logout Button is pressed, changes to Intro View
	 */
	private void addListenerOnLogOutButton() {
		logout = (Button) findViewById(R.id.logout_button_set);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Call the Parse log out method
				ParseUser.logOut();
				// Start and intent for the dispatch activity
				Intent intent = new Intent(SettingsActivity.this,
						ParseStarterProjectActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}
}
