package com.example.easydesign.adapter;

/**
 * Created by zuoyun on 2016/3/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.easydesign.R;
import com.lidroid.xutils.BitmapUtils;

public class Adapter_hlv extends BaseAdapter {
    Context context;
    String type;
    String []imgnames;
    BitmapUtils bitmap;

    public Adapter_hlv(Context context, String type, String[] imgnames, BitmapUtils bitmap) {
        super();
        this.context = context;
        this.type= type;
        this.imgnames = imgnames;
        this.bitmap=bitmap;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imgnames.length-1;
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return imgnames[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =LayoutInflater.from(context).inflate(R.layout.item_hlv, null);
        ImageView img=(ImageView) view.findViewById(R.id.item_hlv_img);
        String url = "http://42.159.246.0/artlu/app/koutu/"+type+"/"+imgnames[position];
        bitmap.display(img, url);
        return view;
    }


}