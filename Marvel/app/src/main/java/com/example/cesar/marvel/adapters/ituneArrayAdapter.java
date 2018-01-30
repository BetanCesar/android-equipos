package com.example.cesar.marvel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.cesar.marvel.R;
import  com.example.cesar.marvel.pojo.itune;

/**
 * Created by Cesar on 26/01/18.
 */

public class ituneArrayAdapter extends ArrayAdapter<itune> {

    private ArrayList<itune> arrayList;

    public ituneArrayAdapter(Context context, int resource, List<itune> objects) {
        super(context, resource, objects);
        arrayList = (ArrayList<itune>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        itune ituneO = arrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itunes_layout, parent, false);
        }
        TextView collectionName = (TextView) convertView.findViewById(R.id.collection);
        TextView trackName = (TextView) convertView.findViewById(R.id.trackName);
        TextView trackPrice = (TextView) convertView.findViewById(R.id.trackPrice);
        collectionName.setText(ituneO.collectionName);
        trackName.setText(ituneO.trackName);
        trackPrice.setText(ituneO.trackPrice+"");
        return convertView;
    }
}
