package com.parse.starter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * PickCharacterActivity.java lets you pick a character by showing their
 * pictures and showing popups with character descriptions. If you press the
 * Main Menu Button, the activity changes to MainMenuActivity.java.
 * PickCharacterActivity.java uses the layouts viewpager.xml, surfer.xml, and
 * grandma.xml in order to provide swiping functionality.
 */

public class PickCharacterActivity extends Activity {

	private CharacterPagerAdapter pagerAdapter;
	private Button mainMenu;
	private Context context;
	private UserInfo userInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		context = this;

		userInfo = null;
		if (User.getCurrentUser() instanceof User) {
			userInfo = ((User) User.getCurrentUser()).getUserInfo();
		}
		if (userInfo == null) {
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}

		pagerAdapter = new CharacterPagerAdapter();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);

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
				PickCharacterActivity.this.finish();
				startActivity(i);
			}
		});
	}

	// Private class that handles the swiping functionality (different "views")
	private class CharacterPagerAdapter extends PagerAdapter {

		private View v;
		private ImageButton characterPic;

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		public void makePopup(final String name, String description){
			// new popup
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			alertDialogBuilder
				.setMessage(description)
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // set character to surfer
																				// and change activity
					public void onClick(DialogInterface dialog, int id) {
						new SaveCharacterTask().execute(name);
					}
				})
				.setNegativeButton("No",new DialogInterface.OnClickListener() { // go back to the activity
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

		public Object instantiateItem(final View collection, final int position) {
			v = new View(collection.getContext());
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int resId = 0;
			switch (position) {
			case 0: // surfer
				resId = R.layout.surfer;
				v = inflater.inflate(resId, null, false);
				characterPic = (ImageButton) v.findViewById(R.id.surfer_picture); // find the picture button that
																					// triggers the popup
				characterPic.setOnClickListener(new OnClickListener() {
					public void onClick(View m) {					
						makePopup("Surfer","This is the surfer. He is super cool!");
					}
				});
				break;
				
			case 1: // grandma
				resId = R.layout.grandma;
				v = inflater.inflate(resId, null, false);
				characterPic = (ImageButton) v.findViewById(R.id.grandma_picture);
				characterPic.setOnClickListener(new OnClickListener() {
					public void onClick(View m) {
						makePopup("Grandma","This is the grandma. She's a cool lady.");
					}
				});
				break;
			}
			((ViewPager) collection).addView(v, 0);
			return v;
		}
		
		
		
		private class SaveCharacterTask extends AsyncTask<String, Void, Void> {
			@Override
			protected Void doInBackground(String... params) {
				String characterSelected = params[0];
				userInfo.getNewItem();
				userInfo.setCurrentCharacter(characterSelected);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				Intent i = new Intent(PickCharacterActivity.this, MapActivity.class);
				PickCharacterActivity.this.finish();
				startActivity(i);
			}
		}

	}
}
