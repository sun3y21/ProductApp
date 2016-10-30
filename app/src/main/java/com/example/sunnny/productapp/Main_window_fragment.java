package com.example.sunnny.productapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Main_window_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "MAIN_WINDOW_F";
    private static final String ARG_PARAM2 = "param2";


    ArrayList<Product> products;
    private boolean end=false;
    SearchView searchView=null;

    public Main_window_fragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_main_window_fragment, container, false);
        //search
        searchView=(SearchView)view.findViewById(R.id.search_item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //here we do the seraching work
                Log.v("ProductApp: ","Submit + "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Bundle bundle=getArguments();
        products=bundle.getParcelableArrayList("result");

        if(products==null)
        {
            products=new ArrayList<>();
            products.add(new Product());
        }
        final GridView g=(GridView)view.findViewById(R.id.productGrid);
        CustomAdapter c=new CustomAdapter(getContext(),R.id.placeHolder,products);
        g.setAdapter(c);

        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),Description.class);
                startActivity(intent);
            }
        });



        g.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isScrolled=false;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                  isScrolled=true;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if(i+i1>=i2&&!isEnd()&&isScrolled)
                {
                    setEnd(true);
                    Snackbar s=Snackbar.make(view,"Load more...",Snackbar.LENGTH_LONG);
                    View v=s.getView();
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    s.setActionTextColor(Color.WHITE);
                    TextView t=(TextView)s.getView().findViewById(android.support.design.R.id.snackbar_text);
                    t.setTextColor(Color.WHITE);
                    t.setTextSize(15);
                    s.setAction(R.string.load, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getContext(),"Now loading",Toast.LENGTH_LONG).show();
                        }
                    });
                    s.show();
                }
                else
                {
                    isScrolled=true;
                    if(i1+i<i2)
                    {
                        setEnd(false);
                    }
                }
            }

        });



        return view;
    }

    public boolean isEnd()
    {
        return end;
    }

    public void setEnd(boolean end)
    {
        this.end=end;
    }

}
