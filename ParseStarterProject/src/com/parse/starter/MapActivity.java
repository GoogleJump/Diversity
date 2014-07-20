package com.parse.starter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class MapActivity extends BaseActivity implements LocationListener,
		ConnectionCallbacks, OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener {
	private final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	private final String GOOGLE_PLACES_API_KEY = "AIzaSyCVxBo2VC97qIx_bP_w73L6hc15cLVvBMg";
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000).setFastestInterval(16)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	private Location location;
	private final int RADIUS = 10000; // in meters
	private final float ZOOM_LEVEL = 12;

	private Gson gson;

	private UserInfo userInfo;
	private GoogleMap map;
	private LocationClient locationClient;

	private ConcurrentHashMap<Material, MaterialMapInfo> materialsOnTheMap
			= new ConcurrentHashMap<Material, MaterialMapInfo>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (locationClient == null) {
			locationClient = new LocationClient(this, this, this);
		}
		if (!locationClient.isConnected()) {
			locationClient.connect();
		}		
	
		setContentView(R.layout.map);
		addTransitionListeners();

		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		map.setMyLocationEnabled(true);
		map.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				marker.showInfoWindow();
				return true;
			}
		});

		// Default because current location cannot be determined at start time
		LatLng myLocation = new LatLng(37.4220, -122.0804);
		if (locationClient != null && locationClient.isConnected()) {
			Location currentLocation = locationClient.getLastLocation();
			myLocation = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
		}

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, ZOOM_LEVEL));

		User user = (User) User.getCurrentUser();
		userInfo = user.getUserInfo();
		gson = new Gson();

		addMapActionListeners();
	}

	@SuppressLint("NewApi")
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map
		if (map == null) {
			// Try to obtain the map from the SupportMapFragment.
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
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
		findViewById(R.id.main_menu_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						MapActivity.this.finish();
						startActivity(new Intent(MapActivity.this,
								MainMenuActivity.class));
					}
				});

		findViewById(R.id.puzzle_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						MapActivity.this.finish();
						startActivity(new Intent(MapActivity.this,
								PuzzleActivity.class));
					}
				});
	}

	private void addMapActionListeners() {
		findViewById(R.id.randomize_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						locateMaterials();
					}
				});

		findViewById(R.id.claim_materials_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						claimMaterial();
					}
				});

		findViewById(R.id.locate_materials_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						locateMaterials();
					}
				});
	}

	private void claimMaterial() {
		if (materialsOnTheMap.isEmpty()) {
			//display a popup
			showWarningDialog("No items have been located; you need to locate them first.");
			return;
		}

		if (locationClient != null && locationClient.isConnected()) {
			location = locationClient.getLastLocation();
			ConcurrentHashMap<Material, MaterialMapInfo> materialsToRemove = new ConcurrentHashMap<Material, MaterialMapInfo>();
			synchronized (materialsOnTheMap) {
				for (Entry<Material, MaterialMapInfo> material: materialsOnTheMap.entrySet()) {
					GooglePlace place = material.getValue().getPlace();
					double materialLat = place.getLat();
					double materialLng = place.getLng();

					double locationLat = location.getLatitude();
					double locationLng = location.getLongitude();

						// Randomly determined what is "close enough" in terms of the
						// threshold
						// TODO(kseniab): fix the algorithm and make it more sensitive
						if (Math.sqrt(Math.pow((materialLat - locationLat), 2)
							+ Math.pow((materialLng - locationLng), 2)) < .2) {
						Marker closestMarker = material.getValue().getMarker();
						if (closestMarker != null) {
							closestMarker.remove();
							materialsToRemove.put(material.getKey(), material.getValue());
							updateUser(MATERIAL_ITEM.MATERIAL,
									material.getKey().getName());

							checkForCompletedItem();
						}
					}
				}
				for(Material material : materialsToRemove.keySet()) {
					materialsOnTheMap.remove(material);
				}
			}
		}
	}

	private void checkForCompletedItem() {
		List<String> materialsCollected = userInfo.getMaterialsCollected();
		String item = userInfo.getCurrentItem();
			ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
			query.whereEqualTo("name", item);

			try {
				List<Item> resultsList = query.find();
				List<String> queriedItemMaterials = resultsList.get(0)
						.getMaterials();
				for (String material : queriedItemMaterials) {
					if (!materialsCollected.contains(material)) {
						return;
					}
				}
				updateUser(MATERIAL_ITEM.ITEM, item);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	/**
	 * Updates the user information about collected items and materials;
	 * Displays a popup to congratulate user.
	 * Gets a new Item when the user solved the existing item.
	 */
	private void updateUser(MATERIAL_ITEM materialOrItem, String name) {
		if (materialOrItem == MATERIAL_ITEM.MATERIAL) {
			showFoundDialog("You found a " + name);

			List<String> materialsSolved = userInfo.getMaterialsSolved();
			materialsSolved.remove(name);

			List<String> materialsCollected = userInfo.getMaterialsCollected();
			materialsCollected.add(name);

			userInfo.saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if (e != null) {
						Log.d("Map Activity, updateUser", e.toString());
					}
				}
			});
		} else {
			showFoundDialog("You just made a " + name);

			List<String> itemsSolved = userInfo.getItemsSolved();
			itemsSolved.remove(name);

			List<String> itemsCollected = userInfo.getItemsCollected();
			itemsCollected.add(name);
			userInfo.getNewItem();

			userInfo.saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if (e != null) {
						Log.d("Map Activity, updateUser", e.toString());
					}
				}
			});
		}

		// need background update

	}

	private void showFoundDialog(String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Congrats!");

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Yay!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		ImageView image = new ImageView(this);
		// set image here
		image.setImageResource(R.drawable.map);

		alertDialogBuilder.setView(image);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	private void showWarningDialog(String msg) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Ooops!");

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Got it!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
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
		if (userInfo.getMaterialsSolved().isEmpty()) {
			//display a popup
			showWarningDialog("No solved items. Go solve some puzzles =)");
			return;
		}
		new PlaceMarkersOnMapTask().execute(materialsOnTheMap);
	}

	/**
	 * Async task to do three things: 1. Query the database for the materials
	 * that the user has collected 2. Get places that are related to <=5
	 * materials 3. Render markers on those places
	 * 
	 * Remember that only the main thread can access the GUI elements which is
	 * why rendering the markers are is the post execution of the thread.
	 */
	private class PlaceMarkersOnMapTask extends
			AsyncTask<ConcurrentHashMap<Material, MaterialMapInfo>, Void, Void> {
		protected Void doInBackground(ConcurrentHashMap<Material, MaterialMapInfo>... params) {
			populateMaterialLocations();

			for (Material material : materialsOnTheMap.keySet()) {
				for (String searchTerm : material.getSearchTerms()) {
					String request = PLACES_URL + "&location="
							+ location.getLatitude() + ","
							+ location.getLongitude() + "&" + "radius="
							+ RADIUS + "&keyword=" + searchTerm + "&key="
							+ GOOGLE_PLACES_API_KEY;
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
			List<String> materialsSolved = userInfo.getMaterialsSolved();
			Collections.shuffle(materialsSolved);
			int listLength = Math.min(5, materialsSolved.size());
			ArrayList<String> allMaterials = new ArrayList<String>(
					materialsSolved.subList(0, listLength));
			ParseQuery<Material> query = ParseQuery.getQuery(Material.class);
			query.whereContainedIn("name", allMaterials);

			try {
				List<Material> resultsList = query.find();
				for (Material m : resultsList) {
					if (!materialsOnTheMap.keySet().contains(m)) {
					materialsOnTheMap.put(m, new MaterialMapInfo());
					}
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
				final int statusCode = getResponse.getStatusLine()
						.getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w(getClass().getSimpleName(), "Error " + statusCode
							+ " for URL " + url);
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
			GooglePlacesResponse placesJson = gson.fromJson(reader,
					GooglePlacesResponse.class);
			List<GooglePlace> resultsList = placesJson.getResults();
			if (resultsList.size() > 0) {
					GooglePlace materialPlace = resultsList.get(0);
					synchronized (materialsOnTheMap) {
						MaterialMapInfo materialMap = materialsOnTheMap.get(material);
						if (materialMap != null && materialMap.getPlace() == null) {
							materialMap.setPlace(materialPlace);
						}
					return true;
					}
				}
				return false;
		}

		// This should be called after the PlaceMarkersOnMapTask execution is done
		@Override
		protected void onPostExecute(Void result) {
			synchronized (materialsOnTheMap) {
				for (Entry<Material, MaterialMapInfo> material : materialsOnTheMap.entrySet()) {
					GooglePlace currentPlace = material.getValue().getPlace();
					Marker currentMarker = material.getValue().getMarker();
					String placeName = currentPlace.getName();
					System.out.println(placeName);
					if (currentMarker == null) {
						currentMarker = map.addMarker(new MarkerOptions()
							.position(new LatLng(currentPlace.getLat(), currentPlace.getLng()))
	                        .title(placeName)
	                        .snippet("You can get " + material.getKey().getName() + " here"));
					
						material.getValue().setMarker(currentMarker);
					}
				}
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!locationClient.isConnected()) {
			locationClient.connect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		if (!locationClient.isConnected()) {
			locationClient.connect();
		}
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
		Location currentLocation = locationClient.getLastLocation();
		LatLng myLocation = new LatLng(currentLocation.getLatitude(),
				currentLocation.getLongitude());
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, ZOOM_LEVEL));
	}

	@Override
	public void onDisconnected() {
		if (locationClient != null) {
			locationClient.disconnect();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationClient != null) {
			locationClient.disconnect();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (locationClient != null) {
			locationClient.disconnect();
		}
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

	public enum MATERIAL_ITEM {
		MATERIAL, ITEM;
	}
	
	class MaterialMapInfo {
	private Marker marker;
	private GooglePlace place;

	void setMarker(Marker marker) {
		this.marker = marker;
	}

	void setPlace(GooglePlace place) {
		this.place = place;
	}
	
	Marker getMarker() {
		return marker;
	}

	GooglePlace getPlace() {
		return place;
	}
}
}
