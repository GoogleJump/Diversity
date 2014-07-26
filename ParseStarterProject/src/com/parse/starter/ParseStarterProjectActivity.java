package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

public class ParseStarterProjectActivity extends BaseActivity {

	public ParseStarterProjectActivity() {
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (ParseUser.getCurrentUser() != null) {
			ParseStarterProjectActivity.this.finish();
			startActivity(new Intent(ParseStarterProjectActivity.this,
					MainMenuActivity.class));
		} else {
			ParseStarterProjectActivity.this.finish();
			startActivity(new Intent(ParseStarterProjectActivity.this,
					SignUpOrLogInActivity.class));
		}

	}
}