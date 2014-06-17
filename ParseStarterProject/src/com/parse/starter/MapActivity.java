package com.parse.starter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class MapActivity extends BaseActivity implements LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	private User user;
	private LocationManager locationManager;
	private String provider;

	private GoogleMap map;
	private LocationClient locationClient;

	private HashMap<String, ArrayList<String>> materialLocations = new HashMap<String, ArrayList<String>>();

	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000).setFastestInterval(16) // 16ms
																														// =
																														// 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		// If map wants to be centered

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		map.setMyLocationEnabled(true);

		locationClient = new LocationClient(this, this, this);

		LatLng myLocation = new LatLng(37.4220, -122.0804);

		if (locationClient != null && locationClient.isConnected()) {
			Location currentLocation = locationClient.getLastLocation();
			myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		}

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));

		addTransitionListeners();
		addMapActionListeners();

		user = ((User) User.getCurrentUser());
	}

	@SuppressLint("NewApi")
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (map != null) {
				map.setMyLocationEnabled(true);
			}
		}
	}

	private void setUpLocationClientIfNeeded() {
		if (locationClient == null) {
			locationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
	}

	public void showMyLocation(View view) {
		if (locationClient != null && locationClient.isConnected()) {
			Log.d("Map Activity", locationClient.getLastLocation().toString());
			System.out.println(locationClient.getLastLocation());
		}
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
				locateMaterials();
			}
		});

		findViewById(R.id.claim_items_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMyLocation(v);
			}
		});

		findViewById(R.id.locate_items_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locateMaterials();
			}
		});
	}

	private void locateMaterials() {
		// get current location
		if (locationClient != null && locationClient.isConnected()) {
			Location location = locationClient.getLastLocation();

		}

		// pick five or less materials and their search terms
		populateMaterialLocations();
		
		// find nearby places
	}

	/**
	 * Put in the five or less materials in the hashmap
	 */
	private void populateMaterialLocations() {
		ArrayList<String> materialsCollected = user.getMaterialsCollected();
		Collections.shuffle(materialsCollected);
		int listLength = Math.min(5, materialsCollected.size());
		ArrayList<String> materials = new ArrayList<String>(materialsCollected.subList(0, listLength));
		ParseQuery<Material> query = ParseQuery.getQuery(Material.class);
		query.whereContainedIn("name", materials);

		materialLocations = new HashMap<String, ArrayList<String>>();

		query.findInBackground(new FindCallback<Material>() {
			public void done(List<Material> resultsList, ParseException e) {
				if (e == null) {
					for (Material m : resultsList) {
						materialLocations.put(m.getName(), m.getSearchTerms());
					}
				} else {
					Log.d("Map Activity", e.toString());
				}
			}
		});
	}

	private void placeMaterials(List<Material> materials) {
		for (Material m : materials) {
			ArrayList<String> searchTerms = m.getSearchTerms();
			// for (String str : searchTerms) {
			//
			// }
		}

	}

	private void claimItem() {
		Log.d("Map Activity", "before locationManager in onClaimItems");
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.d("Map Activity", "in claimItem method");
				// Called when a new location is found by the network location
				// provider.
				onLocationChanged(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		locationClient.connect();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		locationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (locationClient != null) {
			locationClient.disconnect();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
		System.out.printf("lat: %d, long: %d", lat, lng);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

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
