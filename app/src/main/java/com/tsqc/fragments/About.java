package com.tsqc.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.R;



public class About extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout=(ImageView)view.findViewById(R.id.addd);
        imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);






        return view;

    }

}
