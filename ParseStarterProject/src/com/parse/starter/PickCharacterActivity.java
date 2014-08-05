package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.team.diversity.android.R;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.MapActivity.MATERIAL_ITEM;

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
	private boolean firstTime;
	private List<String> charactersNotCollected;
	private List<String> charactersCollected;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
		
		charactersCollected = userInfo.getCharactersCollected();
		charactersNotCollected = new ArrayList<String>();
		
		ParseQuery<Character> query = ParseQuery.getQuery("Character");
		query.whereNotContainedIn("name", charactersCollected);
		try {
			List<Character> resultsList = query.find();
			for (Character character: resultsList){
				charactersNotCollected.add(character.getName());
			}
		} catch (ParseException e) {
			e.printStackTrace();
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
			return charactersNotCollected.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		private void showCharacterDescriptionDialog(final String name) {

			final Dialog myDialog = new Dialog(context);
			myDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
			myDialog.setContentView(R.layout.two_button_dialog);
			myDialog.setCancelable(false);

			TextView dialog_title = (TextView) myDialog
					.findViewById(R.id.title);
			dialog_title.setText(name + "'s Story");

			TextView dialog_message = (TextView) myDialog
					.findViewById(R.id.message);
			dialog_message.setText(context.getResources().getIdentifier("character_dialog_" + name.toLowerCase(), "string",getPackageName()));

			Button yes = (Button) myDialog.findViewById(R.id.dialog_yes);
			yes.setText("Yes");

			Button no = (Button) myDialog.findViewById(R.id.dialog_no);
			no.setText("No");

			yes.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					myDialog.dismiss();
					new SaveCharacterTask().execute(name);
					progressBar.setVisibility(View.VISIBLE);
				}
			});

			no.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					myDialog.dismiss();
				}
			});

			myDialog.show();

		}

		@Override
		public Object instantiateItem(final View collection, final int position) {
			v = new View(collection.getContext());
			LayoutInflater inflater = (LayoutInflater) collection.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int resId = 0;
			
			for (int i = 0; i < charactersNotCollected.size(); i++){
				if (position == i){

					final String theCharacter = charactersNotCollected.get(i);

					resId = R.layout.character;
					
					v = inflater.inflate(resId, null, false);
					characterPic = (ImageButton) v
							.findViewById(R.id.character_picture);
					
					TextView characterName = (TextView) v.findViewById(R.id.character_name);
					characterName.setText(theCharacter);
					
					characterPic.setImageResource(context.getResources().getIdentifier(theCharacter.toLowerCase(), "drawable",getPackageName()));
					
					characterPic.setOnClickListener(new OnClickListener() {
						public void onClick(View m) {
							showCharacterDescriptionDialog(theCharacter);
						}
					});
				}
			}

			((ViewPager) collection).addView(v, 0);
			return v;
		}

		private class SaveCharacterTask extends AsyncTask<String, Void, Void> {
			@Override
			protected Void doInBackground(String... params) {
				String characterSelected = params[0];
				userInfo.setCurrentCharacter(characterSelected);
				userInfo.getNewItem();
				List<String> charactersCollected = userInfo.getCharactersCollected();
				if (charactersCollected.isEmpty()) {
					firstTime = true;
				}
				else {
					firstTime = false;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				Intent i;
				if (firstTime){
					i = new Intent(PickCharacterActivity.this,
							HelpActivity.class);
				}
				else {
					i = new Intent(PickCharacterActivity.this,
							MapActivity.class);
				}
				PickCharacterActivity.this.finish();
				startActivity(i);
			}
		}

	}
}
