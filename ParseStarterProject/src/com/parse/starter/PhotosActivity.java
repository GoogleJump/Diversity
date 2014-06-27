package com.parse.starter;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Adapted from PageCurler's code: com.mystictreegames.pagecurl
 * Used for the graphics
 **/

public class PhotosActivity extends Activity {

	private Button mainMenu; // Brings user back from photo album to the main menu
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setContentView(R.layout.photos);
    	addListenerOnMainMenuButton();
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	System.gc();
    	finish();
    }
    
    /**
	 * Set the current orientation to landscape. This will prevent the OS from changing
	 * the app's orientation.
	 */
	public void lockOrientationLandscape() {
		lockOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * Set the current orientation to portrait. This will prevent the OS from changing
	 * the app's orientation.
	 */
	public void lockOrientationPortrait() {
		lockOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 * Locks the orientation to a specific type.  Possible values are:
	 * <ul>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_BEHIND}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_NOSENSOR}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_SENSOR}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}</li>
	 * <li>{@link ActivityInfo#SCREEN_ORIENTATION_USER}</li>
	 * </ul>
	 * @param orientation
	 */
	public void lockOrientation( int orientation ) {
		setRequestedOrientation(orientation);
	}
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_photos);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);

			}
		});
	}
}



//package com.parse.starter;
//
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
///**
// * TrophiesActivity.java displays the trophies page view: currently, the
// * trophies view is defined by trophies.xml The trophies page displays the
// * current user's items collected thus far if the main_menu_button Button is
// * pressed, the view changes to the Main Menu view
// */

//public class PhotosActivity extends Activity {

//	private Button mainMenu;
////	private Button trophyShelf;
////	private Button inventory;
//	private TextView currentCharacter = null;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.photos);
//		setTitle(R.string.photos_view_name);
//		LinearLayout lView = (LinearLayout) findViewById(R.id.photos_list);
//
//		// get current user's list of collected characters to display
//		User currentUser = null;
//		if (User.getCurrentUser() instanceof User)
//			currentUser = ((User) User.getCurrentUser());
//		ArrayList<String> charactersCollected = null;
//		if (currentUser != null) {
//			charactersCollected = currentUser.getCharactersCollected();
//		} else { // display login page
//			Intent i = new Intent(this, SignUpOrLogInActivity.class);
//			startActivity(i);
//		}
//
//		// display collected items as strings
//		if (charactersCollected != null) {
//			for (int i = 0; i < charactersCollected.size(); i++) {
//				currentCharacter = new TextView(this);
//				currentCharacter.setText(charactersCollected.get(i));
//				lView.addView(currentCharacter);
//			}
//		}
//
//		addListenerOnMainMenuButton();
////		addListenerOnTrophiesButton();
////		addListenerOnInventoryButton();
//	}
//
//	/**
//	 * When the mainMenu Button is pressed, view changes to MainMenuView
//	 */
//	private void addListenerOnMainMenuButton() {
//		mainMenu = (Button) findViewById(R.id.main_menu_button_photos);
//		mainMenu.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
//				startActivity(i);
//
//			}
//		});
//	}
//
////	/**
////	 * When the Trophies Button is pressed, view changes to Trophy Shelf
////	 */
////	private void addListenerOnTrophiesButton() {
////		trophyShelf = (Button) findViewById(R.id.trophies_button_photos);
////		trophyShelf.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				Intent i = new Intent(v.getContext(), TrophiesActivity.class);
////				startActivity(i);
////
////			}
////		});
////	}
////	
////	/**
////	 * When the Inventory Button is pressed,
////	 * 		changes to Inventory view, where all materials collected by the current user is displayed 
////	 */
////	private void addListenerOnInventoryButton() {
////		inventory = (Button) findViewById(R.id.inventory_button_photos);
////		inventory.setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				Intent i = new Intent(v.getContext(), InventoryActivity.class);
////				startActivity(i);
////			}
////		});
////	}
//}
