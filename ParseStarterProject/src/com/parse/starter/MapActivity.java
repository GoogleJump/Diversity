package com.parse.starter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class MapActivity extends BaseActivity implements LocationListener, ConnectionCallbacks,
		OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	private User user;
	private final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	private final String GOOGLE_PLACES_API_KEY ="AIzaSyDKsYGo4Nk-aGBHl3JaOzorYp85TP9h6j4";
	private Location location;
	private final int RADIUS = 20000; // in meters

	private GoogleMap map;
	private LocationClient locationClient;
	private List<GooglePlace> places = new ArrayList<GooglePlace>();

	private Gson gson;

	private HashSet<Material> materialSearchTerms = new HashSet<Material>();

	private static final LocationRequest REQUEST = LocationRequest.create().setInterval(5000).setFastestInterval(16)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
			Location currentLocation = locationClient.getLastLocation();
			myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		}

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16));

		addTransitionListeners();
		addMapActionListeners();

		user = ((User) User.getCurrentUser());

		gson = new Gson();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
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
			// location is close to any markers, do something
		}
	}

	private void locateMaterials() {
		// get current location
		if (locationClient != null && locationClient.isConnected()) {
			location = locationClient.getLastLocation();
		}

		// pick five or less materials and their search terms
		populateMaterialLocations();

		// find nearby places and place markers
		callGooglePlacesAPI();
	}

	private void callGooglePlacesAPI() {
		for (Material material : materialSearchTerms) {
			for (String searchTerm : material.getSearchTerms()) {
				String request = PLACES_URL + "&location=" + location.getLatitude() + "," + location.getLongitude()
						+ "&" + "radius=" + RADIUS + "&keyword=" + searchTerm + "&key=" + GOOGLE_PLACES_API_KEY;
				System.out.println(request);
				InputStream response = getPlaces(request);
				Reader reader = new InputStreamReader(response);

				if (reader != null) {
					System.out.println("HEREEEEEEEEE");
					parseResponse(reader);
					placeMarkers();
				}
			}
		}
	}

	private void parseResponse(Reader reader) {
		GooglePlacesResponse placesJson = gson.fromJson(reader, GooglePlacesResponse.class);
		places = placesJson.getResults();
		System.out.println(places.get(0).getName());

	}

	private void placeMarkers() {
		for (GooglePlace place : places) {
			map.addMarker(new MarkerOptions().position(new LatLng(place.getLat(), place.getLng())));
		}
	}

	private InputStream getPlaces(String url) {
		// try {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		// URL serverUrl = new URL(url);

		// connect to the server
		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(serverUrl.openStream()));
		//
		// // read server response
		// String inputLine;
		// StringBuilder serverOutput = new StringBuilder();
		// while ((inputLine = in.readLine()) != null)
		// serverOutput.append(inputLine);
		// in.close();
		// return serverOutput.toString();
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return null;

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

		materialSearchTerms = new HashSet<Material>();

		try {
			List<Material> resultsList = query.find();
			for (Material m : resultsList) {
				materialSearchTerms.add(m);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void placeMaterials(List<Material> materials) {
		for (Material m : materials) {
			ArrayList<String> searchTerms = m.getSearchTerms();
			// for (String str : searchTerms) {
			//
			// }
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
		int lat = (int) (location.getLatitude());
		int lng = (int) (location.getLongitude());
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
