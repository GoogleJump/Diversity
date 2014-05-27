package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

public class ParseStarterProjectActivity extends Activity {

//	private static final int ACTIVITY_CREATE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

/*		Intent i = new Intent(this, PersistToCloudActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
		*/
		
//		ParseUser currentUser = ParseUser.getCurrentUser();
//		System.out.println(currentUser.toString());
//		ParseUser.logOut();
//		currentUser = ParseUser.getCurrentUser();
//		System.out.println(currentUser.toString());


	    if (ParseUser.getCurrentUser() == null) {
	        // Start an intent for the logged in activity
	        startActivity(new Intent(this, SignUpOrLogInActivity.class));    //FIX THIS LATER
	      } 
	    else {
	        // Start and intent for the logged out activity
	        startActivity(new Intent(this, PersistToCloudActivity.class));
	      }
	}
}
