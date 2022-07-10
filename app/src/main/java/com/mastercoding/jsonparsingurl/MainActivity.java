package com.mastercoding.jsonparsingurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketOption;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    String namey,age;

    private static String JSON_URL="https://run.mocky.io/v3/ce121de2-fd10-42f7-ad7b-1907e21c2344";

    ArrayList<HashMap<String, String>> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friendsList = new ArrayList<>();
        lv = findViewById(R.id.listView);

        GetData getData = new GetData();
        getData.execute();
    }

    public class GetData extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {

                URL url;
                HttpsURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpsURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    int data = isr.read();

                    while (data != -1){
                        current += (char) data;
                        data = isr.read();
                        System.out.print(current);
                    }
                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();

                    }
                }}
            catch (Exception e) {
                e.printStackTrace();
                return "Exception: " +e.getMessage();
            }
             return current;
            }

        @Override
        protected void onPostExecute(String s) {

                        try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("Friends");

                for(int i = 0;i<jsonArray.length();i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    namey = jsonObject1.getString("name");
                    age = jsonObject1.getString("age");


                    // Making a hashmap
                    HashMap<String, String> friends = new HashMap<>();

                    friends.put("name",namey);
                    friends.put("age",age);

                    friendsList.add(friends);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // displaying the results

            ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                    friendsList,
                    R.layout.row_layout,
                    new String[]{
                            "name", "age"},
                    new int[]{R.id.textView, R.id.textView2

                    });

            lv.setAdapter(adapter);

        }
    }
    }

