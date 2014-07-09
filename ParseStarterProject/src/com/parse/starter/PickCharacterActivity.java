package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.Button;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.parse.SaveCallback;
import com.parse.ParseException;

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
	private UserInfo currentUser;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		context = this;

		// handling user
		currentUser = null;
		if (User.getCurrentUser() instanceof User) {
			currentUser = ((User) User.getCurrentUser()).getUserInfo();
		}
		if (currentUser == null) {
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}

		pagerAdapter = new CharacterPagerAdapter();
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(pagerAdapter);

		// adding listeners to buttons
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
				startActivity(i);

			}
		});
	}

	// Private class that handles the swiping functionality (different "views")
	private class CharacterPagerAdapter extends PagerAdapter {

		private View v;
		private ImageButton characterPic;

		// number of characters
		public int getCount() {
			return 2;
		}

		public boolean isViewFromObject(View view, Object object) {
			return view == object;
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

						// new popup
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

						alertDialogBuilder
							.setMessage("This is the surfer. He is super cool!")
							// character description
							.setCancelable(false)
							.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // set character to surfer
																							// and change activity
								public void onClick(DialogInterface dialog, int id) {
									currentUser.setCurrentCharacter("Surfer");
									currentUser.saveInBackground(new SaveCallback() {
										public void done(ParseException e) {
											if (e != null) {
												String currentCharacter = currentUser.getCurrentCharacter();
												Log.d("myApp","currentCharacter is: "+ currentCharacter);
												Intent i = new Intent(PickCharacterActivity.this, MapActivity.class);
												PickCharacterActivity.this.finish();
												startActivity(i);
											}
										}
									});
								}
							})
							.setNegativeButton("No",new DialogInterface.OnClickListener() { // go back to the activity
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked,
									// just close the dialog box and do nothing
									dialog.cancel();
								}
							});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}
				});
				break;
				
			case 1: // grandma
				resId = R.layout.grandma;
				v = inflater.inflate(resId, null, false);
				characterPic = (ImageButton) v.findViewById(R.id.grandma_picture);
				characterPic.setOnClickListener(new OnClickListener() {
					public void onClick(View m) {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

						alertDialogBuilder
							.setMessage("This is the grandma. She's a cool lady.")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// currentUser.setCurrentCharacter("Grandma");
									String currentCharacter = currentUser.getCurrentCharacter();
									Log.d("myApp","currentCharacter is: " + currentCharacter);
									Intent i = new Intent(PickCharacterActivity.this, MapActivity.class);
									PickCharacterActivity.this.finish();
									startActivity(i);
								}
							})
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									// if this button is clicked,
									// just close the dialog box and do nothing
									dialog.cancel();
								}
							});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();
					}
				});
				break;
			}
			((ViewPager) collection).addView(v, 0);
			return v;
		}

	}
}
