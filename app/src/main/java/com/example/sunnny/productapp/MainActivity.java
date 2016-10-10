package com.example.sunnny.productapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            Toast.makeText(getApplicationContext(),"for login request to server.",Toast.LENGTH_LONG).show();
        }
    }
}
