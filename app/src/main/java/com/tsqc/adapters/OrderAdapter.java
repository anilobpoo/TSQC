package com.tsqc.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.fragments.PurchasedItems;
import com.tsqc.fragments.ViewOrder;
import com.tsqc.util.CustomFilter;
import com.tsqc.util.MyApplication;
import com.tsqc.util.MyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * Created by someo on 26-12-2016.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    public ArrayList<MyOrder> albumList;
    public ArrayList<MyOrder> albumList2;
    String userid;
    Fragment fragment;
    CustomFilter filter;
    String oid;
//    private static final String REGISTER_URL = "http://13.228.246.1/app/updatedelete.php";
    private ClipboardManager myClipboard;
    private ClipData myClip;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, ordertotal, orderno, address, mobile, email, gender, items, ddate, dtime, advance, leftover, cid;
        public ImageView overflow;
        public NetworkImageView imageView;
        ImageLoader imageLoader;
        ImageView idel,otypez;
        LinearLayout mail, call,clip;


        public MyViewHolder(View view) {
            super(view);

            imageLoader = MyApplication.getInstance().getImageLoader();
            myClipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);

            name = (TextView) view.findViewById(R.id.oname);
            orderno = (TextView) view.findViewById(R.id.oordernum);
            items = (TextView) view.findViewById(R.id.oitems);
            date = (TextView) view.findViewById(R.id.odate);
            ordertotal = (TextView) view.findViewById(R.id.osales);
            address = (TextView) view.findViewById(R.id.oaddr);
            mobile = (TextView) view.findViewById(R.id.ophone);
            email = (TextView) view.findViewById(R.id.oemail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            imageView = (NetworkImageView) view.findViewById(R.id.pImage);
            idel = (ImageView) view.findViewById(R.id.idel);
            otypez = (ImageView) view.findViewById(R.id.imageView9);

            ddate = (TextView) view.findViewById(R.id.delieverydate);
            dtime = (TextView) view.findViewById(R.id.deliverytime);
            leftover = (TextView) view.findViewById(R.id.leftover);
            advance = (TextView) view.findViewById(R.id.advpaid);

            mail = (LinearLayout) view.findViewById(R.id.linearLayout);
            call = (LinearLayout) view.findViewById(R.id.cll);
            clip = (LinearLayout) view.findViewById(R.id.clip);
            cid = (TextView) view.findViewById(R.id.odeliverynumber);

        }
    }

    public OrderAdapter(Activity mContext, ArrayList<MyOrder> albumList, Fragment fragment) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.fragment = fragment;
        this.albumList2 = albumList;

    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myorders, parent, false);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(LoginConfig.admin_id, mContext.getResources().getString(R.string.na));

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final OrderAdapter.MyViewHolder holder, final int position) {


        final MyOrder album = albumList.get(position);
        String oname = album.getName();
        String otype = album.getOtype();

        Log.e("otype",otype);
        if (otype.equals("0"))
        {
            holder.otypez.setImageResource(R.drawable.bcircle);

        }else if (otype.equals("1"))
        {
            holder.otypez.setImageResource(R.drawable.rcircle);

        }else if (otype.equals("2"))
        {
            holder.otypez.setImageResource(R.drawable.green);

        }

        String del = album.getStatus();
        if (del.equals("Not Delivered")) {
            holder.idel.setImageResource(R.drawable.cancel);
        } else {
            holder.idel.setImageResource(R.drawable.correct);
        }
        String oid = album.getOid();
        Log.e("Ada", oid);


        holder.cid.setText(album.getCid());
        holder.name.setText(oname);
        holder.date.setText(album.getOrderdate());
        holder.items.setText(album.getItems());
        holder.ordertotal.setText(album.getOrdertotal());
        //holder.email.setText(album.getEmail());
        //  holder.mobile.setText(album.getMobile());
        holder.address.setText(album.getAddr());
        holder.orderno.setText("Order #:" + album.getCid());
        String d = mContext.getString(R.string.ddate);
        Log.e("dddd", d);
        holder.ddate.setText(album.getDdate());
        holder.dtime.setText(album.getDtime());
        holder.advance.setText(album.getAdvance());
        holder.leftover.setText(album.getLeftover());

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });
        if (album.getPhoto().equals("")) {
            //holder.imageView.setDefaultImageResId(R.drawable.ic_person_black_24dp);

        } else {
            String url = album.getPhoto().replaceAll(" ", "%20");

            holder.imageView.setImageUrl("http://13.228.246.1/app/photos/" + url, holder.imageLoader);

        }

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ddd", album.getMobile());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + album.getMobile()));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(callIntent);
            }
        });

        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String to=album.getEmail();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");

                //need this to prompts email client only
                email.setType("message/rfc822");
                mContext.startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });


        holder.clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text;
                text = holder.cid.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(mContext.getApplicationContext(), "Delivery Id Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position,view));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int position;
        View view;
        public MyMenuItemClickListener(int position, View view) {
            this.position=position;
            this.view=view;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

        try{

            MyOrder album = albumList.get(position);
            oid=album.getOid();
            String oname=album.getName();
            String orderno=album.getOrderno();
            String mobi=album.getMobile();
            String ad=album.getAddr();
            String mail=album.getEmail();
            String photo=album.getPhoto();
            String gender=album.getGender();

            switch (menuItem.getItemId()) {

                case R.id.view_post:

                    FragmentManager fmq = fragment.getFragmentManager();
                    FragmentTransaction fqt = fmq.beginTransaction();
                    PurchasedItems subjectHome = new PurchasedItems();
                    Bundle args = new Bundle();
                    args.putString("orderId", album.getOid());
                    args.putString("tag", "3");
                    subjectHome.setArguments(args);
                    Log.e("aaaa",oid);
                    fqt.replace(R.id.frame, subjectHome);
                    fqt.addToBackStack(null);
                    fqt.commit();

                    return true;



                default:
            }



        }catch (Exception e)
        {
            e.printStackTrace();
        }
             return false;
    }

    }

    // Do Search...
    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(albumList2,this,fragment);
        }
        return filter;
    }









    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
