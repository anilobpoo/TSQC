package com.tsqc.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.util.AppController;
import com.tsqc.util.FabricItem;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by someo_000 on 01-04-2016.
 */
public class FabricAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<FabricItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Fragment fragment;
    FabricItem item;
   // private String picurl="http://13.228.246.1/app/menu/";

    public FabricAdapter(Activity activity, List<FabricItem> feedItems, Fragment fragment) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.fragment=fragment;
    }



    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int position) {
        return feedItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_fabric, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        item = feedItems.get(position);

        TextView name = (TextView) convertView.findViewById(R.id.fabno);
        name.setText(item.getName());
        NetworkImageView fab=(NetworkImageView)convertView.findViewById(R.id.fImage);
        String url=item.getPic().replaceAll(" ", "%20");
        fab.setImageUrl(WebUrlMethods.picurl_FabricAdapter+url+".png",imageLoader);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ImageView del=(ImageView)convertView.findViewById(R.id.fdelete);
        del.setVisibility(View.GONE);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }



}
