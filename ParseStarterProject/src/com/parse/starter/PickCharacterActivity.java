package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseObject;

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
import android.widget.ProgressBar;

/**
 * PickCharacterActivity.java lets you pick a character by showing their
 * pictures and showing popups with character descriptions. If you press the
 * Main Menu Button, the activity changes to MainMenuActivity.java.
 * PickCharacterActivity.java uses the layouts viewpager.xml, surfer.xml, and
 * grandma.xml in order to provide swiping functionality.
 */

public class PickCharacterActivity extends BaseActivity {

	private ProgressBar progressBar;
	private CharacterPagerAdapter pagerAdapter;
	private Button mainMenu;
	private Context context;
	private UserInfo userInfo;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		context = this;

		progressBar = (ProgressBar) findViewById(R.id.progressBarPickCharacter);
		progressBar.setVisibility(View.GONE);

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
				characterPic = (ImageButton) v
						.findViewById(R.id.surfer_picture);
				characterPic.setOnClickListener(new OnClickListener() {
					public void onClick(View m) {

						// new popup
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						alertDialogBuilder
								.setMessage(
										"Hey dude, I am Sunny de Souza. I am from the big island, surfing the waves since I was "
												+ "a tiny dude. I rode this crazy wave that scatterd all the beachgoers' belongings all over the place. "
												+ "Help the dudes and dudets out.")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												new SaveCharacterTask()
														.execute("Surfer");
												progressBar.setVisibility(View.VISIBLE);
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
				});
				break;

			case 1: // grandma
				resId = R.layout.grandma;
				v = inflater.inflate(resId, null, false);
				characterPic = (ImageButton) v
						.findViewById(R.id.grandma_picture);
				characterPic.setOnClickListener(new OnClickListener() {
					public void onClick(View m) {
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						alertDialogBuilder
								.setMessage(
										"Oh hello, deary. Are you hungry? I would bake you some fresh cookies, "
												+ "but I think my cats hide all the groceries that I just got from the store. Oh dear, where "
												+ "did I put the sweater I knit you? Can you help me out, deary?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												new SaveCharacterTask()
														.execute("Grandma");
												progressBar.setVisibility(View.VISIBLE);
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}
				});
				break;
			}
			((ViewPager) collection).addView(v, 0);
			return v;
		}

		private class SaveCharacterTask extends
				AsyncTask<String, Void, Void> {
			@Override
			protected Void doInBackground(String... params) {
				String characterSelected = params[0];
				userInfo.setCurrentCharacter(characterSelected);
				userInfo.getNewItem();
				List<UserInfo> userInfoList = new ArrayList<UserInfo>();
				userInfoList.add(userInfo);
				ParseObject.pinAllInBackground("UserInfo", userInfoList);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Intent i = new Intent(PickCharacterActivity.this,
						MapActivity.class);
				PickCharacterActivity.this.finish();
				startActivity(i);
			}
		}

	}
}
