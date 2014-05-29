package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;


/**
 * TrophiesActivity.java displays the trophies page view:
 * 		currently, the trophies view is defined by trophies.xml
 * 		The trophies page displays the current user's items collected thus far
 * 		if the main_menu_button Button is pressed, the view changes to the Main Menu view
 */

public class TrophiesActivity extends Activity {
	
	
	private Button mainMenu;
	private TextView currentItem = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.trophies);
		setTitle(R.string.trophies_view_name);
		LinearLayout lView = (LinearLayout) findViewById(R.id.trophies_list);
		
		Person user = new Person();
		user.put("points", 0);
		user.put("levelAt", 1);
		user.put("stateAt", 0); // 0 = puzzle unsolved, 1 = puzzle solved, not gps, 2 = at location
		user.put("puzzleID", 101);
		user.put("currentItem", "coffee");
		user.put("currentIngredient", "water");
		ArrayList<String> items = new ArrayList<String>(Arrays.asList("one", "two", "three", "four"));
		user.put("itemsCollected", items);
		user.saveInBackground();
		ArrayList<String> itemsCollected = user.getCollectedItems();
		
		// display collected items as strings
		for (int i = 0; i < itemsCollected.size(); i++){
			currentItem = new TextView(this);
			currentItem.setText(itemsCollected.get(i));
			lView.addView(currentItem);
			
		}
		
		addListenerOnMainMenuButton();
	}
		
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_trophies);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
}
