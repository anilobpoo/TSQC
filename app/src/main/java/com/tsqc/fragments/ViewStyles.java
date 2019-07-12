package com.tsqc.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.util.AppController;
import com.tsqc.util.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by someo on 15-09-2017.
 */

public class ViewStyles extends Fragment {


    NetworkImageView fab1image,fab2image,fab3image,fab4image,fab5image,fab6image,fab7image,fab8image,fab9image,fab10image;
    String fab1images,fab2images,fab3images,fab4images,fab5images,fab6images,fab7images,fab8images,fab9images,fab10images;
    String fab1nos,fab2nos,fab3nos,fab4nos,fab5nos,fab6nos,fab7nos,fab8nos,fab9nos,fab10nos;
    String fab1meters,fab3meters,fab4meters,fab5meters,fab6meters,fab7meters,fab8meters,fab9meters,fab10meters,fab2meters;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TextView fab1price,fab3price,fab4price,fab5price,fab6price,fab7price,fab8price,fab9price,fab10price,fab2price;
    TextView fab1meter,fab3meter,fab4meter,fab5meter,fab6meter,fab7meter,fab8meter,fab9meter,fab10meter,fab2meter;
    //private String picurl=" http://13.228.246.1/app/photos/";
    Button next;
//    String url="http://13.228.246.1/app/qc/getbyorder.php?id=";


