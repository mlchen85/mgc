package com.mgc.momoweatherwidget;

import android.os.AsyncTask;

/**
 * Created by momogod415 on 2013-07-18.
 */
public class WeatherStub extends AsyncTask<String, Void, String> {

    private String url = "http://api.wunderground.com/api/35dea2c484dd551c/forecast";

    @Override
    protected String doInBackground(String... cityUri) {
        return MessageStub.submit(url + cityUri[0] + ".json");
    }
}
