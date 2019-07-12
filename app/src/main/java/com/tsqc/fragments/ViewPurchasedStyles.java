package com.tsqc.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.util.AppController;
import com.tsqc.util.MyOrder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by someo on 29-03-2018.
 */

public class ViewPurchasedStyles extends Fragment {

    LinearLayout linearLayout;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2, mly, con, sty, extraitemlin;
    TextView fab;
    String details_error;
    CheckBox checkBox;
    NetworkImageView extraitem, extrainfo;
    TextView extraitemname, extrainfoname;
    TextView oextraitemname, oextraitemqty, oextraitemprice;
    CheckBox checkBox1, checkBox2;
    LinearLayout extrait, extrain, oextrait;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String cid, userid, orderid;
    Button derror, dcorrect;
    String noerror = "No Error";
    @SuppressLint("ValidFragment")
    int other_extra_item;


    RadioGroup rd, rd1;

    public ViewPurchasedStyles(String cid, String orderid) {
        this.cid = cid;
        this.orderid = orderid;
    }

    public ViewPurchasedStyles() {

    }

    String username;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpurchased_styles_vertical, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(LoginConfig.admin_id, "Not Available");
        username = sharedPreferences.getString(LoginConfig.admin_name, "Not Available");
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout = (ImageView) view.findViewById(R.id.rtsy);
        imageLoader.displayImage("drawable://" + R.drawable.bg, relativeLayout);
        linearLayout = (LinearLayout) view.findViewById(R.id.llu);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.llu1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.lluff2);
        oextrait = (LinearLayout) view.findViewById(R.id.oeitd);
        mly = (LinearLayout) view.findViewById(R.id.mly);
        extrain = (LinearLayout) view.findViewById(R.id.extrain);
        extrait = (LinearLayout) view.findViewById(R.id.extrait);

        dcorrect = view.findViewById(R.id.dcorrect);
        dcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                correct();
            }
        });
        derror = view.findViewById(R.id.derror);
        derror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i =0;i<linearLayout1.getChildCount();i++){
                    View view_fab = (View)linearLayout1.getChildAt(i);
                    RadioGroup rg_fab =(RadioGroup)view_fab.findViewWithTag(i);
                    if(rg_fab.getCheckedRadioButtonId() == -1){
                        Toast.makeText(getActivity(),"Check Purchase fabric value",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for(int j =0;j<linearLayout.getChildCount();j++){
                    View view_style =(View)linearLayout.getChildAt(j);
                    RadioGroup rg_style =(RadioGroup)view_style.findViewWithTag(j);
                    if(rg_style.getCheckedRadioButtonId()==-1){
                        Toast.makeText(getActivity(),"Check Purchase Style value",Toast.LENGTH_SHORT).show();
                    return;
                    }
                }
                for(int k =0;k<linearLayout2.getChildCount();k++){
                    View view_contrast =(View)linearLayout2.getChildAt(k);
                    RadioGroup rg_contrast =(RadioGroup)view_contrast.findViewWithTag(k);
                    if(rg_contrast.getCheckedRadioButtonId()==-1){
                        Toast.makeText(getActivity(),"Check Purchase contrast value",Toast.LENGTH_SHORT).show();
                    return;
                    }
                }
                for(int k =0;k<mly.getChildCount();k++){
                    View view_measurement =(View)mly.getChildAt(k);
                    RadioGroup rg_measurement =(RadioGroup)view_measurement.findViewWithTag(k);
                    if(rg_measurement.getCheckedRadioButtonId()==-1){
                        Toast.makeText(getActivity(),"Check Measurement value",Toast.LENGTH_SHORT).show();
                   return;
                    }
                }
             /*  if(extraitemlin.getChildCount()>0) {
                   for (int k = 0; k < extraitemlin.getChildCount(); k++) {
                       View view_extra = (View) extraitemlin.getChildAt(k);
                       RadioGroup rg_extra = (RadioGroup) view_extra.findViewWithTag(k);
                       if (rg_extra.getCheckedRadioButtonId() == -1) {
                           Toast.makeText(getActivity(), "Check ExtraItems value", Toast.LENGTH_SHORT).show();

                           return;
                       }
                   }
               }
               if(oextrait.getChildCount()>0) {
                   for (int k = 0; k < oextrait.getChildCount(); k++) {
                       View view_oextra = (View) oextrait.getChildAt(k);
                       RadioGroup rg_oextra = (RadioGroup) view_oextra.findViewWithTag(k);
                       if (rg_oextra.getCheckedRadioButtonId() == -1) {
                           Toast.makeText(getActivity(), "Check other extraItems value", Toast.LENGTH_SHORT).show();
                           return;
                       }
                   }
               }*/

                sendEmail();
            }
        });


        rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd1 = (RadioGroup) view.findViewById(R.id.radgrp1);


        checkBox = view.findViewById(R.id.checkbox1);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "0";
                    error(type, "extra item");
                }
            }
        });


        checkBox = view.findViewById(R.id.checkbox2);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "1";
                    error(type, "extra info");
                }
            }
        });


        con = (LinearLayout) view.findViewById(R.id.con);
        sty = (LinearLayout) view.findViewById(R.id.sty);
        extraitemlin = (LinearLayout) view.findViewById(R.id.extraitemlin);
        extrainfo = (NetworkImageView) view.findViewById(R.id.extrainfo);
        extrainfoname = (TextView) view.findViewById(R.id.extrainfoname);
        extraitemname = (TextView) view.findViewById(R.id.extraitemname);
        fab = (TextView) view.findViewById(R.id.fab);
        extraitem = (NetworkImageView) view.findViewById(R.id.extraitem);
        linearLayout.removeAllViews();
        linearLayout1.removeAllViews();
        linearLayout2.removeAllViews();
        oextrait.removeAllViews();
        fetchData(WebUrlMethods.getpurchaseditem + cid, view);

        return view;
    }


    public void fetchData(String url, final View view) {
        Log.e("url_fetchdetails", url);
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data), view);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {

            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    url, (String) null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d("PICS", "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response, view);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("PICS", "Error: " + error.getMessage());

                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    private void parseJsonFeed(JSONObject response, final View view) {
        String id = null;

        try {

            JSONArray feedArray = response.getJSONArray("Purchased Items");
            if (feedArray == null || feedArray.length() == 0) {


            } else {
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    String title = feedObj.optString("Styles");
                    if (title.equals("null")) {
                        sty.setVisibility(View.GONE);

                    } else {
                        sty.setVisibility(View.VISIBLE);
                        parseStyles(title);
                    }


                    String extraitemDetail = feedObj.optString("extraitems");
                    Log.i("extraitemDetail", extraitemDetail);

                    if (extraitemDetail != null) {
                        Log.i("PresentStatus", "absent");

                        extraitemlin.setVisibility(View.GONE);
                    } else {


                        Log.i("PresentStatus", "Present");
                        extraitemlin.setVisibility(View.VISIBLE);
                        parseExtraItem(extraitemDetail);

                    }

                    String fabric = "{\\\"Fabric\\\":" + feedObj.optString("Fabric") + "}";
                    Log.e("Fabric", fabric);


                    String contrast = feedObj.optString("Contrast");
                    if (contrast.equals("null")) {
                        con.setVisibility(View.GONE);
                    } else {
                        con.setVisibility(View.VISIBLE);
                        parseContrast(contrast);
                    }

                    String extra_info = feedObj.optString("extra_info");
                    String button_image = feedObj.getString("button_image");
                    String stylus_image = feedObj.getString("stylus_image");
                    String button_name = feedObj.optString("button_name");
                    String buttonurl = WebUrlMethods.picurl_ViewOrder + feedObj.getString("button_image");
                    String stylusurl = WebUrlMethods.picurl_ViewOrder + feedObj.getString("stylus_image");


                    if ((extra_info.equals("empty") || extra_info.equals("") || extra_info.equals("null") || extra_info.equals("Empty") || extra_info.equals("EMPTY")) && (stylus_image.equals("null") || stylus_image.equals("EMPTY") || stylus_image.equals("empty") || stylus_image.equals("") || stylus_image.equals("Empty"))) {
                        extrain.setVisibility(View.GONE);
                    } else {
                        extrainfoname.setText(extra_info);
                        extrainfo.setImageUrl(stylusurl, imageLoader);


                        final JSONObject jobj = new JSONObject();
                        try {
                            jobj.put("name", extra_info);
                            jobj.put("image", stylusurl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                        // RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                final RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                                // get RadioButton text
                                String yourVote = selectedrdBtn.getText().toString();
                                if (yourVote.equals("Ok")) {


                                    final String type = "8";
                                    error_actualdata(type, noerror, jobj.toString());

                                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                                } else {


                                    final String type = "8";
                                    error_actualdata(type, extrainfoname.getText() + "", jobj.toString());

                                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                                }


                            }
                        });


                    }

                    String other_extraitems = feedObj.optString("other_extraitems");
                    Log.i("other_extraitems", other_extraitems);
                    if (other_extraitems != null) {
                        other_extra_item = 0;
                        Log.i("PresentExtraStatus", "absent");
                        oextrait.setVisibility(View.GONE);
                    } else {
                        other_extra_item = 1;
                        Log.i("PresentExtraStatus", "present");
                        oextrait.setVisibility(View.VISIBLE);

                    }
                    String fabric_item = feedObj.optString("other_fabric_info");


                    if ((button_image.equals("empty") || button_image.equals("") || button_image.equals("null") || button_image.equals("EMPTY") || button_image.equals("Empty")) && (button_name.equals("null") || button_name.equals("EMPTY") || button_name.equals("empty") || button_name.equals("") || button_name.equals("Empty"))) {
                        extrait.setVisibility(View.GONE);
                    } else {
                        extraitemname.setText(button_name);
                        extraitem.setImageUrl(buttonurl, imageLoader);


                        final JSONObject jobj = new JSONObject();
                        try {
                            jobj.put("name", button_name);
                            jobj.put("image", buttonurl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                        // RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                        rd1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                final RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                                // get RadioButton text
                                String yourVote = selectedrdBtn.getText().toString();
                                if (yourVote.equals("Ok")) {


                                    final String type = "1";
                                    error_actualdata(type, noerror, jobj.toString());

                                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                                } else {


                                    final String type = "1";
                                    error_actualdata(type, extrainfoname.getText() + "", jobj.toString());

                                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                                }


                            }
                        });


                    }
                    String measurementDetail = feedObj.optString("Measure").replaceAll("[{}]", " ");
                    parseMeasurement(measurementDetail);
                    //parseFabrics(fabric);
                    parseFabrics(fabric_item);
                    //parseOtherExtraItem(other_extraitems);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseOtherFabricItem(String response) {


        response = response.replace("\\", "");
        Log.e("ResposeFabrics", response);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            JSONArray array = jsonObj.getJSONArray("FabricExtraDetail");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonobj = (JSONObject) array.get(i);
                String name = jsonobj.getString("name");
                String qty = jsonobj.getString("quantity");
                String price = jsonobj.getString("price");
                initializeOtherExtraitem(name, qty, price,i);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseOtherExtraItem(String response) {


        response = response.replace("\\", "");
        Log.e("ResponseExtraitem", response);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            JSONArray array = jsonObj.getJSONArray("extraitem");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonobj = (JSONObject) array.get(i);
                String name = jsonobj.getString("name");
                String qty = jsonobj.getString("quantity");
                String price = jsonobj.getString("price");
                initializeOtherExtraitem(name, qty, price,i);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeOtherExtraitem(final String name, final String qty, final String price,int tag) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_extraitem, null);
        // NetworkImageView imageStyles = view.findViewById(R.id.stylesimage);
        //TextView nameStyles = view.findViewById(R.id.nameStyles);
        checkBox = view.findViewById(R.id.ocheckbox1);

        oextraitemname = (TextView) view.findViewById(R.id.oextraitemname);
        oextraitemqty = (TextView) view.findViewById(R.id.oextraitemqty);
        oextraitemprice = (TextView) view.findViewById(R.id.oextraitemprice);
        RadioGroup rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd.setTag(tag);


        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("name", name);
            jobj.put("qty", qty);
            jobj.put("Price", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                // get RadioButton text
                String yourVote = selectedrdBtn.getText().toString();
                if (yourVote.equals("Ok")) {


                    final String type = "5";
                    error_actualdata(type, noerror, jobj.toString());

                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                } else {


                    final String type = "5";
                    error_actualdata(type, name, jobj.toString());

                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                }


            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("name", name);
                        jobj.put("qty", qty);
                        jobj.put("Price", price);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String type = "5";
                    error(type, name);
                }
            }
        });

        oextraitemname.setText(name);
        oextraitemqty.setText(qty);
        oextraitemprice.setText(price);
        if (other_extra_item == 1) {
            oextrait.addView(view);
        } else {

        }
    }

    private void parseMeasurement(String measurementDetail) {


        String s = measurementDetail.replaceAll("\"", "");
        String result[] = s.split(",");
        for (int i = 0; i < result.length; i++) {
            initializeMeasurment(result[i],i);
        }


    }

    private void parseFabrics(String response) {
        response = response.replace("\\", "");
        Log.e("ResposeFabrics", response);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            // JSONObject  jsonObj1 = jsonObj.getJSONObject("FabricExtraDetail");
            //JSONObject  jsonObj2 = jsonObj.getJSONObject("other_fabric_info");

            JSONArray array = jsonObj.getJSONArray("FabricExtraDetail");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonobj = (JSONObject) array.get(i);
                String fabimage = jsonobj.getString("fabimage");
                String url = WebUrlMethods.picurl_ViewOrder + jsonobj.getString("fabimage").trim();
                String url1 = url.replace(" ", "%20");
                String names = jsonobj.getString("Fabricnum");
                String extrainfo = jsonobj.getString("extraInfo");
                //initializeFabrics(url1,names);
                if (fabimage.equals("EMPTY") && names.equals("EMPTY")) {

                } else {
                    initializeFabrics(url1, names, extrainfo,i);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseStyles(String response) {
        Log.e("ResposeStyles", response);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            JSONArray array = jsonObj.getJSONArray("styles");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonobj = (JSONObject) array.get(i);
                String url = WebUrlMethods.picurl_FabricAdapter + jsonobj.getString("pic") + ".png";
                String url1 = url.replace(" ", "%20");

                String names = jsonobj.getString("name");


                String fabid = jsonobj.getString("fabid");
                String stylename = jsonobj.getString("namestyle");
                String fabno = jsonobj.getString("fabno");
                String faburl = WebUrlMethods.picurl_ViewOrder + jsonobj.getString("fabimage").trim();
                String imagefab = faburl.replace(" ", "%20");

                initializeStyles(url1, names, stylename, imagefab, fabno,i);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseExtraItem(String response) {
        Log.e("parseExtraItem", response);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            JSONArray array = jsonObj.getJSONArray("otherExtraItems");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonobj = (JSONObject) array.get(i);


                String names = jsonobj.getString("name");


                String quantity = jsonobj.getString("quantity");
                String totalprice = jsonobj.getString("totalprice");
                String imageurl = WebUrlMethods.picurl_ViewOrder + jsonobj.getString("image").trim();
                String url = imageurl.replace(" ", "%20");

                initializExtraitem(url, names, quantity, totalprice,i);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseContrast(String response) {
        // response = response.replace("\\", "");
        Log.e("ResposeContrast", response);

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response);
            JSONArray array = jsonObj.getJSONArray("contrast");
            for (int i = 0; i < array.length(); i++) {

                JSONObject jsonobj = (JSONObject) array.get(i);
                String name = jsonobj.getString("contrastname");
                String url = WebUrlMethods.picurl_FabricAdapter + jsonobj.getString("contrastpic") + ".png";

                String fabid = jsonobj.getString("fabid");
                String fabno = jsonobj.getString("fabno");
                String faburl = WebUrlMethods.picurl_ViewOrder + jsonobj.getString("fabimage").trim();
                String imagefab = faburl.replace(" ", "%20");


                String image = url.replace(" ", "%20");
                Log.e("oage", image);
                initializeContrast(image, name, fabid, imagefab, fabno,i);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initializeStyles(String image, final String name, String fabid, final String fabUrl, String fabNo,int tag) {
        Log.i("Faburl",fabUrl);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_styles, null);
        NetworkImageView imageStyles = view.findViewById(R.id.stylesimage);
        TextView nameStyles = view.findViewById(R.id.nameStyles);

        NetworkImageView FabricStyles = view.findViewById(R.id.fabricImage);
        TextView nameFabric = view.findViewById(R.id.FabricName);

        FabricStyles.setImageUrl(fabUrl, imageLoader);
        nameFabric.setText(fabid);


        RadioGroup rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd.setTag(tag);


        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("stylename", name);
            jobj.put("styleimage", image);
            jobj.put("fabimage", fabUrl);
            jobj.put("fabid", fabid);
            jobj.put("fabno", fabNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                // get RadioButton text
                String yourVote = selectedrdBtn.getText().toString();
                if (yourVote.equals("Ok")) {


                    final String type = "2";
                    error_actualdata(type, noerror, jobj.toString());

                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                } else {


                    final String type = "2";
                    error_actualdata(type, name, jobj.toString());

                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                }


            }
        });
        checkBox = view.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "2";
                    error(type, name);
                }
            }
        });

        imageStyles.setImageUrl(image, imageLoader);
        nameStyles.setText(name);
        linearLayout.addView(view);
    }


    private void initializExtraitem(String image, final String name, String quantity, String totalprice,int tag) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_style_extraitem, null);
        NetworkImageView imageStyles = view.findViewById(R.id.stylesimage);
        TextView nameStyles = view.findViewById(R.id.nameextraitem);

        NetworkImageView FabricStyles = view.findViewById(R.id.fabricImage);
        TextView nameFabric = view.findViewById(R.id.priceextraitem);

        nameFabric.setText(name);


        RadioGroup rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd.setTag(tag);


        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("eitemname", name);
            jobj.put("eitemimage", image);
            jobj.put("price", totalprice);
            jobj.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                // get RadioButton text
                String yourVote = selectedrdBtn.getText().toString();
                if (yourVote.equals("Ok")) {


                    final String type = "2";
                    error_actualdata(type, noerror, jobj.toString());

                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                } else {


                    final String type = "2";
                    error_actualdata(type, name, jobj.toString());

                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                }


            }
        });
        checkBox = view.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "2";
                    error(type, name);
                }
            }
        });

        imageStyles.setImageUrl(image, imageLoader);
        nameStyles.setText(totalprice);
        extraitemlin.addView(view);
    }

    private void initializeFabrics(String image, final String name, String extrainfo,int tagid) {
        Log.e("1", name);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_styles, null);
        NetworkImageView imageStyles = view.findViewById(R.id.stylesimage);
        TextView nameStyles = view.findViewById(R.id.nameStyles);
        TextView FabricName = view.findViewById(R.id.FabricName);

        imageStyles.setImageUrl(image, imageLoader);
        nameStyles.setText(name);
        FabricName.setText(extrainfo);

        RadioGroup rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd.setTag(tagid);
        Log.e("1", name);

        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("name", name);
            jobj.put("image", image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("1", name);
        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                // get RadioButton text
                String yourVote = selectedrdBtn.getText().toString();
                if (yourVote.equals("Ok")) {


                    final String type = "8";
                    error_actualdata(type, noerror, jobj.toString());

                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                } else {


                    final String type = "8";
                    error_actualdata(type, name, jobj.toString());

                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                }


            }
        });
        checkBox = view.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "3";
                    error(type, name);
                }
            }
        });
        imageStyles.setImageUrl(image, imageLoader);
        nameStyles.setText(name);
        Log.e("1", name);
        linearLayout1.addView(view);
    }

    private void initializeContrast(String image, final String name, String fabid, final String fabUrl, String fabNo,int tag) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_contrast, null);
        NetworkImageView imageStyles = view.findViewById(R.id.stylesimage);
        TextView nameStyles = view.findViewById(R.id.nameStyles);
        NetworkImageView FabricStyles = view.findViewById(R.id.fabricImage);
        TextView nameFabric = view.findViewById(R.id.FabricName);

        FabricStyles.setImageUrl(fabUrl, imageLoader);
        nameFabric.setText(fabid);

        imageStyles.setImageUrl(image, imageLoader);
        nameStyles.setText(name);


        RadioGroup rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd.setTag(tag);


        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("stylename", name);
            jobj.put("stleimage", image);
            jobj.put("fabimage", fabUrl);
            jobj.put("fabid", fabid);
            jobj.put("fabno", fabNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                // get RadioButton text
                String yourVote = selectedrdBtn.getText().toString();
                if (yourVote.equals("Ok")) {


                    final String type = "3";
                    error_actualdata(type, noerror, jobj.toString());

                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                } else {


                    final String type = "3";
                    error_actualdata(type, name, jobj.toString());

                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                }


            }
        });
        checkBox = view.findViewById(R.id.checkbox);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "4";
                    error(type, name);
                }
            }
        });

        linearLayout2.addView(view);
    }

    private void initializeMeasurment(final String key,int tag) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.layout_pass_details, null);
        TextView keys = view.findViewById(R.id.pdesc);
        TextView values = view.findViewById(R.id.pamount);

        RadioGroup rd = (RadioGroup) view.findViewById(R.id.radgrp);
        rd.setTag(tag);


        final JSONObject jobj = new JSONObject();
        try {
            jobj.put("name", key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton ok = (RadioButton) view.findViewById(R.id.ok);
                RadioButton notok = (RadioButton) view.findViewById(R.id.notok);


                RadioButton selectedrdBtn = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                // get RadioButton text
                String yourVote = selectedrdBtn.getText().toString();
                if (yourVote.equals("Ok")) {


                    final String type = "4";
                    error_actualdata(type, noerror, jobj.toString());

                    //Toast.makeText(getActivity(),"Ok",Toast.LENGTH_LONG).show();
                } else {


                    final String type = "4";
                    error_actualdata(type, key, jobj.toString());

                    // Toast.makeText(getActivity(),"Not Ok",Toast.LENGTH_LONG).show();
                }


            }
        });


        checkBox = view.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final String type = "5";
                    error(type, key);
                }
            }
        });
        values.setVisibility(View.GONE);
        if (key.isEmpty()) {
            keys.setVisibility(View.GONE);
        } else {
            keys.setText(key);
        }
        mly.addView(view);
    }


    public void error(final String type, final String errormessage) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.uprompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.edittTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String errorDesc = userInput.getText().toString();
                                sendError(type, errormessage, errorDesc);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // checkBox.setChecked(false);
                                dialog.cancel();


                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    public void sendError(final String type, final String errormessage, final String errorDesc) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.mesurl_ViewOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);

                        try {
                            JSONObject jobj = new JSONObject(response);

                            String id = jobj.getString("id");
                            String message = jobj.getString("message");
                            String status = jobj.getString("status");


                            if (status.equals("success")) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                // Log.e("qccheck_id", "150");
                Log.e("order_id", orderid);
                Log.e("order_item_id", cid);
                Log.e("qc_admin_id", userid);
                Log.e("error_type", type);
                Log.e("error_message", errormessage);
                Log.e("error_description", errorDesc);
                Log.e("qc_status", "0");
                Log.e("qc_delete", "0");

                //params.put("qccheck_id", "150");
                params.put("order_id", orderid);
                params.put("order_item_id", cid);
                params.put("qc_admin_id", userid);
                params.put("error_type", type);
                params.put("error_message", errormessage);
                params.put("error_description", errorDesc);
                params.put("qc_status", "0");
                params.put("qc_delete", "0");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void correct() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.oupdate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        if (response.trim().equals("success")) {
                            Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

                        } else if (response.equals("fail")) {
                            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oid", orderid);
                params.put("qc_id", userid);
                params.put("qc_status", "Verified");
                params.put("qc_name", username);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public void sendEmail() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.qcmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.trim().equals("success")) {
                                Toast.makeText(getActivity(), "Quality Check Report Submited sucessfully. Mail Sent...", Toast.LENGTH_SHORT).show();


                                FragmentManager fmq = getFragmentManager();
                                FragmentTransaction fqt = fmq.beginTransaction();
                                MyOrders subjectHome = new MyOrders();
                                fqt.replace(R.id.frame, subjectHome);
                                fqt.addToBackStack(null);
                                fqt.commit();
                            } else if (status.equals("fail")) {
                                Toast.makeText(getActivity(), "An Error has been occured...", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("id", orderid);
                Log.e("Url", WebUrlMethods.qcmail);
                Log.e("OrderItemId", cid);
                params.put("id", cid);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public void error_actualdata(final String type, final String errormessage, final String actualdata) {


        if (errormessage.equals(noerror)) {
            sendError_actualdata(type, errormessage, "No Error", actualdata);
        } else {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.uprompts, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.edittTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String errorDesc = userInput.getText().toString();
                                    sendError_actualdata(type, errormessage, errorDesc, actualdata);
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // checkBox.setChecked(false);
                                    dialog.cancel();


                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }


    }


    public void sendError_actualdata(final String type, final String errormessage, final String errorDesc, final String actualdata) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.mesurl_ViewOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);

                        try {
                            JSONObject jobj = new JSONObject(response);

                            String id = jobj.getString("id");
                            String message = jobj.getString("message");
                            String status = jobj.getString("status");


                            if (status.equals("success")) {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                // Log.e("qccheck_id", "150");
                Log.e("order_id", orderid);
                Log.e("order_item_id", cid);
                Log.e("qc_admin_id", userid);
                Log.e("error_type", type);
                Log.e("error_message", errormessage);
                Log.e("error_description", errorDesc);
                Log.e("qc_status", "0");
                Log.e("qc_delete", "0");
                Log.e("actual_data", actualdata);

                //params.put("qccheck_id", "150");
                params.put("order_id", orderid);
                params.put("order_item_id", cid);
                params.put("qc_admin_id", userid);
                params.put("error_type", type);
                params.put("error_message", errormessage);
                params.put("error_description", errorDesc);
                params.put("actual_data", actualdata);
                params.put("qc_status", "0");
                params.put("qc_delete", "0");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}




