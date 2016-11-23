package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONObject;

public class SignUpPage extends AppCompatActivity {

    Uri.Builder builder=null;
    String mobile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

    }

    public void SignUp(View view)
    {
        EditText nameText=(EditText)findViewById(R.id.name_text);
        EditText mobileText=(EditText)findViewById(R.id.signup_mobile);
        EditText passwordText=(EditText)findViewById(R.id.signUp_password);
        EditText repasswordText=(EditText)findViewById(R.id.signup_re_enterPass);
        RadioButton maleB=(RadioButton)findViewById(R.id.male_rb);
        RadioButton femaleB=(RadioButton)findViewById(R.id.female_rb);
        String gender=maleB.isChecked()==true?"M":"F";

        //find out all the value
        String name=nameText.getText().toString();
               mobile=mobileText.getText().toString();
        String password=passwordText.getText().toString();
        String rePassword=repasswordText.getText().toString();

        //flag for correct input
        boolean flag=true;

        //first check name its length must me greater than 3
        if(name.length()<3)
        {
            Toast.makeText(getApplicationContext(),"Invalid Name",Toast.LENGTH_LONG).show();
            flag=false;
        }
        //password length must be greater than 6

        if(password.length()<6&&flag)
        {
            Toast.makeText(getApplicationContext(),"Password must be of at least 6 characters",Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(!maleB.isChecked()&&!femaleB.isChecked()&&flag)
        {
            Toast.makeText(getApplicationContext(),"Gender must be selected",Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(mobile.length()<10&&flag)
        {
            Toast.makeText(getApplicationContext(),"Invalid Mobile Number",Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(!rePassword.equals(password))
        {
            Toast.makeText(getApplicationContext(),"Re-entered password do not match.",Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(flag)
        {
            //for local host
           // String url="http://10.0.2.2/ProductApp/SignUp.php";
            //for remote server
            String url="http://www.sun3y21.pe.hu/ProductApp/SignUp.php";
            builder=new Uri.Builder().appendQueryParameter("name",name);
            builder.appendQueryParameter("password",password);
            builder.appendQueryParameter("gender",gender);
            builder.appendQueryParameter("mobile",mobile).build();
            new Connection().execute(url);
        }

    }

    class Connection extends AsyncTask<String,String,JSONObject>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(SignUpPage.this);
            progress.setMessage("Connecting...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }



        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject json=JSONParser.makeHttpRequest(strings[0],builder);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json)
        {
            super.onPostExecute(json);
            if(progress.isShowing())
                progress.dismiss();
            if(json!=null)
            {
               try
               {

                   if(json.getString("status").equalsIgnoreCase("success"))
                   {
                       Intent i=new Intent(getApplicationContext(),OTPActivity.class);
                       i.putExtra("MOBILE",mobile);
                       i.putExtra("PARENT","SignUpPage");
                       startActivity(i);
                       finish();
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(),"Number already registered.",Toast.LENGTH_LONG).show();
                   }
               }
               catch (Exception e)
               {
                   Toast.makeText(getApplicationContext(),"Bad response from server",Toast.LENGTH_LONG).show();
               }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Can't connect to server",Toast.LENGTH_LONG).show();
            }
        }
    }


}
