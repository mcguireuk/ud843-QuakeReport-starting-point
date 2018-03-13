/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.erin.paige.quakereport;

import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<String> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
            progress = (ProgressBar) findViewById(R.id.progress_view);
            progress.setVisibility(View.VISIBLE);
        } else {
            progress = (ProgressBar) findViewById(R.id.progress_view);
            progress.setVisibility(View.VISIBLE);
            updateUI("No Internet Connection");



        }

    }

    protected void updateUI(String jsonResponse) {

        // Create a fake list of earthquake locations.
        String emptyText = "";
        if (jsonResponse != null) {

            if (jsonResponse.equals("No Internet Connection")) {
                emptyText = "No Internet Connection";
            } else if (jsonResponse.equals("HTTP Connection error")) {
                emptyText = "HTTP Connection error";
            } else if (jsonResponse.equals("No results Found")) {
                emptyText = "No results Found";
                final ListView earthquakeListView = (ListView) findViewById(R.id.list);
                TextView empty = (TextView) findViewById(R.id.empty);
                empty.setText(emptyText);
                progress.setVisibility(View.GONE);
                earthquakeListView.setEmptyView(empty);
            } else {

                final ArrayList<Earthquakes> earthquakes = QueryUtils.extractEarthquakes(jsonResponse);
                // Find a reference to the {@link ListView} in the layout
                final ListView earthquakeListView = (ListView) findViewById(R.id.list);

// Create a new {@link ArrayAdapter} of earthquakes
                final EarthquakesArrayAdaptor adapter = new EarthquakesArrayAdaptor(
                        this, earthquakes);

                AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = adapter.getItem(i).getUrl();

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                };


                // Set the adapter on the {@link ListView}
                // so the list can be populated in the user interface

                earthquakeListView.setAdapter(adapter);
                earthquakeListView.setOnItemClickListener(itemClickListener);
                progress.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader");



        return new EarthquakeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String o) {
        Log.i(LOG_TAG, "onLoadFinished");

        updateUI(o);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.i(LOG_TAG, "OnLoaderReset");
        updateUI("");
    }

}



