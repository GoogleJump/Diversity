package com.parse.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * PuzzleActivity displays the puzzle view: currently, the puzzle view is
 * defined by puzzle.xml at all times, at most 1 CheckBox in the view can be
 * checked if the CheckBox checked is the wrong answer, the rightorwrong
 * TextView displays a wrong message if the CheckBox checked is the right
 * answer, the view changes to the GPS view if the main_menu_button Button is
 * pressed, the view changes to the Main Menu view
 * 
 * when there is no more materials or items to find, displays an appropriate
 * message to the user saying so
 */
public class PuzzleActivity extends BaseActivity {

	private final int totalNumMultChoice = 4;

	private CheckBox chk1, chk2, chk3, chk4;
	private Button mainMenu;
	private String correctAnswer;
	private String material;
	private EditText anagramView;
	private Button mapButton;
	private Button shuffleButton;
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		User currentUser = (User) User.getCurrentUser();
		userInfo = currentUser.getUserInfo();

		// Where is this material being set?
		material = userInfo.getCurrentMaterial();

		// indicates there is nothing to find
		if (material.equals("")) {
			setContentView(R.layout.nothing_to_find);
		} else {
			final String puzzleID = userInfo.getPuzzleID();
			final String shuffledWord = userInfo.getShuffledWord();

			if (!puzzleID.equals("")) {
				this.riddleViewSet(puzzleID);
			} else if (!shuffledWord.equals("")) {
				this.anagramViewSet(shuffledWord, material);
			} else {
				setContentView(R.layout.nothing_to_find);
			}
		}

