package com.oleg_kuzmenkov.android.nrgimageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface DownloadImageCallbacks {
    void finishDownloadImage(String url, Bitmap bitmap);
    void showImage(int index, ImageView imageView, Bitmap bitmap);
}
