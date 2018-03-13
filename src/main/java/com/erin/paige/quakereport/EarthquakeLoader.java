package com.erin.paige.quakereport;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static com.erin.paige.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Created by Shaun McGuire on 17/05/2017.
 */

class EarthquakeLoader extends AsyncTaskLoader<String> {

    public EarthquakeLoader (Context context){
            super(context);
    }


    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.i(LOG_TAG, "LoadInBackground");


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String minMagnitude = sharedPrefs.getString(getContext().getString(R.string.settings_min_magnitude_key), getContext().getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(getContext().getString(R.string.settings_order_by_key), getContext().getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse("https://earthquake.usgs.gov/fdsnws/event/1/query?");
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "100");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

       /* StringBuilder builder = new StringBuilder();
        String now = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis() - 12 * 60 * 60 * 1000);
        builder.append(yesterday + "&endtime=" + now + "&minmagnitude=1" + "&limit=100" + "&orderby=time");
        final String USGS_REQUEST_URL = builder.toString();*/


        Log.i("URI:  ", uriBuilder.toString());

     return HttpRequest(uriBuilder.toString());
    }

    private String HttpRequest(String USGS_REQUEST_URL) {
        Log.i(LOG_TAG, "HttpRequest");

        String jsonResponse;

        URL url;
        HttpURLConnection urlConnection;
        InputStream inputStream;
        try {
            url = new URL(USGS_REQUEST_URL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            if (urlConnection.getResponseCode() == 200) {
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Http Response Code:" + urlConnection.getResponseCode());
                return "Error with creating URL";
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with IOException on HTTP Connection", e);
            return "HTTP Connection error";
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (inputStream != null) {
            // function must handle java.io.IOException here
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error with IOException on HTTP Connection close", e);
            }
        }
        if (jsonResponse.contains("\"count\":0")){
            return "No results Found";
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) {

        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader buffer = new BufferedReader(inputStreamReader);
            try {
                String Line = buffer.readLine();
                while (Line != null) {
                    stringBuilder.append(Line);
                    Line = buffer.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return stringBuilder.toString();
    }

}
