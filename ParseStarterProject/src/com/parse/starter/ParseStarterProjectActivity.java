package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ParseStarterProjectActivity extends Activity {

	private static final int ACTIVITY_CREATE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent i = new Intent(this, PersistToCloudActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
}
