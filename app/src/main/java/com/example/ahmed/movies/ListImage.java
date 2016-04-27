package com.example.ahmed.movies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.NameList;

import java.util.ArrayList;


public class ListImage extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList <String> itemname;
    private final int[] imgid;

    public ListImage(Activity context, ArrayList <String> itemname, int[] imgid) {
        super(context, R.layout.trailer_item, itemname);
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.trailer_item, null,true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView textView = (TextView) rowView.findViewById(R.id.list_item_text);

        imageView.setImageResource(imgid[position]);
        textView.setText("trailer "+ (position+1));
        return rowView;

    };
}
