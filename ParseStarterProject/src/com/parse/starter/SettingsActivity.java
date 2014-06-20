package com.parse.starter;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * SettingsActivity displays the Settings page of the app.
 * Contains a MainMenu button that returns user to Main Menu
 * Contains a Logout Button that has a Logout Popup, which 
 * 		asks the User if they want to logout or not.
 * Contains a Restart Button that has a Restart Popup, which 
 * 		asks the User if they want to restart or not.
 */
public class SettingsActivity extends Activity {
	
	private Button mainMenu;
	private Button restart;
	private Button logout;
	private PopupWindow popupLogout;
	private PopupWindow popupRestart;
	private Button insidePopupLogoutYes;
	private Button insidePopupLogoutNo;
	private Button insidePopupRestartYes;
	private Button insidePopupRestartNo;
	private LinearLayout layoutOfPopupLogout;
	private LinearLayout layoutOfPopupRestart;
	private TextView popupLogoutText;
	private TextView popupRestartText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);
		setTitle(R.string.settings);

		init();
		popupLogoutInit();
		popupRestartInit();
		
		addListenerOnRestartButton();
		addListenerOnLogOutButton();
		addListenerOnMainMenuButton();
	}
	
	/**
	 * Initializes the popup for the logout button with yes and no button
	 * When yes button is pressed, the user is logged out
	 * When no button is pressed, the user stays on Settings page
	 */
	public void popupLogoutInit() {
		popupLogout = new PopupWindow(layoutOfPopupLogout);
		popupLogout.setContentView(layoutOfPopupLogout);
		if (logout == null) {
			Log.d("MyApp","logout is null");
		}
		
		// log out user when yes is pressed
		insidePopupLogoutYes.setOnClickListener(new OnClickListener() {
			@Override
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
		
		// keep user on same page when no is pressed
		insidePopupLogoutNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupLogout.dismiss();
			}
		});
		
		popupLogout = new PopupWindow(layoutOfPopupLogout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupLogout.setContentView(layoutOfPopupLogout);
	}
	
	/**
	 * Initializes popup for restart button with yes and no buttons
	 * When yes is pressed, game is restarted
	 * When no is pressed, user stays on same page
	 */
	public void popupRestartInit() {
		popupRestart = new PopupWindow(layoutOfPopupRestart);
		popupRestart.setContentView(layoutOfPopupRestart);
		if (restart == null) {
			Log.d("MyApp","restart is null");
		}
		
		// restart user's progress when yes is pressed
		insidePopupRestartYes.setOnClickListener(new OnClickListener() {
			@Override
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
		
		// keep user on same page when this is pressed
		insidePopupRestartNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupRestart.dismiss();
			}
		});
		popupRestart = new PopupWindow(layoutOfPopupRestart,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupRestart.setContentView(layoutOfPopupRestart);
	}
	
	/**
	 * Initializes the Setting View, the Logout Popup, and the Restart Popup
	 */
	public void init() {
		logout = (Button) findViewById(R.id.logout_button_set);
		insidePopupLogoutYes = new Button(this);
		insidePopupLogoutNo = new Button(this);
		layoutOfPopupLogout = new LinearLayout(this);
		popupLogoutText = new TextView(this);
		popupLogoutText.setText("Are you sure you want to log out?");
		popupLogoutText.setPadding(0,0,0,20);
		insidePopupLogoutYes.setText("Yes");
		insidePopupLogoutNo.setText("No");
		layoutOfPopupLogout.setOrientation(1);
		layoutOfPopupLogout.addView(popupLogoutText);
		layoutOfPopupLogout.addView(insidePopupLogoutYes);
		layoutOfPopupLogout.addView(insidePopupLogoutNo);
		
		restart = (Button) findViewById(R.id.restart_button_set);
		insidePopupRestartYes = new Button(this);
		insidePopupRestartNo = new Button(this);
		layoutOfPopupRestart = new LinearLayout(this);
		popupRestartText = new TextView(this);
		popupRestartText.setText("Are you sure you want to restart?");
		popupRestartText.setPadding(0,0,0,20);
		insidePopupRestartYes.setText("Yes");
		insidePopupRestartNo.setText("No");
		layoutOfPopupRestart.setOrientation(1);
		layoutOfPopupRestart.addView(popupRestartText);
		layoutOfPopupRestart.addView(insidePopupRestartYes);
		layoutOfPopupRestart.addView(insidePopupRestartNo);
	}
	
	
	/**
	 * When the Restart Button is pressed, restart popup comes up
	 */
	private void addListenerOnRestartButton() {
		restart = (Button) findViewById(R.id.restart_button_set);
		restart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				popupRestart.showAsDropDown(restart,0,0);
			}
		});
	}
	
	/**
	 * When MainMenu Button is pressed, changes to MainMenu View
	 */
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
	 * When the Logout Button is pressed, logout popup comes up
	 */
	private void addListenerOnLogOutButton() {
		logout = (Button) findViewById(R.id.logout_button_set);
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				popupLogout.showAsDropDown(logout,0,0);				
			}
		});
	}
}
