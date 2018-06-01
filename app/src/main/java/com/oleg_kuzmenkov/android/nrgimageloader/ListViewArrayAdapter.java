package com.oleg_kuzmenkov.android.nrgimageloader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListViewArrayAdapter extends ArrayAdapter<String> {
    private List<String> mURLs;
    private TextView mIdCarTextView;
    private ImageLoader mImageLoader;

    public ListViewArrayAdapter(Context context, List<String> urls) {
        super(context, R.layout.image_view,urls);
        mURLs = urls;
        // Set the size of the memory cache to 6 megabytes
        // maxMemory is about 196 megabytes
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 32;

        mImageLoader = ImageLoader.get(5,cacheSize);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ImageView imageView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image_view, parent, false);
            imageView = view.findViewById(R.id.image_view);
        } else {
            imageView = view.findViewById(R.id.image_view);
            imageView.setImageBitmap(null);
        }
        mIdCarTextView = view.findViewById(R.id.image_url);
        mIdCarTextView.setBackgroundColor(Color.RED);
        mIdCarTextView.setText(mURLs.get(position));
        mImageLoader.displayImage(imageView,mURLs.get(position));

        return view;
    }

}

