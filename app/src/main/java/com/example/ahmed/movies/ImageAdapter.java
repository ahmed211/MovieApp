package com.example.ahmed.movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> posterURLs;

    public ImageAdapter(Context context, ArrayList<String> posterURLs ) {
        mContext = context;
        this.posterURLs=posterURLs;
    }

    public int getCount() {
        return posterURLs.size();
    }

    public Object getItem(int position) {
        return getItem(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (null == convertView) {

            imageView = new ImageView(mContext);
            convertView=imageView;
        }
        else
            imageView = (ImageView)convertView;
        Picasso.with(mContext).load(posterURLs.get(position)).resize(540, 820).into(imageView);

        return convertView;

    }
}
