package com.parse.starter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.team.diversity.android.R;

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
	private final double CLAIM_DISTANCE = 10;

	private Gson gson;

	private UserInfo userInfo;
	private MapFragment mapFragment;
	private GoogleMap map;
	private LocationClient locationClient;
	private Context context = this;

	private ConcurrentHashMap<Material, MaterialMapInfo> materialsOnTheMap = new ConcurrentHashMap<Material, MaterialMapInfo>();

	private Button mainMenuButton;
	private Button puzzleButton;
	private Button randomizeButton;
	private Button claimButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		if (locationClient == null) {
			locationClient = new LocationClient(this, this, this);
		}
		if (!locationClient.isConnected()) {
			locationClient.connect();
		}

		setContentView(R.layout.map);
		mainMenuButton = (Button) findViewById(R.id.main_menu_button);
		puzzleButton = (Button) findViewById(R.id.puzzle_button);
		randomizeButton = (Button) findViewById(R.id.randomize_button);
		claimButton = (Button) findViewById(R.id.claim_materials_button);

		addTransitionListeners();

		mapFragment = (MapFragment) getFragmentManager().findFragmentById(
				R.id.map);
		GoogleMap map = mapFragment.getMap();

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
			locateMaterials();

		}

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
				ZOOM_LEVEL));

		User user = (User) User.getCurrentUser();
		userInfo = user.getUserInfo();
		gson = new Gson();

		addMapActionListeners();
	}

	@Override
	public void onBackPressed() {
		// MapActivity.this.finish();
		startActivity(new Intent(MapActivity.this, MainMenuActivity.class));
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
						// MapActivity.this.finish();
						Intent i = new Intent(MapActivity.this,
								MainMenuActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);
						removeAllMarkers();
					}
				});

		findViewById(R.id.puzzle_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						removeAllMarkers();
						startActivity(new Intent(MapActivity.this,
								PuzzleActivity.class));
					}
				});
	}

	private void removeAllMarkers() {
		Iterator<Entry<Material, MaterialMapInfo>> iterator = materialsOnTheMap
				.entrySet().iterator();
		while (iterator.hasNext()) {
			MaterialMapInfo mapInfo = iterator.next().getValue();
			if (mapInfo != null) {
				if (mapInfo.getMarker() != null) {
					mapInfo.getMarker().remove();
				}
			}
		}
	}

	private void addMapActionListeners() {
		findViewById(R.id.randomize_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						changeAllButtonStates(false);
						// remove all the existing markers
						// This might not be necessary if the map will be
						// re-rendered
						synchronized (materialsOnTheMap) {
							for (MaterialMapInfo materialInfo : materialsOnTheMap
									.values()) {
								if (materialInfo.marker != null) {
									materialInfo.marker.remove();
								}
							}
							materialsOnTheMap.clear();
						}
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
	}

	private void changeAllButtonStates(boolean enabled) {
		mainMenuButton.setEnabled(enabled);
		puzzleButton.setEnabled(enabled);
		randomizeButton.setEnabled(enabled);
		claimButton.setEnabled(enabled);
	}

	private void claimMaterial() {
		if (materialsOnTheMap.isEmpty()) {
			// display a popup
			showWarningDialog(R.string.no_located_materials);
			changeAllButtonStates(true);
			return;
		}

		if (locationClient != null && locationClient.isConnected()) {
			location = locationClient.getLastLocation();
			ConcurrentHashMap<Material, MaterialMapInfo> materialsToRemove = new ConcurrentHashMap<Material, MaterialMapInfo>();
			synchronized (materialsOnTheMap) {
				for (Entry<Material, MaterialMapInfo> material : materialsOnTheMap
						.entrySet()) {
					GooglePlace place = material.getValue().getPlace();
					System.out.println(place);
					Location materialLocation = new Location("");
					materialLocation.setLatitude(place.getLat());
					materialLocation.setLongitude(place.getLng());

					// distance in terms of meters
					float distance = materialLocation.distanceTo(location);
					if (distance < CLAIM_DISTANCE) {
						Marker closestMarker = material.getValue().getMarker();
						if (closestMarker != null) {
							closestMarker.remove();
							materialsToRemove.put(material.getKey(),
									material.getValue());

							// updating userInfo when materials are solved
							updateUserMaterial(material.getKey().getName());
						}
					}
				}
				checkForCompletedItem();
				for (Material material : materialsToRemove.keySet()) {
					materialsOnTheMap.remove(material);
					showFoundDialog("You found ", material.getArticle(),
							material.getName(), false);
				}
				if (materialsToRemove.keySet().size() == 0) {
					showWarningDialog(R.string.no_nearby_materials);
					changeAllButtonStates(true);
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
			String article = resultsList.get(0).getArticle();
			for (String material : queriedItemMaterials) {
				if (!materialsCollected.contains(material)) {
					return;
				}
			}
			System.out.println("item completed");
			updateUserOther(item, article);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the user information about collected items; Displays a popup to
	 * congratulate user. Gets a new Item when the user solved the existing item
	 * and when the user has more items to solve
	 */
	private void updateUserOther(String name, String article) {
		userInfo.addItemCollected(name);
		userInfo.getNewItem();

		// indicates that the Character has been completed
		if (userInfo.getCurrentItem().equals("FINISHED")) {
			String completedChar = userInfo.getCurrentCharacter();

			// updating userInfo
			userInfo.addCharacterCollected(completedChar);
			userInfo.setCurrentCharacter("");
			showFoundDialog("You just found all items for ", "the ",
					completedChar, true /* isChar */);
		}
		showFoundDialog("You just made ", article, name, false);
		userInfo.setMaterialsCollected(Collections.<String> emptyList());
		// need background update
	}

	/**
	 * Updates the user information about collected materials; Displays a popup
	 * to congratulate user. Gets a Material when the user solved the existing
	 * item and when the user has more items to solve
	 */
	private void updateUserMaterial(String name) {
		List<String> materialsSolved = userInfo.getMaterialsSolved();
		materialsSolved.remove(name);

		List<String> materialsCollected = userInfo.getMaterialsCollected();
		materialsCollected.add(name);

		userInfo.saveEventually(new SaveCallback() {
			public void done(ParseException e) {
				if (e != null) {
					Log.d("Map Activity, updateUser", e.toString());
				}
			}
		});

	}

	/**
	 * Found dialog that pops up when a material/item/character is found
	 * 
	 * @param msg
	 *            String message to appear in the found dialog
	 * @param materialOrItemOrChar
	 *            String name of the material/item/character found dialog is for
	 * @param isChar
	 *            boolean that is true if dialog indicates completed character
	 *            false otherwise
	 */
	private void showFoundDialog(String message, String article,
			String materialOrItem, final boolean isChar) {

		final Dialog myDialog = new Dialog(context);
		myDialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.one_button_image_dialog);
		myDialog.setCancelable(false);

		TextView dialog_title = (TextView) myDialog.findViewById(R.id.title);
		dialog_title.setText("Congrats!");

		TextView dialog_message = (TextView) myDialog
				.findViewById(R.id.message);
		dialog_message.setText(message + article + materialOrItem + "!");

		final ImageView image = (ImageView) myDialog
				.findViewById(R.id.collected);
		System.out.println("before setting the picture in the image view");

		if (!isChar) {
			// name of the image to add
			String imageName = materialOrItem.toLowerCase().replace(' ', '_');
			// set image here
			image.setImageResource(getResources().getIdentifier(imageName,
					"drawable", getPackageName()));
		}
		Button yes = (Button) myDialog.findViewById(R.id.dialog_yes);
		yes.setText("Yay!");

		yes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isChar) {
					myDialog.dismiss();
					Intent i = new Intent(MapActivity.this,
							PhotosActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
				} else {
					myDialog.dismiss();
				}
				Drawable drawable = image.getDrawable();
				if (drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					Bitmap bitmap = bitmapDrawable.getBitmap();
					bitmap.recycle();
				}

			}
		});

		myDialog.show();
	}

	private void locateMaterials() {
		// get current location
		if (locationClient != null && locationClient.isConnected()) {
			location = locationClient.getLastLocation();
		}
		if (userInfo.getMaterialsSolved().isEmpty()) {
			// display a popup
			changeAllButtonStates(true);
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
		protected Void doInBackground(
				ConcurrentHashMap<Material, MaterialMapInfo>... params) {
			populateMaterialLocations();

			for (Material material : materialsOnTheMap.keySet()) {
				// TODO(kseniab): pass by value?
				ArrayList<String> searchTerms = new ArrayList<String>(
						material.getSearchTerms());
				Collections.shuffle(searchTerms);
				for (String searchTerm : searchTerms) { /**/
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
					synchronized (materialsOnTheMap) {
						if (!materialsOnTheMap.keySet().contains(m)) {
							materialsOnTheMap.put(m, new MaterialMapInfo());
						}
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
			Collections.shuffle(resultsList);
			if (resultsList.size() > 0) {
				GooglePlace materialPlace = resultsList.get(0);
				synchronized (materialsOnTheMap) {
					MaterialMapInfo materialMap = materialsOnTheMap
							.get(material);
					if (materialMap != null && materialMap.getPlace() == null) {
						materialMap.setPlace(materialPlace);
					}
					return true;
				}
			}
			return false;
		}

		// This should be called after the PlaceMarkersOnMapTask execution is
		// done
		@Override
		protected void onPostExecute(Void result) {
			synchronized (materialsOnTheMap) {
				LatLngBounds.Builder bc = new LatLngBounds.Builder();
				bc.include(new LatLng(location.getLatitude(), location
						.getLongitude()));
				for (Entry<Material, MaterialMapInfo> material : materialsOnTheMap
						.entrySet()) {
					GooglePlace currentPlace = material.getValue().getPlace();
					Marker currentMarker = material.getValue().getMarker();
					String placeName = currentPlace.getName();
					LatLng location = new LatLng(currentPlace.getLat(),
							currentPlace.getLng());
					if (currentMarker == null) {
						currentMarker = map.addMarker(new MarkerOptions()
								.position(location)
								.title(placeName)
								.snippet(
										"You can get "
												+ material.getKey().getName()
												+ " here"));

						material.getValue().setMarker(currentMarker);
					}
					bc.include(location);
				}
				map.animateCamera(CameraUpdateFactory.newLatLngBounds(
						bc.build(), 100));
			}
			changeAllButtonStates(true);
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
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
				ZOOM_LEVEL));
		locateMaterials();
	}

	@Override
	public void onDisconnected() {
		if (locationClient != null) {
			locationClient.disconnect();
		}
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
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
