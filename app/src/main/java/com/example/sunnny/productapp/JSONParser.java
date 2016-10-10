package com.example.sunnny.productapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sunnny on 10/10/16.
 */

public class JSONParser {

    public static JSONObject makeHttpRequest(String urlString,Uri.Builder uri)
    {
        JSONObject jsonObject=null;
        HttpURLConnection urlConnection=null;
        BufferedReader bufferedReader=null;
        try
        {
          //  Log.v("Sunnny---",uri.toString());

            URL url=new URL(urlString);
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");

            //because the method is post we need to encode the data sent with the url
            OutputStream os=urlConnection.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            String query=uri.build().getEncodedQuery();
            writer.write(query);
            writer.flush();
            //set timeout time
            urlConnection.setConnectTimeout(20000);
            //now connect
            urlConnection.connect();

            InputStream inputStream=urlConnection.getInputStream();
            if(inputStream==null)
            {
               // Log.v("Sunnny---","Error server is down");
            }
            else
            {

                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer strBuffer=new StringBuffer();
                while((line=bufferedReader.readLine())!=null)
                {
                    strBuffer.append(line);
                }

                jsonObject=new JSONObject(strBuffer.toString());
                Log.v("ProductApp",strBuffer.toString());
            }

        }catch (Exception exp)
        {
            Log.v("ProductApp",exp.getMessage()+"------");
        }
        finally
        {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(bufferedReader!=null)
            {
                try {
                    bufferedReader.close();
                }
                catch (Exception e)
                {
                    Log.e("ProductApp:",e.getMessage());
                }
            }

        }

        return jsonObject;
    }
}
