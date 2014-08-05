package com.parse.starter;

import java.util.List;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.team.diversity.android.R;

/**
 * InventoryActivity.java displays the inventory page view: currently, the
 * inventory view is defined by inventory.xml The inventory page displays the
 * current user's materials collected thus far. if the main_menu_button Button
 * is pressed, the view changes to the Main Menu view if the materials collected
 * create an item, that item is added to the trophy shelf and then those
 * materials disappear from the inventory (TO DO)
 */

public class InventoryActivity extends BaseActivity {

	private Button mainMenu;
	private ImageView currentMaterial = null;
	private TableRow currentRow = null;
	// changing to 1 so that all the materials can be seen
	private int numMaterialsInRow = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.inventory);
		setTitle(R.string.inventory_view_name);
		TableLayout tView = (TableLayout) findViewById(R.id.inventory_list);

		// a hack to extend the picture out so it doesn't look warpy
		Drawable bg = tView.getBackground();
		/*if (bg != null) {
			if (bg instanceof BitmapDrawable) {
				BitmapDrawable bmp = (BitmapDrawable) bg;
				bmp.mutate();
				bmp.setTileModeXY(null, TileMode.REPEAT);
			}
		} 
		*/

		// get current user's list of collected characters to display
		UserInfo userInfo = ((User) User.getCurrentUser()).getUserInfo();
		List<String> materialsCollected = userInfo.getMaterialsCollected();
		Log.d("inventory activity: ", materialsCollected.toString());

		if (materialsCollected != null) {
			int numMaterials = materialsCollected.size();
			int numColumns = numMaterials / numMaterialsInRow + 1;
			int materialsPlaced = 0; // counter for filling in items

			for (int i = 0; i < numColumns; i++) { // for each row
				currentRow = new TableRow(this);
				for (int j = 0; j < numMaterialsInRow; j++) { // 3 in each row
					if (materialsPlaced < numMaterials) {
						currentMaterial = new ImageView(this);
						int id = this.getResources().getIdentifier(
								materialsCollected.get(materialsPlaced),
								"drawable", getPackageName());
						currentMaterial.setImageResource(id);
						currentRow.addView(currentMaterial);
						materialsPlaced++;
					}
				}
				tView.addView(currentRow);
			}
		}
		addListenerOnMainMenuButton();
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