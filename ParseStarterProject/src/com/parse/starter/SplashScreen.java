package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Splash Screen that is opened when the app is first turned on
 */
public class SplashScreen extends BaseActivity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title);

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer.
			 */
			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreen.this,
						ParseStarterProjectActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
