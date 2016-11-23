package com.example.sunnny.productapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static com.example.sunnny.productapp.MainActivity.LOGGED_IN;

public class Description extends AppCompatActivity {


    Product p=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent=getIntent();
        if(intent!=null)
        {
            p=intent.getParcelableExtra("product");

            //set image
            ImageView imageView=(ImageView)findViewById(R.id.desciption_image);
            Picasso.with(getApplicationContext())
                    .load(p.getImageUrl()).fit()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(imageView);

            //set Product name
            TextView pName=(TextView)findViewById(R.id.des_name);
            pName.setText(p.getName());

            //set Product Price
            TextView pPrice=(TextView)findViewById(R.id.des_price);
            pPrice.setText("Price: Rs "+p.getPrice()+"");

            //set Description
            TextView pDesc=(TextView)findViewById(R.id.des_description);
            pDesc.setText(p.getDescription());


        }
    }

    public void buyNow(View view)
    {
        SharedPreferences sharedPref =getSharedPreferences("ProductApp", Context.MODE_PRIVATE);
        boolean loggedIn=sharedPref.getBoolean(LOGGED_IN,false);
        if(loggedIn)
        {
             Intent i=new Intent(getApplicationContext(),Form.class);
             i.putExtra("product",p);
             startActivity(i);
             finish();
        }
        else
        {

            Snackbar sbar=Snackbar.make(view,"Login Required.",Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.Login), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            sbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            sbar.show();
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
                else {
                    Toast.makeText(getApplicationContext(), "Logout unsuccessfull!!!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.viewProfile:
                Intent i=new Intent(getApplicationContext(),UserProfile.class);
                startActivity(i);
                break;
        }
        return true;
    }

}
