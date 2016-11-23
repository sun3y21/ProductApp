package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static com.example.sunnny.productapp.MainActivity.MOBILE;
import static com.example.sunnny.productapp.MainActivity.NAME;

public class UserProfile extends AppCompatActivity {

    Uri.Builder builder=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref =getSharedPreferences("ProductApp", Context.MODE_PRIVATE);
        TextView nameT=(TextView)findViewById(R.id.user_name);
        nameT.setText(sharedPref.getString(NAME,""));

        TextView mobileT=(TextView)findViewById(R.id.user_number);
        mobileT.setText(sharedPref.getString(MOBILE,""));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    class Connection extends AsyncTask<String,String,JSONObject>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json=JSONParser.makeHttpRequest(strings[0],builder);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json)
        {
            super.onPostExecute(json);
            if(progress.isShowing())
                progress.dismiss();
            if(json!=null)
            {

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Can't connect to server",Toast.LENGTH_LONG).show();
            }
        }
    }


}
