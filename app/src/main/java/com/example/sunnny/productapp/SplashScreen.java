package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sunnny.productapp.MainActivity.LOGGED_IN;

public class SplashScreen extends AppCompatActivity {

    Uri.Builder builder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences sharedPref =getSharedPreferences("ProductApp", Context.MODE_PRIVATE);
        boolean loggedIn=sharedPref.getBoolean(LOGGED_IN,false);
        Log.v("ProductApp:","Logged in: "+loggedIn);


        if(loggedIn==false)
        {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                   /* Create an Intent that will start the Menu-Activity. */
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 3000);

        }
        else
        {
            ConnectivityManager c=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
            if(c.getActiveNetworkInfo()==null)
            {
                Toast.makeText(getApplicationContext(),"No Internet Access.",Toast.LENGTH_LONG).show();
            }
            else
            {
                builder=new Uri.Builder();
                builder.appendQueryParameter("limit","30");
                //String url="http://10.0.2.2/ProductApp/getProducts.php";
                String url="http://www.sun3y21.pe.hu/ProductApp/getProducts.php";
                new Connection().execute(url);
            }

        }
    }

    class Connection extends AsyncTask<String,String,JSONObject> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            return JSONParser.makeHttpRequest(strings[0], builder);
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            if (json != null) {
                try {
                     if (json.getString("status").equalsIgnoreCase("success") && json.getString("msg").equalsIgnoreCase("result")) {
                        JSONArray jsonArray = json.getJSONArray("result");
                        Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                        ArrayList<Product> arr = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Product p = new Product();
                            p.setDescription(jsonObject.getString("description"));
                            p.setId(jsonObject.getString("id"));
                            p.setImageUrl(jsonObject.getString("url"));
                            p.setLatitude(jsonObject.getDouble("latitude"));
                            p.setLongitude(jsonObject.getDouble("longitude"));
                            p.setName(jsonObject.getString("name"));
                            p.setPrice(jsonObject.getDouble("price"));
                            p.setRating(jsonObject.getDouble("rating"));
                            p.setQuantity(jsonObject.getInt("quantity"));
                            arr.add(p);
                        }
                        intent.putParcelableArrayListExtra("result", arr);
                        startActivity(intent);
                        finish();
                     }
                     else
                     {
                         Toast.makeText(getApplicationContext(), "Unable to fetch data.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Bad response from server.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Unable to connect server.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
