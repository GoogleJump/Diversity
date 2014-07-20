package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;

/**
 * This class is an example of how to persist data to cloud
 * and link a view to an activity. For testing purposes only. 
 *
 */
public class PersistToCloudActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set view to the persist_to_cloud.xml
		setContentView(R.layout.persist_to_cloud);
		setTitle(R.string.app_name);
		
		Material material = new Material();
		material.put("name", "testing");
		material.saveEventually();
	}
}
