package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * PickCharacterActivity.java lets you pick a character by showing their
 * pictures and showing popups with character descriptions. If you press the
 * Main Menu Button, the activity changes to MainMenuActivity.java.
 * PickCharacterActivity.java uses the layouts viewpager.xml, surfer.xml, and
 * grandma.xml in order to provide swiping functionality.
 */

public class AboutActivity extends Activity {
	
	private Button mainMenu;
	private UserInfo userInfo;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.about);

		userInfo = null;
		if (User.getCurrentUser() instanceof User) {
			userInfo = ((User) User.getCurrentUser()).getUserInfo();
		}
		if (userInfo == null) {
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}

		addListenerOnMainMenuButton();
	}

	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_pick_character);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				AboutActivity.this.finish();
				startActivity(i);
			}
		});
	}

}