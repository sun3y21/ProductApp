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

public class OTPActivity extends AppCompatActivity {

    ProgressDialog progress=null;
    int TASK=0; //if task = 0 it means verification
    Uri.Builder builder=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progress=new ProgressDialog(OTPActivity.this);
        Intent i=getIntent();
        String mobile="";
        if(i!=null)
        {
            mobile=i.getStringExtra("MOBILE");
        }
        //this parameters are based upon the url of send.php which require mobilNumber and countryCode as two parameter
        builder=new Uri.Builder().appendQueryParameter("mobileNumber",mobile);
        builder.appendQueryParameter("countryCode","91");
    }

    public void resend(View view)
    {
        TASK=1;
        progress.setMessage("Resending...");
        //for local host
        //String Url="http://10.0.2.2/ProductApp/Send.php";
        //for server
        String Url="http://www.sun3y21.pe.hu/ProductApp/Send.php";
        new Connection().execute(Url);
    }

    public void verify(View view)
    {
        TASK=0;
        progress.setMessage("Verifying...");
        //for local host
        //String Url="http://10.0.2.2/ProductApp/Verification.php";
        //for remote server
        String Url="http://www.sun3y21.pe.hu/ProductApp/Verification.php";
        EditText otpText=(EditText)findViewById(R.id.otptext);
        String otp=otpText.getText().toString();
        builder.appendQueryParameter("oneTimePassword",otp);
        new Connection().execute(Url);
    }


    class Connection extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                //to decide whether it is resend or verify operation use TASK variable
                if(TASK==0)
                {
                    try
                    {
                        if(json.getString("status").equalsIgnoreCase("success"))
                        {
                            //user signed up successfuly now we can send him to home page ( for now it is login page)
                            Toast.makeText(getApplicationContext(),"User Verified Successfully.",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Wrong OTP.",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
                else
                {
                      try
                      {
                          Toast.makeText(getApplicationContext(),"OTP_SENT_SUCCESSFULLY",Toast.LENGTH_LONG).show();
                      }
                      catch (Exception e)
                      {
                          Log.e("ProductApp:",e.getMessage());
                      }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to connect server",Toast.LENGTH_LONG).show();
            }
        }
    }
}
