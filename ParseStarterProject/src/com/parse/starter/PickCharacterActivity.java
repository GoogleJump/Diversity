package com.parse.starter;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

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
		
		private void showCharacterDescriptionDialog(final String name, String message) {

	        final Dialog myDialog = new Dialog(context);     
	        myDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
	        myDialog.setContentView(R.layout.two_button_dialog);
	        myDialog.setCancelable(false);

	        TextView dialog_title = (TextView) myDialog.findViewById(R.id.title);
	        dialog_title.setText(name + "'s Story");

	        TextView dialog_message = (TextView) myDialog.findViewById(R.id.message);
	        dialog_message.setText(message);

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
						showCharacterDescriptionDialog("Surfer","Hey dude, I am Sunny de Souza. I am from the big island, surfing the waves since I was a tiny dude. I rode this crazy wave that scatterd all the beachgoers' belongings all over the place. Help the dudes and dudets out.");
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
						showCharacterDescriptionDialog("Grandma","Oh hello, deary. Are you hungry? I would bake you some fresh cookies, but I think my cats hide all the groceries that I just got from the store. Oh dear, where did I put the sweater I knit you? Can you help me out, deary?");
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
