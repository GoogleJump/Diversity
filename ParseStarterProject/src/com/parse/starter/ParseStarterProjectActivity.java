package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import com.parse.ParseUser;
import com.team.diversity.android.R;

public class ParseStarterProjectActivity extends BaseActivity {

	public ParseStarterProjectActivity() {
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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