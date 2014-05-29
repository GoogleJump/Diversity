package com.parse.starter;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The MainMenu view shows the main menu with a Start/Continue button that takes
 * the user to either the Puzzle View or the GPS View depending on their status
 * in the game. It also has a Log Out button that takes the User to the homepage
 */
public class MainMenuActivity extends Activity {
	
	private Button startContinue;
	private Button logout;
	private Button trophies;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mainmenu);
		setTitle(R.string.main_menu);
		
		addListenerOnStartContinueButton();
		addListenerOnLogOutButton();
		addListenerOnTrophiesButton();
	}
	
	/**
	 * When the Start/Continue Button is pressed,
	 * 		if the client is on the puzzle part, changes to Puzzle View
	 * 		if the client is on the GPS part, changes to GPS View
	 */
	private void addListenerOnStartContinueButton() {
		startContinue = (Button) findViewById(R.id.start_continue_button_mm);
		startContinue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), PuzzleActivity.class);

				// check somehow if client is on GPS or Puzzle section
				// if (on GPS section){
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
		logout.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// Intent i = new Intent(v.getContext(), IntroActivity.class);
				// startActivity(i);
			}
		});
	}
	
	/**
	 * When the Trophies Button is pressed,
	 * 		changes to trophies page, where all items collected by the current user is displayed 
	 */
	private void addListenerOnTrophiesButton() {
		trophies = (Button) findViewById(R.id.trophies_button_mm);
		trophies.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), TrophiesActivity.class);
				startActivity(i);
			}
		});
	}


}