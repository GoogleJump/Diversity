package com.parse.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ToDoListActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final Button persistButton = (Button) findViewById(R.id.persistButton);
		persistButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ToDoListActivity.this,
						PersistToCloudActivity.class);
				startActivity(i);
			}
		});

		final Button queryButton = (Button) findViewById(R.id.queryButton);
		queryButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent j = new Intent(ToDoListActivity.this,
						QueryCloudActivity.class);
				startActivity(j);
			}
		});

	}
}