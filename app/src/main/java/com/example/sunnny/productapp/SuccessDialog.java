package com.example.sunnny.productapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SuccessDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_dialog);
        setTitle("Success");

        TextView t=(TextView)findViewById(R.id.successmsg);
        Intent i=getIntent();
        if (i != null) {

            t.setText("Your order for "+i.getStringExtra("name")+" has been placed succesfully.Your order id is "+i.getStringExtra("orderId"));

        }
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


    public void shopMore(View view)
    {
         finish();
    }

}
