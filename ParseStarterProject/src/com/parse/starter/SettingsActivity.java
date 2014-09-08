package com.parse.starter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import com.parse.ParseUser;
import com.team.diversity.android.R;

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
	private Button help;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.activity_settings);
		setTitle(R.string.settings);

		logout = (Button) findViewById(R.id.logout_button_set);

		restart = (Button) findViewById(R.id.restart_button_set);

		addListenerOnRestartButton();
		addListenerOnLogOutButton();
		addListenerOnMainMenuButton();
		addListenerOnAboutButton();
		addListenerOnHelpButton();
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
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
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
				startActivity(intent);
			}
		});
	}
	
	/**
	 * When the Help Button is pressed, changes to Help View
	 */
	private void addListenerOnHelpButton() {
		help = (Button) findViewById(R.id.help_button);
		help.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(SettingsActivity.this,
						HelpActivity.class);
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

        final Dialog myDialog = new Dialog(context);     
        myDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.two_button_dialog);
        myDialog.setCancelable(false);

        TextView dialog_title = (TextView) myDialog.findViewById(R.id.title);
        dialog_title.setText(R.string.dialog_restart);

        TextView dialog_message = (TextView) myDialog.findViewById(R.id.message);
        dialog_message.setText(R.string.dialog_restart_detail);

        Button yes = (Button) myDialog.findViewById(R.id.dialog_yes);
        yes.setText(R.string.dialog_yes);
        
        Button no = (Button) myDialog.findViewById(R.id.dialog_no);
        no.setText(R.string.dialog_no);
        
        yes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	myDialog.dismiss();
				UserInfo userInfo = ((User) User.getCurrentUser()).getUserInfo();
				userInfo.restart();

				Intent intent = new Intent(SettingsActivity.this,
						MainMenuActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
		
            }
        });
        
        no.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }
	

	/**
	 * Displays the logout dialog, which has a yes button to logout the game for
	 * the user, and a no button that returns to the settings view
	 */
	private void showLogoutDialog() {

        final Dialog myDialog = new Dialog(context);     
        myDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.two_button_dialog);
        myDialog.setCancelable(false);

        TextView dialog_title = (TextView) myDialog.findViewById(R.id.title);
        dialog_title.setText(R.string.dialog_logout);

        TextView dialog_message = (TextView) myDialog.findViewById(R.id.message);
        dialog_message.setText(R.string.dialog_logout_detail);

        Button yes = (Button) myDialog.findViewById(R.id.dialog_yes);
        yes.setText(R.string.dialog_yes);
        
        Button no = (Button) myDialog.findViewById(R.id.dialog_no);
        no.setText(R.string.dialog_no);
        
        yes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	myDialog.dismiss();
				// Call the Parse log out method
				ParseUser.logOut();

				Intent intent = new Intent(SettingsActivity.this,
						SignUpOrLogInActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
            }
        });
        
        no.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

}