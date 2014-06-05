package com.parse.starter;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
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
	private Button logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mainmenu);
		setTitle(R.string.main_menu);

		addListenerOnStartContinueButton();
		addListenerOnLogOutButton();
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
				Intent i = new Intent(v.getContext(), PuzzleActivity.class);
				int state = ((User) User.getCurrentUser()).getState();
				// if client is on GPS section
				if (state == 0) {
					i = new Intent(v.getContext(), GPSActivity.class);
				}
				startActivity(i);
			}
		});
	}

	/**
	 * When the Logout Button is pressed, changes to Intro View
	 */
	private void addListenerOnLogOutButton() {
		logout = (Button) findViewById(R.id.logout_button_mm);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Call the Parse log out method
				ParseUser.logOut();
				// Start and intent for the dispatch activity
				Intent intent = new Intent(MainMenuActivity.this,
						ParseStarterProjectActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

}