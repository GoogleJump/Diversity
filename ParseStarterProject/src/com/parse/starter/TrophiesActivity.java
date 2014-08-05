package com.parse.starter;

import java.util.List;
import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
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
 * TrophiesActivity.java displays the trophies page view: currently, the
 * trophies view is defined by trophies.xml The trophies page displays the
 * current user's items collected thus far if the main_menu_button Button is
 * pressed, the view changes to the Main Menu view
 */

public class TrophiesActivity extends BaseActivity {

	private Button mainMenu;
	private ImageView currentItem = null;
	private TableRow currentRow = null;
	// changed to 1 because the view can really only accommodate 1 item due to
	// size of the picture
	private int numItemsInRow = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.trophies);
		setTitle(R.string.trophies_view_name);
		TableLayout tView = (TableLayout) findViewById(R.id.trophies_list);

		// a hack to avoid distortions in the background image
		Drawable bg = tView.getBackground();
		if (bg != null) {
			if (bg instanceof BitmapDrawable) {
				BitmapDrawable bmp = (BitmapDrawable) bg;
				bmp.mutate();
				bmp.setTileModeXY(TileMode.MIRROR, TileMode.REPEAT);
			}
		}

		// get current user's list of collected characters to display
		UserInfo userInfo = ((User) User.getCurrentUser()).getUserInfo();
		List<String> itemsCollected = userInfo.getItemsCollected();
		Log.d("trophy activity: ", itemsCollected.toString());


		// display collected items 
		if (itemsCollected != null) {

			int numItems = itemsCollected.size();
			int numColumns = numItems / numItemsInRow + 1;
			int itemsPlaced = 0; // counter for filling in items

			for (int i = 0; i < numColumns; i++) { // for each row
				currentRow = new TableRow(this);
				for (int j = 0; j < numItemsInRow; j++) { 
					if (itemsPlaced < numItems) {
						currentItem = new ImageView(this);
						int id = this.getResources().getIdentifier(
								itemsCollected.get(itemsPlaced).replace(" ","_"), "drawable",
								getPackageName());
						currentItem.setImageResource(id);
						currentRow.addView(currentItem);
						itemsPlaced++;
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
		mainMenu = (Button) findViewById(R.id.main_menu_button_trophies);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainMenuActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

			}
		});
	}
}
