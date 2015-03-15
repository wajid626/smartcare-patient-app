package com.smartcare.pillreminder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract.Document;
import android.renderscript.Element;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * This activity displays a google maps that will zoom to your current location.
 * It will also place markers on all of the pharmacies near your location
 * 
 * @author peteryasi
 * 
 */
public class MyGoogleMap extends FragmentActivity implements LocationListener {

	// create the maps
	private GoogleMap googleMap;
	private SupportMapFragment map;

	// global variables
	double currentLat = 0;
	double currentLong = 0;

	/**
	 * Called when activity is created. This method initializes the map
	 * instance. It will then use the location manager to detect the users
	 * current location and zoom into that spot
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_google_map);

		// initialize the map
		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapview)).getMap();
		googleMap.setMyLocationEnabled(true);
		
		// find the current location
		Criteria criteria = new Criteria();
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// if there is a location zoom the camera into the spot
		if (location != null) {
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			
			String locLat = String.valueOf(latitude) + ","
					+ String.valueOf(longitude);
			Log.d("MAD", locLat);

			// zoom the camera
			LatLng loc = new LatLng(latitude, longitude);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));
			
			// call to the get pharmacies method
			getPharmacies(latitude, longitude);
		}

	}

	/**
	 * This method is the begining of a list of methods involved in getting the
	 * location of various pharmacies from the internet and parsing that
	 * information into java so that markers can be placed according to their
	 * location
	 * 
	 * NOTE: This code was taken from an online source.
	 * http://wptrafficanalyzer.in/blog/showing-nearby-places
	 * -using-google-places-api-and-google-map-android-api-v2/
	 * 
	 * 
	 * @param lat
	 * @param log
	 */
	public void getPharmacies(double lat, double log) {

		Toast.makeText(MyGoogleMap.this, "Lat: " + lat + "Long:" + log,
				Toast.LENGTH_LONG).show();
		Log.d("MAD", "here");

		StringBuilder sb = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/search/json?");
		sb.append("location=" + lat + "," + log);
		sb.append("&radius=5000");
		sb.append("&types=pharmacy");
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc");
		Log.d("MAD", sb.toString());

		// Creating a new non-ui thread task to download json data
		PlacesTask placesTask = new PlacesTask();

		// Invokes the "doInBackground()" method of the class PlaceTask
		placesTask.execute(sb.toString());
	}

	/**
	 * A method to download json data from url NOTE: This code was taken from an
	 * online source. http://wptrafficanalyzer.in/blog/showing-nearby-places
	 * -using-google-places-api-and-google-map-android-api-v2/
	 * */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	/**
	 * A class, to download Google Places NOTE: This code was taken from an
	 * online source. http://wptrafficanalyzer.in/blog/showing-nearby-places
	 * -using-google-places-api-and-google-map-android-api-v2/
	 * */
	private class PlacesTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {
			ParserTask parserTask = new ParserTask();

			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}

	}

	/**
	 * A class to parse the Google Places in JSON format NOTE: This code was
	 * taken from an online source.
	 * http://wptrafficanalyzer.in/blog/showing-nearby-places
	 * -using-google-places-api-and-google-map-android-api-v2/
	 * */
	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/* Getting the parsed data as a List construct */
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			// Clears all the existing markers
			googleMap.clear();
			Log.d("MAD", "post ex");

			for (int i = 0; i < list.size(); i++) {

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();

				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);

				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat"));

				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng"));

				// Getting name
				String name = hmPlace.get("place_name");
				Log.d("MAD", "Name: " + name);

				// Getting vicinity
				String vicinity = hmPlace.get("vicinity");

				LatLng latLng = new LatLng(lat, lng);

				// Setting the position for the marker
				markerOptions.position(latLng);

				// Setting the title for the marker.
				// This will be displayed on taping the marker
				markerOptions.title(name + " : " + vicinity);

				// Placing a marker on the touched position
				googleMap.addMarker(markerOptions);
			}
		}
	}

	/**
	 * Overides the locationChanged method. when the location of the user
	 * changes this method detects that change and moves the camera accordingly
	 */
	@Override
	public void onLocationChanged(Location location) {

		
		// find the current latitude and longitude
		currentLat = location.getLatitude();
		currentLong = location.getLongitude();
		LatLng latLng = new LatLng(currentLat, currentLong);

		// Move the camera to new location
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
		
	}

}