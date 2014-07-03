package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * InventoryActivity.java displays the inventory page view:
 * 		currently, the inventory view is defined by inventory.xml
 * 		The inventory page displays the current user's materials collected thus far.
 * 		if the main_menu_button Button is pressed, the view changes to the Main Menu view
 *		if the materials collected create an item, that item is added to the trophy shelf
 *      and then those materials disappear from the inventory	(TO DO)
 */

public class InventoryActivity extends Activity {
	
	
	private Button mainMenu;
//	private Button photoAlbum;
//	private Button trophyShelf;
	//private TextView currentMaterial = null;
	private ImageView currentMaterial = null;
	private TableRow currentRow = null;
	private int numMaterialsInRow = 3;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.inventory);
		setTitle(R.string.inventory_view_name);
		TableLayout tView = (TableLayout) findViewById(R.id.inventory_list);
		
		// trying something here
		Drawable bg = tView.getBackground();
		if (bg!=null) {
			if (bg instanceof BitmapDrawable) {
				BitmapDrawable bmp = (BitmapDrawable) bg;
				bmp.mutate();
				bmp.setTileModeXY(null,  TileMode.REPEAT);
			}
		}
		
		
		
		// get current user's list of collected characters to display
		User currentUser = null;
		if (User.getCurrentUser() instanceof User)
			currentUser = ((User) User.getCurrentUser());
		ArrayList<String> materialsCollected = null;
		if (currentUser != null) {
			materialsCollected = currentUser.getMaterialsCollected();
			
			// display collected items as strings
			for (int i = 0; i < materialsCollected.size(); i++){
				currentMaterial = new TextView(this);
				currentMaterial.setText(materialsCollected.get(i));
				lView.addView(currentMaterial);
				
			}
			
			addListenerOnMainMenuButton();
			addListenerOnPhotosButton();
			addListenerOnTrophiesButton();
		}
		else { // display login page
			Intent i = new Intent(this, SignUpOrLogInActivity.class);
			startActivity(i);
		}		
		
	}
		
	
	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_inventory);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				startActivity(i);
				
			}
		});
	}
}
