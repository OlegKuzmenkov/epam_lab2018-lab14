package com.oleg_kuzmenkov.android.nrgimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

class GetImageFromDiskCacheTask extends AsyncTask<String, Void, Bitmap> {
    private final String LOG_TAG = "Message";

    private int mIndex;
    private String mFilePath;
    private ImageView mImageView;
    private DownloadImageCallbacks mDownloadImageCallbacks;


    public GetImageFromDiskCacheTask(int index, ImageView imageView,DownloadImageCallbacks downloadImageCallbacks) {
        mIndex = index;
        mImageView = imageView;
        mDownloadImageCallbacks = downloadImageCallbacks;
    }

    protected Bitmap doInBackground(String... paths) {
        mFilePath = paths[0];
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(mFilePath);
            if(bitmap != null) {
                return bitmap;
            } else{
                Log.e(LOG_TAG,"Couldn't get bitmap");
                return null;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null) {
            mDownloadImageCallbacks.showImage(mIndex,mImageView,bitmap);
        }
    }
}

