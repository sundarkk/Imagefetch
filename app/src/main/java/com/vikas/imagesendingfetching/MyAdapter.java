package com.vikas.imagesendingfetching;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

class MyAdapter extends ArrayAdapter<String> {

    Activity activity;
    String [] imageurl;

    public MyAdapter(MainActivity mainActivity, int item, String[] imageurl) {
        super(mainActivity,item,imageurl);
        this.activity=mainActivity;
        this.imageurl=imageurl;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.item,parent,false);
        ImageView imageView=convertView.findViewById(R.id.image);
        Glide.with(activity).load(imageurl[position]).into(imageView);
        return convertView;
    }
}
