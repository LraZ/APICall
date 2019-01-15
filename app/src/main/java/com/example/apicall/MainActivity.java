package com.example.apicall;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.apicall.entities.AccessPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    ListView lv;
    Button  btnFetchWeather;
    ArrayList<HashMap<String,String>> resultList;
    AccessPoint tempAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_url_connection);
        resultList = new ArrayList<>();
        tempAP = new AccessPoint();

        lv = (ListView) findViewById(R.id.tv_weather_json);
        btnFetchWeather = (Button) findViewById(R.id.btn_fetch_weather);
        btnFetchWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchWeatherData().execute();
            }
        });
    }


    private class FetchWeatherData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String response = "";


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //https://www.salzburg.gv.at/ogd/c8711f5c-a49f-446d-ad69-6435bbc5a78e/names-szg.json
                //http://192.168.0.241:9000/api/getAllAccessPoints
                URL url = new URL("http://192.168.0.233:9000/api/getAllAccessPoints");

                // Create the request to OpenWeatherMap, and open the connection
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();


                // Read the input stream into a String
                InputStream in = new BufferedInputStream(conn.getInputStream());
                StringBuilder sb = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(in));

                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append('\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                response = sb.toString();

                Log.e(TAG, "Response from url: " + response);

                if (response != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        // Getting JSON Array node
                        JSONArray data = jsonObj.getJSONArray("data");

                        // looping through All Contacts
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject d = data.getJSONObject(i);
                            String mac = d.getString("mac");
                            String type = d.getString("type");
                            String activity = d.getString("activity");
                            tempAP.setMac(d.getString("mac"));
                            tempAP.setType(d.getInt("type"));
                            tempAP.setActivity(d.getBoolean("activity"));


                            // tmp hash map for single contact
                            HashMap<String, String> DataHashMap = new HashMap<>();

                            // adding each child node to HashMap key => value
                            DataHashMap.put("mac", mac);
                            DataHashMap.put("type", type);
                            DataHashMap.put("activity", activity);

                            // adding contact to contact list
                            resultList.add(DataHashMap);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                return null;
                //return resultList;
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, resultList,
                    R.layout.list_item, new String[]{ "mac","type", "activity"},
                    new int[]{R.id.mac, R.id.type, R.id.activity});
            lv.setAdapter(adapter);
        }
    }
}