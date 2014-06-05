package com.parse.demo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * This test queries the cloud and returns a unique object in the cloud. 
 *
 */
public class QueryCloudActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.query_cloud);
		setTitle(R.string.app_name);

		final TextView string = (TextView) findViewById(R.id.queryText);
		final Button queryButton = (Button) findViewById(R.id.queryCloudButton);
		final Button backButton = (Button) findViewById(R.id.backToMainFromQuery);

		queryButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ParseQuery<Puzzle> query = Puzzle.getQuery();
				
				// NOTE: Have to match value type EXACTLY
				query.whereEqualTo("points", 3);
				
				//string.setText(query.toString());
				query.findInBackground(new FindCallback<Puzzle>() {
					public void done(List<Puzzle> scoreList, ParseException e) {
						if (e == null && scoreList.size() > 0) {
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
							if (e == null) {
								string.setText("We queried nothing.");
							} else {
								string.setText((CharSequence) e);
							}
							
						}
					}
				});
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent j = new Intent(QueryCloudActivity.this, ToDoListActivity.class);
            	startActivity(j);
			}
		});

		
	}

}