		addListenerOnMainMenuButton();
		addListenerOnShuffleButton();
		addListenerOnGPSButton();
	}

	/**
	 * Sets the view for the case of a riddle
	 * 
	 * @param puzzleID
	 *            String that is the id of the current puzzle; puzzleID is not
	 *            the empty string
	 */
	private void riddleViewSet(String puzzleID) {
		ParseQuery<Puzzle> query = Puzzle.getQuery();
		query.whereEqualTo("objectId", puzzleID);

		try {
			List<Puzzle> potentialPuzzles = query.find();

			setContentView(R.layout.puzzle);
			setTitle(R.string.puzzle_view_name);

			Puzzle puzzle = potentialPuzzles.get(0);

			TextView question = (TextView) findViewById(R.id.question);
			question.setText(puzzle.getString("riddle"));

			correctAnswer = puzzle.getString("material");

			chk1 = (CheckBox) findViewById(R.id.chkanswer_1);
			chk2 = (CheckBox) findViewById(R.id.chkanswer_2);
			chk3 = (CheckBox) findViewById(R.id.chkanswer_3);
			chk4 = (CheckBox) findViewById(R.id.chkanswer_4);

			ArrayList<String> options = puzzle.getOptions();
			options.add(correctAnswer);

			// randomly assigning CheckBoxes different answer options
			List<CheckBox> checkBoxes = new ArrayList<CheckBox>(Arrays.asList(
					chk1, chk2, chk3, chk4));
			Collections.shuffle(options);
			for (int i = 0; i < totalNumMultChoice; i++) {
				checkBoxes.get(i).setText(options.get(i));
			}

			// adding listeners specific to riddle view
			addListenerOnChkAnswer_1();
			addListenerOnChkAnswer_2();
			addListenerOnChkAnswer_3();
			addListenerOnChkAnswer_4();
			addListenerOnRiddleSubmitButton();

		} catch (ParseException e) {
			setContentView(R.layout.nothing_to_find);
		}
	}

	/**
	 * Sets the view for the case of an anagram
	 * 
	 * @param shuffledWord
	 *            String that is shuffled; shuffledWord is not the empty string
	 * @param material
	 *            String that is the shuffledWord unshuffled; material is not
	 *            the empty string
	 */
	private void anagramViewSet(String shuffledWord, String material) {
		setContentView(R.layout.anagram);
		setTitle(R.string.anagram_view_name);

		String scrambled = shuffledWord;

		TextView question = (TextView) findViewById(R.id.question);

		question.setText("Can you unscramble: " + scrambled + "?");

		// Set up the submit form.
		anagramView = (EditText) findViewById(R.id.anagramInput);
		correctAnswer = material;

		addListenerOnAnagramSubmitButton();

	}

	/**
	 * Gives a random new puzzle to the user when puzzle button is pressed
	 */
	private void addListenerOnShuffleButton() {
		shuffleButton = (Button) findViewById(R.id.new_puzzle_puz);
		shuffleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userInfo.getNewMaterialShuffleStyle();
				userInfo.saveEventually();

				Intent i = new Intent(PuzzleActivity.this, PuzzleActivity.class);
				PuzzleActivity.this.finish();
				startActivity(i);
			}

		});
	}

	/**
	 * Takes the user to GPS/map view when the map button is pressed
	 */
	private void addListenerOnGPSButton() {
		mapButton = (Button) findViewById(R.id.gps_button_puzzle);
		mapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PuzzleActivity.this, MapActivity.class);
				PuzzleActivity.this.finish();
				startActivity(i);
			}
		});
	}

	/**
	 * When the mainMenu Button is pressed, view changes to MainMenuView
	 */
	private void addListenerOnMainMenuButton() {
		mainMenu = (Button) findViewById(R.id.main_menu_button_puzzle);
		mainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(PuzzleActivity.this,
						MainMenuActivity.class);
				PuzzleActivity.this.finish();
				startActivity(i);
			}
		});
	}

	// THE SAME ACTIVITY OCCURS FOR ALL addListenerOnChkAnswer_[1-4]()
	// SEE checkBoxChecked FOR SPECIFIC ACTIVITY

	private void addListenerOnChkAnswer_1() {
		chk1 = (CheckBox) findViewById(R.id.chkanswer_1);
		chk1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkOneBox(chk1, v);
			}
		});
	}

	private void addListenerOnChkAnswer_2() {
		chk2 = (CheckBox) findViewById(R.id.chkanswer_2);
		chk2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkOneBox(chk2, v);
			}
		});
	}

	private void addListenerOnChkAnswer_3() {
		chk3 = (CheckBox) findViewById(R.id.chkanswer_3);
		chk3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkOneBox(chk3, v);
			}
		});
	}

	private void addListenerOnChkAnswer_4() {
		chk4 = (CheckBox) findViewById(R.id.chkanswer_4);
		chk4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkOneBox(chk4, v);
			}
		});
	}

	/**
	 * Called when a CheckBox is clicked on.
	 * 
	 * If CheckBox CB is checked, removes checks from all other CheckBoxes in
	 * View that are not CB
	 * 
	 * @param CB
	 *            CheckBox that the user clicks on
	 * @param v
	 *            the View that CB belongs in, which is altered by this method
	 */
	private void checkOneBox(CheckBox CB, View v) {
		if (((CheckBox) v).isChecked()) {
			List<CheckBox> checkBoxes = new ArrayList<CheckBox>(Arrays.asList(
					chk1, chk2, chk3, chk4));

			// unchecking CheckBoxes that are not CB
			for (CheckBox chk : checkBoxes) {
				if (!CB.equals(chk)) {
					chk.setChecked(false);
				}
			}
		}
	}

	/**
	 * Called when submit button is pressed for riddle case checks if the check
	 * box checked is the correct answer and shows the correct answer popup if
	 * correct and the wrong answer popup if wrong
	 */
	private void addListenerOnRiddleSubmitButton() {
		Button riddleSubmit = (Button) findViewById(R.id.submit_button_puzzle);
		riddleSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<CheckBox> checkBoxes = new ArrayList<CheckBox>(Arrays
						.asList(chk1, chk2, chk3, chk4));

				// boolean that indicates whether or not to show the correct
				// popup
				boolean showCorrect = false;

				for (CheckBox chk : checkBoxes) {
					if (chk.isChecked()) {
						if (chk.getText().equals(correctAnswer)) {
							showCorrect = true;
						}
					}
				}
				if (showCorrect) {
					showCorrectDialog();
				} else {
					showIncorrectDialog();
				}
			}
		});
	}

	/**
	 * Called when submit button is pressed for anagram case checks if the check
	 * box checked is the correct answer and shows the correct answer popup if
	 * correct and the wrong answer popup if wrong
	 */

	private void addListenerOnAnagramSubmitButton() {
		Button anagramSubmit = (Button) findViewById(R.id.anagramSubmit);
		anagramSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// indicates the answer is correct
				if (anagramView.getText().toString().equals(correctAnswer)) {
					showCorrectDialog();
				} else {
					showIncorrectDialog();
				}
			}
		});
	}

	/**
	 * Displays the correct dialog, which takes user to the MapActivity
	 */
	private void showCorrectDialog() {
		String currentMaterial = userInfo.getCurrentMaterial();
		userInfo.addMaterialSolved(currentMaterial);
		userInfo.setCurrentMaterial("");
		userInfo.getNewMaterialShuffleStyle();

		userInfo.saveEventually();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Congrats!");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"You correctly solved the puzzle for " + material + "!")
				.setCancelable(false)
				.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								PuzzleActivity.this.finish();
								startActivity(new Intent(PuzzleActivity.this,
										MapActivity.class));
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Displays the incorrect Dialog, which leaves user on same page
	 */
	private void showIncorrectDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Try again");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"Sorry, you did not solve the puzzle correctly. Try again.")
				.setCancelable(false)
				.setPositiveButton("Okay",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
}