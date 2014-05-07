package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * MainMenuActivity displays the Main Menu view
 * The view includes a Start/Continue button that takes the User to the Puzzle view
 * 		or GPS view depending on the User's state -- THIS ISN'T DONE YET
 * and a Logout button that takes the User to the Home screen
 */
public class MainMenuActivity extends Activity {
	
	private Button startContinue;
	private Button logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_menu);
		setTitle(R.string.main_menu);
		
		addListenerOnStartContinueButton();
		addListenerOnLogOutButton();
	}
	
	/**
	 * When the Start/Continue Button is pressed,
	 * 		if the client is on the puzzle part, changes to Puzzle View
	 * 		if the client is on the GPS part, changes to GPS View
	 */
	private void addListenerOnStartContinueButton() {
		startContinue = (Button) findViewById(R.id.start_continue_button_mm);
		startContinue.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), PuzzleActivity.class);

				// check somehow if client is on GPS or Puzzle section
				// if (on GPS section) {
				//     i = new Intent(v.getContext(), GPSActivity.class);
				//}
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
			@Override
			public void onClick(View v) {
				// Intent i = new Intent(v.getContext(), IntroActivity.class);
				// startActivity(i);
			}
		});
	}


}
