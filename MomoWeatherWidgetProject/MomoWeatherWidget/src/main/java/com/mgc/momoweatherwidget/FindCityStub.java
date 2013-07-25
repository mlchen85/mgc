package com.mgc.momoweatherwidget;

import android.os.AsyncTask;

/**
 * Created by momogod415 on 2013-07-23.
 */
public class FindCityStub extends AsyncTask<String, Void, String> {

    private String request = "http://autocomplete.wunderground.com/aq?query=";

    @Override
    protected String doInBackground(String... strings) {
        return MessageStub.submit(request + strings[0]);
    }
}
