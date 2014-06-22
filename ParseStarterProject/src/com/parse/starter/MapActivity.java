package com.parse.starter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MapActivity extends BaseActivity implements LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	private final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	private final String GOOGLE_PLACES_API_KEY = "AIzaSyDKsYGo4Nk-aGBHl3JaOzorYp85TP9h6j4";
	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000).setFastestInterval(16)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	private Location location;
	private final int RADIUS = 20000; // in meters

	private Gson gson;

	private User user;
	private GoogleMap map;
	private LocationClient locationClient;
	
	private HashMap<GooglePlace, Material> placeToMaterial = new HashMap<GooglePlace, Material>();
	private List<GooglePlace> places = new ArrayList<GooglePlace>();
	private HashSet<Material> materialSearchTerms = new HashSet<Material>();
	private HashMap<GooglePlace, Marker> markers = new HashMap<GooglePlace, Marker>();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		map.setMyLocationEnabled(true);

		locationClient = new LocationClient(this, this, this);

		// Default because current location cannot be determined at start time
		LatLng myLocation = new LatLng(37.4220, -122.0804);

		if (locationClient != null && locationClient.isConnected()) {
			System.out.println("here");
			Location currentLocation = locationClient.getLastLocation();
			myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		}

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));

		user = ((User) User.getCurrentUser());
		gson = new Gson();

		addTransitionListeners();
		addMapActionListeners();
	}

	@SuppressLint("NewApi")
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map
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
				claimItem();
			}
		});

		findViewById(R.id.locate_items_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				locateMaterials();
			}
		});
	}

	private void claimItem() {
		if (locationClient != null && locationClient.isConnected()) {
			location = locationClient.getLastLocation();

			for (GooglePlace place : places) {
				double materialLat = place.getLat();
				double materialLng = place.getLng();

				double locationLat = location.getLatitude();
				double locationLng = location.getLongitude();

				// Randomly determined what is "close enough" in terms of the threshold
				if (Math.sqrt(Math.pow((materialLat - locationLat), 2) + Math.pow((materialLng - locationLng), 2)) < .2) {
					Marker closestMarker = markers.get(place);
					closestMarker.remove();

					// remove this material from the materialSolved and to the materialsCollected list
					Material closestMaterial = placeToMaterial.get(place);
					
					updateUser(closestMaterial.getName());
				}
			}
		}
	}

	private void updateUser(String material) {
		// need popup to reveal item
		showMaterialFoundDialog(material);

		// need background update
		ArrayList<String> materialsSolved = user.getMaterialsSolved();
		materialsSolved.remove(material);

		ArrayList<String> materialsCollected = user.getMaterialsCollected();
		materialsCollected.add(material);

		user.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e != null) {
					Log.d("Map Activity, updateUser", e.toString());
				}
			}
		});
	}

	private void showMaterialFoundDialog(String material) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Congrats!");

		// set dialog message
		alertDialogBuilder.setMessage("You found a " + material).setCancelable(false)
				.setPositiveButton("Yay!", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		ImageView image = new ImageView(this);
		image.setImageResource(R.drawable.map);
		
		alertDialogBuilder.setView(image);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	private void locateMaterials() {
		// get current location
		if (locationClient != null && locationClient.isConnected()) {
			location = locationClient.getLastLocation();
		}
		new PlaceMarkersOnMapTask().execute(places);
	}

	/**
	 * Async task to do three things: 1. Query the database for the materials that the user has collected 2. Get places
	 * that are related to <=5 materials 3. Render markers on those places
	 * 
	 * Remember that only the main thread can access the GUI elements which is why rendering the markers are is the post
	 * execution of the thread.
	 */
	private class PlaceMarkersOnMapTask extends AsyncTask<List<GooglePlace>, Void, Void> {
		protected Void doInBackground(List<GooglePlace>... params) {
			populateMaterialLocations();

			for (Material material : materialSearchTerms) {
				for (String searchTerm : material.getSearchTerms()) {
					String request = PLACES_URL + "&location=" + location.getLatitude() + "," + location.getLongitude()
							+ "&" + "radius=" + RADIUS + "&keyword=" + searchTerm + "&key=" + GOOGLE_PLACES_API_KEY;
					InputStream response = getPlaces(request);
					Reader reader = new InputStreamReader(response);

					if (reader != null) {
						// go on to the next material that the user has solved
						if (parseResponse(material, reader)) {
							break;
						}
					}
				}
			}
			return null;
		}

		/**
		 * Put in the five or less materials in the hashmap
		 */
		private void populateMaterialLocations() {
			ArrayList<String> materialsSolved = user.getMaterialsSolved();
			Collections.shuffle(materialsSolved);
			int listLength = Math.min(5, materialsSolved.size());
			ArrayList<String> materials = new ArrayList<String>(materialsSolved.subList(0, listLength));
			ParseQuery<Material> query = ParseQuery.getQuery(Material.class);
			query.whereContainedIn("name", materials);

			materialSearchTerms = new HashSet<Material>();

			try {
				List<Material> resultsList = query.find();
				for (Material m : resultsList) {
					materialSearchTerms.add(m);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		private InputStream getPlaces(String url) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);

			try {
				HttpResponse getResponse = client.execute(getRequest);
				final int statusCode = getResponse.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
					return null;
				}

				HttpEntity getResponseEntity = getResponse.getEntity();
				return getResponseEntity.getContent();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private boolean parseResponse(Material material, Reader reader) {
			GooglePlacesResponse placesJson = gson.fromJson(reader, GooglePlacesResponse.class);
			List<GooglePlace> resultsList = placesJson.getResults();
			if (resultsList.size() > 0) {
				GooglePlace materialPlace = resultsList.get(0);
				places.add(materialPlace);
				placeToMaterial.put(materialPlace, material);
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Void result) {
			for (GooglePlace place : places) {
				System.out.println(place.getName());
				Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())));
				markers.put(place, marker);
			}
		}
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
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationClient.requestLocationUpdates(REQUEST, this); // LocationListener
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
