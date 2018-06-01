package com.oleg_kuzmenkov.android.nrgimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageThread implements Runnable {
    private final String LOG_TAG = "Message";

    private int mIndex;
    private String mUrl;
    private ImageView mImageView;
    private DownloadImageCallbacks mDownloadImageCallbacks;

    public DownloadImageThread(int index, String url, ImageView imageView, DownloadImageCallbacks downloadImageCallbacks) {
        mIndex = index;
        mUrl = url;
        mImageView = imageView;
        mDownloadImageCallbacks = downloadImageCallbacks;
    }

    @Override
    public void run() {
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(mUrl).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

        if(bitmap != null) {
            mDownloadImageCallbacks.finishDownloadImage(mUrl,bitmap);
            mDownloadImageCallbacks.showImage(mIndex,mImageView,bitmap);
        }
    }

}
