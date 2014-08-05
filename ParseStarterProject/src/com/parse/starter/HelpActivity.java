package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * SettingsActivity displays the Settings page of the app. Contains a MainMenu
 * button that returns user to Main Menu Contains a Logout Button that has a
 * Logout Dialog, which asks the User if they want to logout or not. Contains a
 * Restart Button that has a Restart Dialog, which asks the User if they want to
 * restart or not.
 */
public class HelpActivity extends BaseActivity {
	
	private Button mainMenu;
	private ImageButton play;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.help);
		setTitle(R.string.help);

		addListenerOnMainMenuButton();
		addListenerOnPlayButton();
	}

	/**
	 * When MainMenu Button is pressed, changes to MainMenu View
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_set);
		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HelpActivity.this,
						MainMenuActivity.class);
				HelpActivity.this.finish();
				startActivity(intent);
			}
		});
	}
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnPlayButton() {
		play = (ImageButton) findViewById(R.id.map_help);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HelpActivity.this, MapActivity.class);
				HelpActivity.this.finish();
				startActivity(i);
			}
		});
	}

}