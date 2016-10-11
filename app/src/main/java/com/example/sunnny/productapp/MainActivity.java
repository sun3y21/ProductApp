package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final static String MOBILE = "mobile";
    final static String LOGGED_IN="loggedIn";
    final static String NAME="name";
    String number=null;
    Uri.Builder builder=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if already login move to main Window
        SharedPreferences sharedPref =getSharedPreferences("ProductApp",Context.MODE_PRIVATE);
        boolean loggedIn=sharedPref.getBoolean(LOGGED_IN,false);
        Log.v("ProductApp:","Logged in: "+loggedIn);


        if(loggedIn)
        {
            String name=sharedPref.getString(NAME,"unknown");
            Toast.makeText(getApplicationContext(),name+" already logged in",Toast.LENGTH_LONG).show();
            Intent i=new Intent(getApplicationContext(),MainWindow.class);
            startActivity(i);
            finish();
        }
    }

    public void signUp(View view)
    {
        ConnectivityManager c=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(c.getActiveNetworkInfo()==null)
        {
            Toast.makeText(getApplicationContext(),"No Internet Access.",Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent i=new Intent(getApplicationContext(),SignUpPage.class);
            startActivity(i);

        }
    }

    public void logIn(View view)
    {
        ConnectivityManager c=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(c.getActiveNetworkInfo()==null)
        {
            Toast.makeText(getApplicationContext(),"No Internet Access.",Toast.LENGTH_LONG).show();
        }
        else
        {
            EditText numText=(EditText)findViewById(R.id.phoneText);
            EditText passText=(EditText)findViewById(R.id.passwordText);
            number=numText.getText().toString();
            String password=passText.getText().toString();

            builder=new Uri.Builder();
            builder.appendQueryParameter("mobile",number);
            builder.appendQueryParameter("password",password);
            String url="http://10.0.2.2/ProductApp/login.php";
            new Connection().execute(url);
        }
    }

    public void skip(View view)
    {
          Intent i=new Intent(getApplicationContext(),MainWindow.class);
          startActivity(i);
          finish();
    }

    public void forgotPass(View view)
    {
         Toast.makeText(getApplicationContext(),"Option Unavailable",Toast.LENGTH_LONG).show();
    }


    class Connection extends AsyncTask<String,String,JSONObject>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Connecting...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            return JSONParser.makeHttpRequest(strings[0],builder);
        }


        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            progress.dismiss();
            if(json!=null)
            {
                try
                {
                    if(json.getString("status").equalsIgnoreCase("success"))
                    {
                        //shared preferences will store the login info to stop using login page again and again
                        SharedPreferences sharedPreferences=getSharedPreferences("ProductApp",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean(LOGGED_IN,true);
                        editor.putString(MOBILE,number);
                        editor.putString(NAME,json.getString("name"));
                        editor.commit();

                        //now move to main window
                        Intent i=new Intent(getApplicationContext(),MainWindow.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Mobile number or password is incorrect.",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Bad response from server.",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to connect server.",Toast.LENGTH_LONG).show();
            }
        }
    }

}
