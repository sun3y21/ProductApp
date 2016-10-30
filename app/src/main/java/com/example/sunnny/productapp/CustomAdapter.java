package com.example.sunnny.productapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sunnny on 21/10/16.
 */

public class CustomAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> arr=null;

    public CustomAdapter(Context context, int resource, ArrayList<Product> objects) {
        super(context, resource, objects);
        arr=objects;
    }


    @Override
    public int getCount() {
        return arr.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.grid_placeholder,null);
        }

        ImageView imageView=(ImageView)convertView.findViewById(R.id.productImage);
        imageView.setMaxHeight(150);

        TextView productName=(TextView)convertView.findViewById(R.id.productName);
        productName.setText(arr.get(position).getName());

        TextView productPrice=(TextView)convertView.findViewById(R.id.productPrice);
        productPrice.setText("Rs: "+arr.get(position).getPrice());

        Picasso.with(getContext())
                .load(arr.get(position).getImageUrl()).fit()
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imageView);

        return convertView;
    }
}
