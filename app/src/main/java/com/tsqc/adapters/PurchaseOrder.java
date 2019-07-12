package com.tsqc.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.fragments.ViewPurchasedStyles;
import com.tsqc.fragments.ViewStyles;
import com.tsqc.util.AppController;
import com.tsqc.util.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by someo_000 on 01-04-2016.
 */
public class PurchaseOrder extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<OrderItem> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Fragment fragment;
    OrderItem item;
    String orderId;
    //private String picurl=" http://13.228.246.1/app/photos/";
    //private static final String REGISTER_URL ="http://13.228.246.1/app/deleteitem.php" ;
    ArrayList<String> piclist;

    public PurchaseOrder(Activity activity, List<OrderItem> feedItems, Fragment fragment,String orderId) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.fragment=fragment;
        this.orderId=orderId;
    }

    String scurrency;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_purchase, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        item = feedItems.get(position);
        piclist=new ArrayList<>();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        scurrency = sharedPreferences.getString(LoginConfig.currency, "Not Available");


        TextView name = (TextView) convertView.findViewById(R.id.pname);
        name.setText(item.getName());
        TextView qty = (TextView) convertView.findViewById(R.id.pqty);
        qty.setText("QTY: "+item.getQty());
        TextView price = (TextView) convertView.findViewById(R.id.pprice);
        price.setText("Includes Making Price: "+item.getPrice());
        TextView total = (TextView) convertView.findViewById(R.id.ptotal);
        total.setText(scurrency+item.getSubtotal());

        final String cid= item.getId();
        final String ordierid=item.getCid();
        Log.e("cid",cid);
        Log.e("ordierid",ordierid);

        if(item.getType().equals("2")){

        }

        ImageView down=(ImageView)convertView.findViewById(R.id.down);
        if(item.getQc_check()== null){
            down.setEnabled(true);
            down.setVisibility(View.VISIBLE);
        }
        if(item.getQc_check()=="1"){
            down.setEnabled(false);
            down.setVisibility(View.GONE);
            Toast.makeText(activity,"AlreadyChecked",Toast.LENGTH_SHORT).show();
        }
        down.setTag(position);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fmq = fragment.getFragmentManager();
                FragmentTransaction fqt = fmq.beginTransaction();
                ViewPurchasedStyles subjectHome = new ViewPurchasedStyles (cid,orderId);
                Log.e("aaaa",item.getId());
                fqt.replace(R.id.frame, subjectHome);
                fqt.addToBackStack(null);
                fqt.commit();
            }
        });
        return convertView;
    }



}
