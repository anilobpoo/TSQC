package com.tsqc.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.widget.Filter;


import com.tsqc.LoginConfig;
import com.tsqc.adapters.OrderAdapter;

import java.util.ArrayList;

/**
 * Created by Hp on 3/17/2016.
 */
public class CustomFilter extends Filter {

    OrderAdapter adapter;
    ArrayList<MyOrder> filterList;
    String type;
    Fragment fragment;

    public CustomFilter(ArrayList<MyOrder> filterList, OrderAdapter adapter, Fragment fragment)
    {
        this.adapter=adapter;
        this.filterList=filterList;
        this.fragment=fragment;
        SharedPreferences sharedPreferences = fragment.getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        type = sharedPreferences.getString(LoginConfig.type, "Not Available");

    }



    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results=new FilterResults();

        if (type.equals("1"))
        {
            //CHECK CONSTRAINT VALIDITY
            if(constraint != null && constraint.length() > 0)
            {
                //CHANGE TO UPPER
                constraint=constraint.toString().toUpperCase();
                //STORE OUR FILTERED PLAYERS
                ArrayList<MyOrder> filteredPlayers=new ArrayList<>();

                for (int i=0;i<filterList.size();i++)
                {
                    //CHECK
                    if(filterList.get(i).getName().toUpperCase().contains(constraint)||filterList.get(i).getOrderno().toUpperCase().contains(constraint)||
                            filterList.get(i).getAddr().toUpperCase().contains(constraint)||filterList.get(i).getEmail().toUpperCase().contains(constraint)
                            ||filterList.get(i).getGender().toUpperCase().contains(constraint)||filterList.get(i).getOrderdate().toUpperCase().contains(constraint)
                            ||filterList.get(i).getOrdertotal().toUpperCase().contains(constraint)|filterList.get(i).getMobile().toUpperCase().contains(constraint))
                    {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredPlayers.add(filterList.get(i));
                    }
                }

                results.count=filteredPlayers.size();
                results.values=filteredPlayers;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;

            }
        } else {
            //CHECK CONSTRAINT VALIDITY
            if(constraint != null && constraint.length() > 0)
            {
                //CHANGE TO UPPER
                constraint=constraint.toString().toUpperCase();
                //STORE OUR FILTERED PLAYERS
                ArrayList<MyOrder> filteredPlayers=new ArrayList<>();

                for (int i=0;i<filterList.size();i++)
                {
                    //CHECK
                    if(filterList.get(i).getName().toUpperCase().contains(constraint)||filterList.get(i).getOrderno().toUpperCase().contains(constraint))
                    {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredPlayers.add(filterList.get(i));
                    }
                }

                results.count=filteredPlayers.size();
                results.values=filteredPlayers;
            }else
            {
                results.count=filterList.size();
                results.values=filterList;

            }
        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.albumList= (ArrayList<MyOrder>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}