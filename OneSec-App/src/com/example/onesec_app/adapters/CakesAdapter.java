package com.example.onesec_app.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.util.Utilities;
import com.example.onesec_app.R;

public class CakesAdapter extends ArrayAdapter<Cake> {

	Context context; 
    int layoutResourceId;    
    List<Cake> cakeList;
    
    public CakesAdapter(Context context, int layoutResourceId, List<Cake> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.cakeList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CakesHolder holder = null;
        
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new CakesHolder();
            holder.thumbnailView = (ImageView)row.findViewById(R.id.cakeThumbnail);
            holder.titleView = (TextView)row.findViewById(R.id.cakeTitle);
//            holder.dateView = (TextView)row.findViewById(R.id.cakeDate);
           
            row.setTag(holder);
        }
        else {
            holder = (CakesHolder)row.getTag();
        }
        
        Cake cake = cakeList.get(position);
        holder.thumbnailView.setImageBitmap(cake.getThumbnail(context));
        holder.titleView.setText(cake.getTitle());
//        holder.dateView.setText(Utilities.dateToString(cake.getDate()));
        
        return row;
    }
    
    static class CakesHolder {
        ImageView thumbnailView;
        TextView titleView;
//        TextView dateView;
    }
	
}
