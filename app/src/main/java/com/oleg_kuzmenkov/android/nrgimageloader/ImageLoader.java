package com.oleg_kuzmenkov.android.nrgimageloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ImageLoader {
    private final String LOG_TAG = "Message";
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1000;
    // Sets the Time Unit to Milliseconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    private static ImageLoader sImageLoader;

    private List<ImageView> mImageViews;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskCache mDiskCache;

    private DownloadImageCallbacks mDownloadImageCallbacks = new DownloadImageCallbacks() {
        @Override
        public void finishDownloadImage(String url,Bitmap bitmap) {
            Log.d(LOG_TAG,"finishDownloadImage");
            addBitmapToMemoryCache(url,bitmap);
            mDiskCache.addBitmapToDiskCache(url,bitmap);
        }

        @Override
        public void showImage(final int index, final ImageView imageView, Bitmap bitmap) {
            final Bitmap scaledBitmap = scaleBitmap(imageView, bitmap);

            imageView.post(new Runnable() {
                @Override
                public void run() {
                    int indexOfLastTask = mImageViews.lastIndexOf(imageView);
                    if (indexOfLastTask == index) {
                        imageView.setImageBitmap(scaledBitmap);
                    }
                }
            });
        }
    };

    public static ImageLoader get(int countTheads, int cacheSize){
        if(sImageLoader == null){
            sImageLoader = new ImageLoader(countTheads,cacheSize);
        }
        return sImageLoader;
    }

    private ImageLoader(int countThreads, int cacheSize){
        mImageViews = new ArrayList<>();
        mDiskCache = new DiskCache();

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

        mThreadPoolExecutor = new ThreadPoolExecutor(countThreads, 20,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                new LinkedBlockingDeque<Runnable>());
    }

    /**
     * Display the target image in ImageView
     */
    public void displayImage(ImageView imageView, String url){
        //check memory cache
        Bitmap bitmap = getBitmapFromMemoryCache(url);
        if (bitmap == null) {
            // check disk cashe
            boolean isExistInDiskCache = mDiskCache.fileIsExistInDiskCache(url);
            if(isExistInDiskCache == false) {
                Log.d(LOG_TAG,"Get Bitmap from Network");
                //download image from network
                mImageViews.add(imageView);
                int indexOfCurrentView = mImageViews.lastIndexOf(imageView);
                mThreadPoolExecutor.execute(new DownloadImageThread(indexOfCurrentView,url,imageView,mDownloadImageCallbacks));
            } else{
                //exist in disk cache
                Log.d(LOG_TAG,"Get Bitmap from DiskCache");
                mImageViews.add(imageView);
                int indexOfCurrentView = mImageViews.lastIndexOf(imageView);
                mDiskCache.showBitmapFromDiskCache(indexOfCurrentView,url,imageView,mDownloadImageCallbacks);
            }
        } else{
            // exist in memory cache
            Log.d(LOG_TAG,"Get Bitmap from MemoryCache");
            mImageViews.add(imageView);
            bitmap = scaleBitmap(imageView,bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Add bitmap to memory cache
     */
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * Get bitmap from memory cache
     */
    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * Scale bitmap by size of the ImageView
     */
    private Bitmap scaleBitmap(ImageView imageView, Bitmap bitmap){
        Bitmap scaledBitmap = null;

        if(imageView != null && bitmap!=null){
            int wantedWidth;
            int wantedHeight;

            if(bitmap.getWidth() > imageView.getLayoutParams().width ) {
                wantedWidth = imageView.getLayoutParams().width;
            } else {
                wantedWidth = bitmap.getWidth();
            }

            if(bitmap.getHeight() > imageView.getLayoutParams().height){
                wantedHeight = imageView.getLayoutParams().height;
            } else {
                wantedHeight = bitmap.getHeight();
            }
            scaledBitmap = Bitmap.createScaledBitmap(bitmap,wantedWidth,wantedHeight,false);
        }
        return scaledBitmap;
    }
}
