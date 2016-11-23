package com.example.sunnny.productapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Main_window_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "MAIN_WINDOW_F";
    private static final String ARG_PARAM2 = "param2";


    Uri.Builder builder=null;
    ArrayList<Product> products;
    private boolean end=false;
    SearchView searchView=null;
    CustomAdapter c=null;
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
        builder=new Uri.Builder();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //here we do the seraching work
               // String url="http://10.0.2.2/ProductApp/Search.php";
                String url="http://www.sun3y21.pe.hu/ProductApp/Search.php";
                builder=new Uri.Builder();
                builder.appendQueryParameter("keyword",query);
                searchView.setFocusable(false);
                new Connection().execute(url);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Bundle bundle=getArguments();
        if(products==null)
        products=bundle.getParcelableArrayList("result");

        if(products==null)
        {
            products=new ArrayList<>();
            products.add(new Product());
        }
        final GridView g=(GridView)view.findViewById(R.id.productGrid);
        c=new CustomAdapter(getContext(),R.id.placeHolder,products);
        g.setAdapter(c);

        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),Description.class);
                intent.putExtra("product",products.get(i));
                startActivity(intent);
            }
        });


        g.setFadingEdgeLength(0);

        g.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isScrolled=false;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                  isScrolled=true;
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                if(i>8&&i+i1>=i2&&!isEnd()&&isScrolled)
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

    class Connection extends AsyncTask<String,String,JSONObject>
    {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getContext());
            progress.setMessage("Searching...");
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
                    if(json.has("statusE"))
                    {
                        Toast.makeText(getContext(),"Invalid Search.",Toast.LENGTH_LONG).show();
                    }
                    if (json.getString("status").equalsIgnoreCase("success") && json.getString("msg").equalsIgnoreCase("result")) {
                        JSONArray jsonArray = json.getJSONArray("result");
                        ArrayList<Product> arr = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Product p = new Product();
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

                        c.clear();
                        c.addAll(arr);

                    }

                }
                catch (Exception exp)
                {
                    Toast.makeText(getContext(),"Bad response from server",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(getContext(),"Can't connect to server",Toast.LENGTH_LONG).show();
            }
        }
}


}
