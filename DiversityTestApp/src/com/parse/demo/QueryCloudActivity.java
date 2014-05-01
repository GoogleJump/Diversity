package com.parse.demo;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class QueryCloudActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.query_cloud);
		setTitle(R.string.app_name);

		final TextView string = (TextView) findViewById(R.id.testing);
		final Button queryButton = (Button) findViewById(R.id.queryCloudButton);

		queryButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ParseQuery<Puzzle> query = Puzzle.getQuery();

				// NOTE: Have to match value type EXACTLY
				query.whereEqualTo("points", 5);

				query.findInBackground(new FindCallback<Puzzle>() {
					public void done(List<Puzzle> scoreList, ParseException e) {
						if (e == null) {
							Puzzle puzzle = scoreList.get(0);
							string.setText("Puzzle: \nLevel: "
									+ Integer.toString(puzzle.getPoints())
									+ "\nLocation: "
									+ Double.toString(puzzle.getLocation()
											.getLatitude())
									+ ", "
									+ Double.toString(puzzle.getLocation()
											.getLongitude()));
						} else {
							string.setText((CharSequence) e);
						}
					}
				});
			}
		});

		// ParseQuery<Puzzle> query = new ParseQuery<Puzzle>("Puzzle");
		// // query.whereEqualTo("levelNumber", "1");
		// try {
		// List<Puzzle> results = query.find();
		// // string.setText(results.size());
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private void getQuery() {
		ParseQuery<Puzzle> query = Puzzle.getQuery();
		query.whereEqualTo("levelNumber", "1");

		try {
			List<Puzzle> results = query.find();
			// string.setText(results.size());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
