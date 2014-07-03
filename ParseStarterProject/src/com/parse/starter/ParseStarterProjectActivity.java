package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.parse.ParseUser;

public class ParseStarterProjectActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.title);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		if (ParseUser.getCurrentUser() != null) {
			startActivity(new Intent(ParseStarterProjectActivity.this,
					MainMenuActivity.class));
		} else {
			startActivity(new Intent(ParseStarterProjectActivity.this,
					SignUpOrLogInActivity.class));
		}
        return true;
    }

	
}