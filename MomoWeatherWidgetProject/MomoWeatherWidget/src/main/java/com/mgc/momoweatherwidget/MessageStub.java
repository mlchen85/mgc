package com.mgc.momoweatherwidget;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by momogod415 on 2013-07-23.
 */
public class MessageStub {
    public static String submit(String url) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                content.close();
            } else {
                Log.e(MessageStub.class.toString(), "Failed to download File");
            }
        } catch (IOException e) {
            Log.e(MessageStub.class.getName(), e.toString());
        }
        return builder.toString();
    }
}
