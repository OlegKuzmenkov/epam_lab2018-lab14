package com.oleg_kuzmenkov.android.nrgimageloader;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class DiskCache {
    private final String LOG_TAG = "Message";
    private final String FOLDER_NAME = "Downloads";

    private int countFiles;
    private boolean isCreate;
    private File mDirectory;
    private HashMap<String,String> mFilesHashMap;

    public DiskCache() {
        mFilesHashMap = new HashMap();
        mDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME);
        if (!mDirectory.exists()) {
            //create directory
            isCreate = mDirectory.mkdirs();
            if(!isCreate){
                Log.e(LOG_TAG,"Couldn't create target directory");
            }
        }
    }

    /**
     * Add bitmap to the DiskCache
     */
    public void addBitmapToDiskCache(String url, Bitmap bitmap) {
        countFiles++;
        String filename="file_"+countFiles+".jpeg";
        if(saveFile(mDirectory,filename,bitmap) == true){
            mFilesHashMap.put(url,filename);
        } else{
            Log.e(LOG_TAG,"Couldn't save file");
        }
    }

    /**
     * Show bitmap from the DiskCache
     */
    public void showBitmapFromDiskCache(int index, String url,ImageView imageView, DownloadImageCallbacks downloadImageCallbacks) {
        String filename = mFilesHashMap.get(url);
        if(filename != null){
            String filePath = mDirectory+File.separator +filename;
            new GetImageFromDiskCacheTask(index,imageView,downloadImageCallbacks).execute(filePath);
        }
    }

    /**
     * Check DiskCache for the presence of the file
     */
    public boolean fileIsExistInDiskCache(String url){
        String filename = mFilesHashMap.get(url);
        if(filename == null){
            return false;
        }
        return true;
    }

    /**
     * Save bitmap into the file
     */
    private boolean saveFile(File dir, String fileName, Bitmap bitmap){
        File imageFile = new File(dir,fileName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(imageFile);
            if(bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            out.close();
            return true;
        } catch (IOException e) {
            Log.e(LOG_TAG,e.getMessage());
            return false;
        }
    }
}
