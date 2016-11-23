package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity {

    Uri.Builder builder=null;
    ProgressDialog progress=null;
    String mobile=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        progress=new ProgressDialog(ForgotPassword.this);
        setTitle("Forgot Password");
    }

    public void sendOTP(View view)
    {
        EditText mobileText=(EditText)findViewById(R.id.forgotMobile);
        mobile=mobileText.getText().toString();
        if(mobile.length()==10)
        {
           // String Url="http://10.0.2.2/ProductApp/ForgotPass.php";
            // for remote server
            String Url="http://www.sun3y21.pe.hu/ProductApp/ForgotPass.php";
            builder=new Uri.Builder();
            builder.appendQueryParameter("mobile",mobile);
            new Connection().execute(Url);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid mobile number.",Toast.LENGTH_LONG).show();
        }

    }

    class Connection extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            if(progress.isShowing())
            {
                progress.dismiss();
            }
            if(json!=null)
            {
                 try
                 {
                     if(json.getString("status").equalsIgnoreCase("Failure"))
                     {
                         Toast.makeText(getApplicationContext(),json.getString("msg"),Toast.LENGTH_LONG).show();
                     }
                     else
                     {
                         Intent i=new Intent(getApplicationContext(),OTPActivity.class);
                         i.putExtra("MOBILE",mobile);
                         i.putExtra("PARENT","ForgotPassword");
                         startActivity(i);
                         finish();
                     }
                 }
                 catch (Exception e)
                 {
                     Log.v("ProductApp: ",e.getMessage()+"----");
                 }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to connect server",Toast.LENGTH_LONG).show();
            }
        }
    }
}
