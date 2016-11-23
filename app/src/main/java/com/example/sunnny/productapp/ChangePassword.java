package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    ProgressDialog progress=null;
    Uri.Builder builder=null;
    String mobile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent i=getIntent();
        if(i!=null)
        {
            mobile=i.getStringExtra("MOBILE");
        }
    }

    public void changePass(View view)
    {
        EditText password=(EditText)findViewById(R.id.newPassword);
        String newPass=password.getText().toString();
        if(newPass.length()<6)
        {
            Toast.makeText(getApplicationContext(),"Too small password. Password must be of atleast 6 digits.",Toast.LENGTH_LONG).show();
        }
        else
        {
            //String Url="http://10.0.2.2/ProductApp/ChangePass.php";
            // for remote server
            String Url="http://www.sun3y21.pe.hu/ProductApp/ChangePass.php";
            builder=new Uri.Builder();
            builder.appendQueryParameter("mobile",mobile);
            builder.appendQueryParameter("password",newPass);
            new Connection().execute(Url);
        }
    }


    class Connection extends AsyncTask<String,String,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(ChangePassword.this);
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
                  Toast.makeText(getApplicationContext(),"Password Changed Successfully.",Toast.LENGTH_LONG).show();
                  Intent i=new Intent(getApplicationContext(),MainActivity.class);
                  startActivity(i);
                  finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to connect server",Toast.LENGTH_LONG).show();
            }
        }
    }
}
