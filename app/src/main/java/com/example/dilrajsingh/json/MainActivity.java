package com.example.dilrajsingh.json;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  //  EditText translateEditText;
    Weather weather = new Weather();
    Wind wind = new Wind();
    Clouds clouds = new Clouds();
    AutoCompleteTextView translateEditText;
    Location location = new Location();
    TextView textView, translationTextView, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10;
    ArrayList<String> sd;
    ListView list;
    ScrollView scroll;
    ProgressDialog pg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // translateEditText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);
        textView9 = (TextView) findViewById(R.id.textView9);
        translationTextView = (TextView) findViewById(R.id.translationTextView);
        translateEditText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        translateEditText.setThreshold(1);
        String[] city = {"Faridabad", "Delhi", "Badli", "Samaypur"};
        pg = new ProgressDialog(this);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, city);
        translateEditText.setAdapter(ad);
   //     textView10 = (TextView) findViewById(R.id.textView10);
      //  scroll = (ScrollView) findViewById(R.id.scrollView);

    }

    // Calls for the AsyncTask to execute when the translate button is clicked
    public void onTranslateClick(View view) {


        // If the user entered words to translate then get the JSON data
        if(!isEmpty(translateEditText)){

            Toast.makeText(this, "Getting Weather",
                    Toast.LENGTH_LONG).show();
            pg.setMessage("Geting Weather");
            pg.setCancelable(false);
            pg.show();
            // Calls for the method doInBackground to execute
            new SaveTheFeed().execute();

        } else {

            // Post an error message if they didn't enter words
            Toast.makeText(this, "Enter Location",
                    Toast.LENGTH_SHORT).show();
            translationTextView.setText("Current Temperature : ");
            textView.setText("Humidity : ");
            textView2.setText("Pressure : ");
            textView3.setText("Maximum Temperature : ");
            textView4.setText("Minimum Temperature : ");
            textView5.setText("Wind Speed : ");
            textView6.setText("Wind Direction : ");
            textView7.setText("Clouds : ");
            textView8.setText("Location : ");
           // textView9.setText(result);

        }

    }

    // Check if the user entered words to translate
    // Returns false if not empty
    protected boolean isEmpty(EditText editText){

        // Get the text in the EditText convert it into a string, delete whitespace
        // and check length
        return editText.getText().toString().trim().length() == 0;

    }

    // Allows you to perform background operations without locking up the user interface
    // until they are finished
    // The void part is stating that it doesn't receive parameters, it doesn't monitor progress
    // and it won't pass a result to onPostExecute
    class SaveTheFeed extends AsyncTask<Void, Void, Void>{

        // Holds JSON data in String format
        String jsonString = "";

        // Will hold the translations that will be displayed on the screen
        String result = "";

        // Everything that should execute in the background goes here
        // You cannot edit the user interface from this method
        @Override
        protected Void doInBackground(Void... voids) {

            // Get access to the EditText so we can get the text in it


            // Get the text from EditText
            String wordsToTranslate = translateEditText.getText().toString();

            // Replace spaces in the String that was entered with + so they can be passed
            // in a URL
            wordsToTranslate = wordsToTranslate.replace(" ", "+");

            // Client used to grab data from a provided URL
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            // Provide the URL for the post request
            HttpPost httpPost = new HttpPost("http://api.openweathermap.org/data/2.5/weather?q=" + wordsToTranslate +  "&APPID=5f4dc4461931cda1c0a418db5d350d71&units=metric");

            // Define that the data expected is in JSON format
            httpPost.setHeader("Content-type", "application/json");

            // Allows you to input a stream of bytes from the URL
            InputStream inputStream = null;

            try{

                // The client calls for the post request to execute and sends the results back
                HttpResponse response = httpClient.execute(httpPost);

                // Holds the message sent by the response
                HttpEntity entity = response.getEntity();

                // Get the content sent
                inputStream = entity.getContent();

                // A BufferedReader is used because it is efficient
                // The InputStreamReader converts the bytes into characters
                // My JSON data is UTF-8 so I read that encoding
                // 8 defines the input buffer size
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 800000);

                // Storing each line of data in a StringBuilder
                StringBuilder sb = new StringBuilder();

                String line = null;

                // readLine reads all characters up to a \n and then stores them
                while((line = reader.readLine()) != null){

                    sb.append(line + "\n");

                }

                // Save the results in a String
                jsonString = sb.toString();

                // Create a JSONObject by passing the JSON data
                JSONObject jObject = new JSONObject(jsonString);


             //   JSONObject jObj = getObject("weather", jObject);
                JSONObject jObj2 = getObject("main", jObject);
                JSONObject jObj3 = getObject("wind", jObject);
                JSONObject jObj4 = getObject("clouds", jObject);
                JSONObject jObj5 = getObject("coord", jObject);

                // Get the Array named translations that contains all the translations
        //        org.json.JSONArray jsonArray = jObject.getJSONArray("weather");

                // Cycles through every translation in the array
                String[] languages = {"description"};

              //  JSONObject translationObject =
                //        jsonArray.getJSONObject(0);

              //  result = result + languages[0] + " : " +
                //        translationObject.getString(languages[0]) + "\n";

                // Save all the translations by getting them with the key
                weather.setTemp(getFloat("temp", jObj2));
                weather.setHum(getLong("humidity", jObj2));
                weather.setPress(getFloat("pressure", jObj2));
                weather.setTmax(getFloat("temp_max", jObj2));
                weather.setTmin(getFloat("temp_min", jObj2));

                wind.setDeg(getFloat("deg", jObj3));
                wind.setSpeed(getFloat("speed", jObj3));

                clouds.setAll(getInt("all", jObj4));

                location.setLat(getFloat("lat", jObj5));
                location.setLon(getFloat("lon", jObj5));

                // Save all the translations by getting them with the key
               // try{









            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Called after doInBackground finishes executing
        @Override
        protected void onPostExecute(Void aVoid) {

            // Put the translations in the TextView
            TextView translationTextView = (TextView) findViewById(R.id.translationTextView);
            pg.dismiss();
            translationTextView.setText("Current Temperature : " + Float.toString(weather.getTemp()) + " ^C");
            textView.setText("Humidity : " + Long.toString(weather.getHum()) + "%");
            textView2.setText("Pressure : " + Float.toString(weather.getPress()) + " Pa");
            textView3.setText("Maximum Temperature : " + Float.toString(weather.getTmax()) + " ^C");
            textView4.setText("Minimum Temperature : " + Float.toString(weather.getTmin()) + " ^C");
            textView5.setText("Wind Speed : " + Float.toString(wind.getSpeed()) + " m/s");
            textView6.setText("Wind Direction : " + Float.toString(wind.getDeg()));
            textView7.setText("Clouds : " + Integer.toString(clouds.getAll()) + "%");
            textView8.setText("Location : (" + Float.toString(location.getLat()) + ", " + location.getLon() + ")");
            textView9.setText(result);

            //text.setText(result);
           // scroll.addView(textView);
        }

        protected void outputTranslations(JSONArray jsonArray){

            // Used to get the translation using a key


        }

    }
    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static long  getLong(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getLong(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

}
