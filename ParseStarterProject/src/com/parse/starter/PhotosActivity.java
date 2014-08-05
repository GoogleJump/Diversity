package com.parse.starter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.team.diversity.android.R;

/**
 * Adapted from PageCurler's code: com.mystictreegames.pagecurl
 * Used for the graphics
 **/

public class PhotosActivity extends BaseActivity {

	private Button mainMenu; // Brings user back from photo album to the main menu
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
				
				// recycle all the bitmaps used and free memory used in photo album
				View photoAlbum = findViewById(R.id.dcgpagecurlPageCurlView1);
				((PageCurlView) photoAlbum).recycleBitmaps();
				ViewGroup vg = (ViewGroup)(photoAlbum.getParent());
				vg.removeView(photoAlbum);
				
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				PhotosActivity.this.finish();
				startActivity(i);

			}
		});
	}
}