    String id;
    public ViewStyles(String id) {

        this.id=id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_styles, container, false);

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout=(ImageView)view.findViewById(R.id.aoddd);
        imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);


        fab1image=(NetworkImageView)view.findViewById(R.id.fab1image);
        fab1price=(TextView) view.findViewById(R.id.fab1price);
        fab1meter=(TextView)view.findViewById(R.id.fab1meters);

        fab2image=(NetworkImageView)view.findViewById(R.id.fab2image);
        fab2price=(TextView) view.findViewById(R.id.fab2price);
        fab2meter=(TextView)view.findViewById(R.id.fab2meters);

        fab3image=(NetworkImageView)view.findViewById(R.id.fab3image);
        fab3price=(TextView) view.findViewById(R.id.fab3price);
        fab3meter=(TextView)view.findViewById(R.id.fab3meters);

        fab4image=(NetworkImageView)view.findViewById(R.id.fab4image);
        fab4price=(TextView) view.findViewById(R.id.fab4price);
        fab4meter=(TextView)view.findViewById(R.id.fab4meters);

        fab5image=(NetworkImageView)view.findViewById(R.id.fab5image);
        fab5price=(TextView) view.findViewById(R.id.fab5price);
        fab5meter=(TextView)view.findViewById(R.id.fab5meters);


        fab6image=(NetworkImageView)view.findViewById(R.id.fab6image);
        fab6price=(TextView) view.findViewById(R.id.fab6price);
        fab6meter=(TextView)view.findViewById(R.id.fab6meters);

        fab7image=(NetworkImageView)view.findViewById(R.id.fab7image);
        fab7price=(TextView) view.findViewById(R.id.fab7price);
        fab7meter=(TextView)view.findViewById(R.id.fab7meters);

        fab8image=(NetworkImageView)view.findViewById(R.id.fab8image);
        fab8price=(TextView) view.findViewById(R.id.fab8price);
        fab8meter=(TextView)view.findViewById(R.id.fab8meters);

        fab9image=(NetworkImageView)view.findViewById(R.id.fab9image);
        fab9price=(TextView) view.findViewById(R.id.fab9price);
        fab9meter=(TextView)view.findViewById(R.id.fab9meters);


        fab10image=(NetworkImageView)view.findViewById(R.id.fab10image);
        fab10price=(TextView) view.findViewById(R.id.fab10price);
        fab10meter=(TextView)view.findViewById(R.id.fab10meters);

        next=(Button)view.findViewById(R.id.nec);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fmq = getFragmentManager();
                FragmentTransaction fqt = fmq.beginTransaction();
                Styles subjectHome = new Styles(id);
                fqt.replace(R.id.frame, subjectHome);
                fqt.addToBackStack(null);
                fqt.commit();

            }
        });

        fetchOrders(WebUrlMethods.url_PurchasedItems+id);

        return view;

    }


    public void fetchOrders(String url)
    {
        // We first check for cached request
        Log.e("url",url);
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null)
        {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseCommentFeeds(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    url, (String)null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d("Error", "Response: " + response.toString());
                    if (response != null) {
                        parseCommentFeeds(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }
    private void parseCommentFeeds(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("Order");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                fab1nos=feedObj.getString("fabric_1");
                fab1images=feedObj.getString("fabric_image1");
                fab1meters=feedObj.getString("fab_1_meters");

                fab2nos=feedObj.getString("fabric_2");
                fab2images=feedObj.getString("fabric_image2");
                fab2meters=feedObj.getString("fab_2_meters");

                fab3nos=feedObj.getString("fabric_3");
                fab3images=feedObj.getString("fabric_image3");
                fab3meters=feedObj.getString("fab_3_meters");


                fab4nos=feedObj.getString("fabric_4");
                fab4images=feedObj.getString("fabric_image4");
                fab4meters=feedObj.getString("fab_4_meters");

                fab5nos=feedObj.getString("fabric_5");
                fab5images=feedObj.getString("fabric_image5");
                fab5meters=feedObj.getString("fab_5_meters");

                fab6nos=feedObj.getString("fabric_6");
                fab6images=feedObj.getString("fabric_image6");
                fab6meters=feedObj.getString("fab_6_meters");


                fab7nos=feedObj.getString("fabric_7");
                fab7images=feedObj.getString("fabric_image7");
                fab7meters=feedObj.getString("fab_7_meters");

                fab9nos=feedObj.getString("fabric_9");
                fab9images=feedObj.getString("fabric_image9");
                fab9meters=feedObj.getString("fab_9_meters");

                fab8nos=feedObj.getString("fabric_8");
                fab8images=feedObj.getString("fabric_image8");
                fab8meters=feedObj.getString("fab_8_meters");

                fab10nos=feedObj.getString("fabric_10");
                fab10images=feedObj.getString("fabric_image10");
                fab10meters=feedObj.getString("fab_10_meters");


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        fab1price.setText("Fabric No:"+fab1nos);
        fab1meter.setText("Fabric Meters:"+fab1meters);
        String url1=fab1images.replaceAll(" ", "%20");
        fab1image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url1,imageLoader);

        fab2price.setText("Fabric No:"+fab2nos);
        fab2meter.setText("Fabric Meters:"+fab2meters);
        String url2=fab2images.replaceAll(" ", "%20");
        fab2image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url2,imageLoader);

        fab3price.setText("Fabric No:"+fab3nos);
        fab3meter.setText("Fabric Meters:"+fab3meters);
        String url3=fab3images.replaceAll(" ", "%20");
        fab3image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url3,imageLoader);

        fab4price.setText("Fabric No:"+fab4nos);
        fab4meter.setText("Fabric Meters:"+fab4meters);
        String url4=fab4images.replaceAll(" ", "%20");
        fab4image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url4,imageLoader);

        fab5price.setText("Fabric No:"+fab5nos);
        fab5meter.setText("Fabric Meters:"+fab5meters);
        String url5=fab5images.replaceAll(" ", "%20");
        fab5image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url5,imageLoader);

        fab6price.setText("Fabric No:"+fab6nos);
        fab6meter.setText("Fabric Meters:"+fab6meters);
        String url6=fab6images.replaceAll(" ", "%20");
        fab6image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url6,imageLoader);

        fab7price.setText("Fabric No:"+fab7nos);
        fab7meter.setText("Fabric Meters:"+fab7meters);
        String url7=fab7images.replaceAll(" ", "%20");
        fab7image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url7,imageLoader);

        fab8price.setText("Fabric No:"+fab8nos);
        fab8meter.setText("Fabric Meters:"+fab8meters);
        String url8=fab8images.replaceAll(" ", "%20");
        fab8image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url8,imageLoader);

        fab9price.setText("Fabric No:"+fab9nos);
        fab9meter.setText("Fabric Meters:"+fab9meters);
        String url9=fab9images.replaceAll(" ", "%20");
        fab9image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url9,imageLoader);

        fab10price.setText("Fabric No:"+fab10nos);
        fab10meter.setText("Fabric Meters:"+fab10meters);
        String url10=fab10images.replaceAll(" ", "%20");
        fab10image.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url10,imageLoader);




    }






}
