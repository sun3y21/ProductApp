package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String MOBILE = "mobile";
    final static String LOGGED_IN="loggedIn";
    final static String NAME="name";
    String number=null;
    boolean skipPressed=false;
    Uri.Builder builder=null;

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
            EditText numText=(EditText)findViewById(R.id.phoneText);
            EditText passText=(EditText)findViewById(R.id.passwordText);
            number=numText.getText().toString();
            String password=passText.getText().toString();
            if(number.length()!=10)
            {
                Toast.makeText(getApplicationContext(),"Invalid mobile number.",Toast.LENGTH_LONG).show();
            }
            else
            {
                builder=new Uri.Builder();
                builder.appendQueryParameter("mobile",number);
                builder.appendQueryParameter("password",password);
                //for localhost
                //String url="http://10.0.2.2/ProductApp/login.php";
                //for remote server
                String url="http://www.sun3y21.pe.hu/ProductApp/Login.php";
                new Connection().execute(url);
            }

        }
    }

    public void skip(View view)
    {
           builder=new Uri.Builder();
           builder.appendQueryParameter("limit","30");
           //String url="http://10.0.2.2/ProductApp/getProducts.php";
           String url="http://www.sun3y21.pe.hu/ProductApp/getProducts.php";
           new Connection().execute(url);
    }

    public void forgotPass(View view)
    {
         Intent i=new Intent(getApplicationContext(),ForgotPassword.class);
         startActivity(i);
    }


    class Connection extends AsyncTask<String,String,JSONObject>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
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
            progress.dismiss();
            if(json!=null)
            {
                try
                {
                    if(json.getString("status").equalsIgnoreCase("success")&&json.getString("msg").equalsIgnoreCase("Login success"))
                    {
                        //shared preferences will store the login info to stop using login page again and again
                        SharedPreferences sharedPreferences=getSharedPreferences("ProductApp",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean(LOGGED_IN,true);
                        editor.putString(MOBILE,number);
                        editor.putString(NAME,json.getString("name"));
                        editor.commit();
                        skip(null);
                    }
                    else if(json.getString("status").equalsIgnoreCase("success")&&json.getString("msg").equalsIgnoreCase("result"))
                    {
                        JSONArray jsonArray=json.getJSONArray("result");
                        Intent intent=new Intent(getApplicationContext(),MainWindow.class);
                        ArrayList<Product> arr=new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                            Product p=new Product();
                            p.setDescription(jsonObject.getString("description"));
                            p.setId(jsonObject.getString("id"));
                            p.setImageUrl(jsonObject.getString("url"));
                            p.setLatitude(jsonObject.getDouble("latitude"));
                            p.setLongitude(jsonObject.getDouble("longitude"));
                            p.setName(jsonObject.getString("name"));
                            p.setPrice(jsonObject.getDouble("price"));
                            p.setRating(jsonObject.getDouble("rating"));
                            p.setQuantity(jsonObject.getInt("quantity"));
                            arr.add(p);
                        }
                        intent.putParcelableArrayListExtra("result",arr);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        if(!skipPressed)
                        {
                            Toast.makeText(getApplicationContext(),"Mobile number or password is incorrect.",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Unable to fetch data.",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Bad response from server.",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to connect server.",Toast.LENGTH_LONG).show();
            }
        }
    }

}
