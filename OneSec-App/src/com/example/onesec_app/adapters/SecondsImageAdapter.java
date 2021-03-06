package com.example.onesec_app.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.onesec.impl.second.Second;

public class SecondsImageAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor mCursor;

    public SecondsImageAdapter(Context context) {
        mContext = context;
    }
    
    public SecondsImageAdapter(Context context, Cursor c) {
    	mContext = context;
    	mCursor = c;
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(155, 155));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Second second = new Second(mCursor);
        mCursor.moveToNext();
        imageView.setImageBitmap(second.getThumbnail(mContext));
        return imageView;
    }
}