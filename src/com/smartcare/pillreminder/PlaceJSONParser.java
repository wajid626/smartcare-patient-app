package com.smartcare.pillreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used in order to Parse the JSON information received from the
 * internet in order to read into as java code
 * 
 * NOTE: This code was taken from an online source.
 * http://wptrafficanalyzer.in/blog/showing-nearby-places
 * -using-google-places-api-and-google-map-android-api-v2/
 * 
 * @author peteryasi
 * 
 */
public class PlaceJSONParser {

	/** Receives a JSONObject and returns a list */
	public List<HashMap<String, String>> parse(JSONObject jObject) {

		JSONArray jPlaces = null;
		try {
			// Retrieves all the elements in the 'places' array
			jPlaces = jObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/*
		 * Invoking getPlaces with the array of json object where each json
		 * object represent a place
		 */
		return getPlaces(jPlaces);
	}

	private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> place = null;

		/* Taking each place, parses and adds to list object */
		for (int i = 0; i < placesCount; i++) {
			try {
				/* Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject) jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return placesList;
	}

	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlace) {

		HashMap<String, String> place = new HashMap<String, String>();
		String placeName = "-NA-";
		String vicinity = "-NA-";
		String latitude = "";
		String longitude = "";

		try {
			// Extracting Place name, if available
			if (!jPlace.isNull("name")) {
				placeName = jPlace.getString("name");
			}

			// Extracting Place Vicinity, if available
			if (!jPlace.isNull("vicinity")) {
				vicinity = jPlace.getString("vicinity");
			}

			latitude = jPlace.getJSONObject("geometry")
					.getJSONObject("location").getString("lat");
			longitude = jPlace.getJSONObject("geometry")
					.getJSONObject("location").getString("lng");

			place.put("place_name", placeName);
			place.put("vicinity", vicinity);
			place.put("lat", latitude);
			place.put("lng", longitude);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return place;
	}
}