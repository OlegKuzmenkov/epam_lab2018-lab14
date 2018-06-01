package com.oleg_kuzmenkov.android.nrgimageloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private final String LOG_TAG = "Message";

    private Button mDisplayingImagesButton;
    private ListView mListView;
    private ListViewArrayAdapter mAdapter;
    private List<String> mUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrls = new ArrayList();
        mUrls.add("http://bipbap.ru/wp-content/uploads/2017/04/3444df58d1da267e13bdf666a946a4f3ffa9b659.jpg");
        mUrls.add("http://www.topoboi.com/pic/201307/800x480/topoboi.com-2251.jpg");
        mUrls.add("https://cdn.allwallpaper.in/wallpapers/800x480/702/beautiful-butterfly-and-flowers-800x480-wallpaper.jpg");
        mUrls.add("http://pt.naturewallpaperfree.com/animais-peixes/natureza-papel-de-parede-800x480-1640-e183fa4.jpg");
        mUrls.add("http://ru.naturewallpaperfree.com/vody-volny/priroda-oboi-800x480-489-d2bb7e5d.jpg");
        mUrls.add("https://i.pinimg.com/originals/2f/a4/65/2fa465f585291eb4ff8404c2eb96feea.jpg");
        mUrls.add("http://ru.naturewallpaperfree.com/gory-nebo-poberezhiy/priroda-oboi-800x480-3556-d54af798.jpg");
        mUrls.add("https://i.pinimg.com/originals/0b/6c/06/0b6c06fa66ff06e6160b48e4433c742d.jpg");
        mUrls.add("https://cdn.allwallpaper.in/wallpapers/800x480/12746/cn-tower-canadian-national-railway-islam-muslim-mosques-800x480-wallpaper.jpg");
        mUrls.add("http://symphonytravel.com.ua/wp-content/uploads/2017/03/1Sheraton-Maldives-Full-Moon-Resort-Spa-11-800x480.jpg");
        mUrls.add("http://luckytrvl.com/wp-content/uploads/2017/02/92-1-800x480.jpg");
        mUrls.add("http://www.wanderlustmary.com/wp-content/uploads/2017/02/bali-800x480.jpg");
        mUrls.add("http://colibri-travel.com/wp-content/uploads/2017/03/kuramathi-island-resort-20362122-1425372854-ImageGalleryLightbox-800x480.jpg");
        mUrls.add("http://oboinastol.net/katalog_kartinok/tom24/034/skachat_oboi_800x480.jpg");
        mUrls.add("https://cdn.allwallpaper.in/wallpapers/800x480/12785/sunset-cityscapes-london-cities-800x480-wallpaper.jpg");

        mListView = findViewById(R.id.list_view);
        mDisplayingImagesButton = findViewById(R.id.download_image_button);
        mDisplayingImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission() == true) {
                    init(mUrls);
                } else{
                    startRequestForPermission();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
                } else{
                    //permission already granted
                    Log.d(LOG_TAG, "Permission Granted");
                    init(mUrls);
                }
                break;

            default:
                break;
        }
    }

    /**
     * Check permission
     */
    private boolean checkPermission(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //permission already granted
                return true;
            } else {
                return false;
            }
        } else {
            //permission already granted
            return true;
        }
    }

    /**
     * Request permission
     */
    private void startRequestForPermission () {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * Initialize the ListView
     */
    private void init(List<String> urls){
        if(mAdapter == null) {
            mAdapter = new ListViewArrayAdapter(getApplicationContext(), urls);
        }
        mListView.setAdapter(mAdapter);
    }
}
