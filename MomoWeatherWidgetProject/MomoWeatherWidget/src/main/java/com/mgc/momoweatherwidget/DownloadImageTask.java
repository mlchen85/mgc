package com.mgc.momoweatherwidget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

/**
 * Created by momogod415 on 2013-07-22.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlString = urls[0];
        Bitmap icon = null;
        try {
            URL url = new URL(urlString);
            icon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            Log.e(DownloadImageTask.class.getName(), "unable to load weather icon");
        }
        return icon;
    }
}
