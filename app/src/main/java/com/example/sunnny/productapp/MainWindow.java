package com.example.sunnny.productapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.sunnny.productapp.MainActivity.LOGGED_IN;

public class MainWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        Intent intent=getIntent();
        ArrayList<Product> arr=intent.getParcelableArrayListExtra("result");
        if(savedInstanceState==null)
        {
            Fragment f=(Fragment)new Main_window_fragment();
            FragmentManager fm=getSupportFragmentManager();
            Bundle bundle=new Bundle();
            bundle.putParcelableArrayList("result",arr);
            f.setArguments(bundle);
            fm.beginTransaction().add(R.id.activity_main_window,f).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout_menu:
                //change the value of shared preference which is stroing the status of login
                SharedPreferences sharedPreferences=getSharedPreferences("ProductApp",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean(LOGGED_IN,false).apply();
                if(editor.commit())
                {
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(getApplicationContext(),"Logged out successfully",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Logout unsuccessfull!!!",Toast.LENGTH_LONG).show();
                }

        }
        return true;
    }

}
