package com.example.sunnny.productapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class Description extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "To write review you can use this.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Intent intent=getIntent();
        if(intent!=null)
        {
            Product p=intent.getParcelableExtra("product");

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
        Toast.makeText(getApplicationContext(),"Generate Otp and verify order.",Toast.LENGTH_LONG).show();
    }

}
