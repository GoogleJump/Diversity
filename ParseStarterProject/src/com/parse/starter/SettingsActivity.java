package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.ParseUser;

/**
 * SettingsActivity displays the Settings page of the app. Contains a MainMenu
 * button that returns user to Main Menu Contains a Logout Button that has a
 * Logout Dialog, which asks the User if they want to logout or not. Contains a
 * Restart Button that has a Restart Dialog, which asks the User if they want to
 * restart or not.
 */
public class SettingsActivity extends BaseActivity {

	private Button mainMenu;
	private Button restart;
	private Button logout;
	private Button about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.settings);
		setTitle(R.string.settings);

		logout = (Button) findViewById(R.id.logout_button_set);

		restart = (Button) findViewById(R.id.restart_button_set);

		addListenerOnRestartButton();
		addListenerOnLogOutButton();
		addListenerOnMainMenuButton();
		addListenerOnAboutButton();
	}

	/**
	 * When MainMenu Button is pressed, changes to MainMenu View
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_set);
		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						MainMenuActivity.class);
				SettingsActivity.this.finish();
				startActivity(intent);
			}
		});
	}

	/**
	 * When the Restart Button is pressed, restart dialog comes up
	 */
	private void addListenerOnRestartButton() {
		restart = (Button) findViewById(R.id.restart_button_set);
		restart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showRestartDialog();
			}
		});
	}

	/**
	 * When the Logout Button is pressed, logout dialog comes up
	 */
	private void addListenerOnLogOutButton() {
		logout = (Button) findViewById(R.id.logout_button_set);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showLogoutDialog();
			}
		});
	}
	
	/**
	 * When the About Button is pressed, changes to About View
	 */
	private void addListenerOnAboutButton() {
		about = (Button) findViewById(R.id.about_button_mm);
		about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						AboutActivity.class);
				SettingsActivity.this.finish();
				startActivity(intent);
			}
		});
	}

	/**
	 * Displays the restart dialog, which has a yes button to restart the game
	 * for the user, and a no button that returns to the settings view
	 */
	private void showRestartDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Restart");

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you would like to restart?");

		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						UserInfo userInfo = ((User) User.getCurrentUser()).getUserInfo();
						userInfo.restart();

						// Start and intent for the dispatch activity
						Intent intent = new Intent(SettingsActivity.this,
								MainMenuActivity.class);
						SettingsActivity.this.finish();
						startActivity(intent);

					}
				});

		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Displays the logout dialog, which has a yes button to logout the game for
	 * the user, and a no button that returns to the settings view
	 */
	private void showLogoutDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Logout");

		// set dialog message
		alertDialogBuilder.setMessage("Are you sure you would like to logout?");

		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Call the Parse log out method
						ParseUser.logOut();

						// Start and intent for the dispatch activity
						Intent intent = new Intent(SettingsActivity.this,
								SignUpOrLogInActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						SettingsActivity.this.finish();
						startActivity(intent);
					}
				});

		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}