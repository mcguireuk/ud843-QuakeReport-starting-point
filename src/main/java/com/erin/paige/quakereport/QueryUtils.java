package com.erin.paige.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }



    /**
     * Return a list of {@link Earthquakes} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquakes> extractEarthquakes(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquakes> earthquakes = new ArrayList<>();



        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject earthquakesList = new JSONObject(jsonResponse);
            JSONArray earthquakeFeatures = earthquakesList.getJSONArray("features");

            for(int i = 0; i < earthquakeFeatures.length(); i++){
               JSONObject earthquake = earthquakeFeatures.getJSONObject(i);
                JSONObject earthquakeProperties = earthquake.getJSONObject("properties");

                double mag = earthquakeProperties.getDouble("mag");
                String place = earthquakeProperties.getString("place");
                Long time = earthquakeProperties.getLong("time");
                String url = earthquakeProperties.getString("url");

                Date dateObject = new Date(time);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm a");
                String dateToDisplay = dateFormat.format(dateObject);
                String timeToDisplay = timeFormat.format(dateObject);

                earthquakes.add(new Earthquakes(mag,place,dateToDisplay, timeToDisplay, url));

            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results" + " - " + jsonResponse, e);



        }

        // Return the list of earthquakes
        return earthquakes;
    }


}