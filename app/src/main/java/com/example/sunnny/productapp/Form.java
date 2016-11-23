package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Random;

import static com.example.sunnny.productapp.MainActivity.MOBILE;

public class Form extends AppCompatActivity {

    Product p=null;
    ProgressDialog progress=null;
    Uri.Builder builder=null;
    String mobile;
    String orderId=null;

    int turn=0;// turn 0 for generate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setTitle("Buy Now");
        Intent i=getIntent();
        builder=new Uri.Builder();
        SharedPreferences sharedPref =getSharedPreferences("ProductApp", Context.MODE_PRIVATE);
        mobile=sharedPref.getString(MOBILE,"");
        if(i!=null)
        {
            p=i.getParcelableExtra("product");
            TextView pName=(TextView)findViewById(R.id.productName);
            pName.setText(p.getName());
            EditText quantity=(EditText)findViewById(R.id.quantity);
            TextView pPrice=(TextView)findViewById(R.id.price);
            pPrice.setText("Rs: "+p.getPrice());
        }


    }


    public void generateOTP(View view)
    {
        builder.appendQueryParameter("mobileNumber",mobile);
        builder.appendQueryParameter("countryCode","91");
        //String Url="http://10.0.2.2/ProductApp/send.php";
        //for server
        String Url="http://www.sun3y21.pe.hu/ProductApp/send.php";
        new Connection().execute(Url);
    }


    public void verify(View view)
    {
        turn=1;
       try
       {
           EditText addressText=(EditText)findViewById(R.id.address);
           if(addressText.getText().length()<10)
           {
               Toast.makeText(getApplicationContext(),"Please enter a valid address",Toast.LENGTH_LONG).show();
           }
           else
           {
               EditText quantityText=(EditText)findViewById(R.id.quantity);
               int q=Integer.parseInt(quantityText.getText().toString());
               if(q<=0||q>2)
               {
                   Toast.makeText(getApplicationContext(),"Quantity must be greater than 0 and smaller than equal to 2",Toast.LENGTH_LONG).show();
               }
               else
               {
                   builder.appendQueryParameter("productId",p.getId());
                   builder.appendQueryParameter("price",""+p.getPrice());
                   builder.appendQueryParameter("quantity",""+q);
                   builder.appendQueryParameter("address",addressText.getText().toString());
                   //create a order id
                   Random r=new Random();
                   long l=Math.abs(r.nextLong());
                   orderId=mobile.substring(5)+p.getId().substring(0,7)+""+l;
                   builder.appendQueryParameter("orderId",orderId);
                   EditText otpText=(EditText)findViewById(R.id.buyOtpText);
                   builder.appendQueryParameter("oneTimePassword",otpText.getText().toString());
                   //String Url="http://10.0.2.2/ProductApp/Buy.php";
                   String Url="http://www.sun3y21.pe.hu/ProductApp/Buy.php";
                   new Connection().execute(Url);

               }

           }

       }
       catch (Exception e)
       {

       }
    }


    class Connection extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(Form.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(30);
            if(turn==0)
            {
                progress.setMessage("Generating Otp..");
            }
            else
            {
                progress.setMessage("Processing order..");
            }
            progress.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            return JSONParser.makeHttpRequest(strings[0],builder);
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            super.onPostExecute(json);
            if(progress.isShowing())
            {
                progress.dismiss();
            }
            if(json!=null)
            {
                 try
                 {
                     if(turn==0&&json.getString("status").equalsIgnoreCase("success"))
                     {
                          Toast.makeText(getApplicationContext(),"OTP Sent Successfully",Toast.LENGTH_LONG).show();
                     }
                     else if(turn==1&&json.getString("status").equalsIgnoreCase("success"))
                     {
                          Intent i=new Intent(getApplicationContext(),SuccessDialog.class);
                          i.putExtra("name",p.getName());
                          i.putExtra("orderId",orderId);
                          startActivity(i);
                          finish();
                     }
                 }
                 catch (Exception e)
                 {
                     Log.v("ProductApp:",e.getMessage()+"*****----");
                 }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to connect server",Toast.LENGTH_LONG).show();
            }
        }
    }

}
