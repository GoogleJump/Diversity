package com.parse.starter;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class MapActivity extends Activity implements LocationListener {
	private User user;
	private LocationManager locationManager;
	private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// set content view AFTER ABOVE sequence (to avoid crash)
		setContentView(R.layout.map);

		addTransitionListeners();
		addMapActionListeners();

		user = ((User) User.getCurrentUser());
	}

	private void addTransitionListeners() {
		findViewById(R.id.main_menu_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MapActivity.this, MainMenuActivity.class));
			}
		});

		findViewById(R.id.puzzle_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MapActivity.this, PuzzleActivity.class));
			}
		});
	}

	private void addMapActionListeners() {
		findViewById(R.id.randomize_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locateItems();
			}
		});

		findViewById(R.id.claim_items_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				claimItem();
			}
		});

		findViewById(R.id.locate_items_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locateItems();
			}
		});
	}

	private void locateItems() {
		ArrayList<String> itemsCollected = user.getItemsCollected();
		Collections.shuffle(itemsCollected);
		// pick the first 5 items in the shuffled list

	}

	private void claimItem() {
		// check location
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);


		// Initialize the location fields
		if (location != null) {
			onLocationChanged(location);
		} else {

			// do something
			System.out.println("Do something");
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
		System.out.printf("lat: %d, long: %d", lat, lng);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
