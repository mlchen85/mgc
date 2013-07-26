package com.mgc.momoweatherwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by momogod415 on 2013-07-19.
 */
public class MomoWeatherWidget extends AppWidgetProvider {

    private static final String TAG = "MomoWeatherWidget";
    private static String mCityUri = "";
    private static String mTempFormat = "";
    private static String mCityName = "";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, HashMap<String, String> prefs) {

        Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId);
        mCityUri = prefs.get("cityUri");
        mTempFormat = prefs.get("temp_format");
        mCityName = prefs.get("cityName");

        HashMap<String, String> results = getWeather();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

        views.setImageViewBitmap(R.id.weather_icon, getWeatherIcon(results.get("icon")));
        views.setTextViewText(R.id.period, setPeriod(results.get("weekday"), results.get("day"), results.get("month"), results.get("year")));
        if (mTempFormat == "C") {
            views.setTextViewText(R.id.temp_high, results.get("temp_high"));
            views.setTextViewText(R.id.temp_low, results.get("temp_low"));
        } else if (mTempFormat == "F") {
            views.setTextViewText(R.id.temp_high, convertTemperature(results.get("temp_high")));
            views.setTextViewText(R.id.temp_low, convertTemperature(results.get("temp_low")));
        }
        views.setTextViewText(R.id.location, mCityName);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    static CharSequence setPeriod(String weekday, String day, String month, String year) {
        StringBuilder sb = new StringBuilder();
        sb.append(weekday + "   ");
        sb.append(day + "/" + month + "/" + year);

        return sb.toString();
    }

    static Bitmap getWeatherIcon(String iconString) {
        Bitmap icon = null;
        try {
            icon = new DownloadImageTask().execute(iconString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return icon;
    }

    static HashMap<String, String> getWeather() {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String jsonString = new WeatherStub().execute(mCityUri).get().toString();
            JSONObject jsonWeather = new JSONObject(jsonString);

            JSONObject forecast = jsonWeather.getJSONObject("forecast");
            JSONObject simpleforecast = forecast.getJSONObject("simpleforecast");
            JSONArray forecastday = simpleforecast.getJSONArray("forecastday");

            JSONObject period0 = forecastday.getJSONObject(0);

            JSONObject date = period0.getJSONObject("date");
            JSONObject high = period0.getJSONObject("high");
            JSONObject low = period0.getJSONObject("low");

            map.put("icon", period0.getString("icon_url"));
            map.put("weekday", date.getString("weekday"));
            map.put("day", date.getString("day"));
            map.put("month", date.getString("month"));
            map.put("year", date.getString("year"));
            map.put("temp_high", high.getString("celsius"));
            map.put("temp_low", low.getString("celsius"));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String convertTemperature(String celsius) {
        Integer c = new Integer(celsius);
        return String.valueOf(c * 9 / 5 + 32);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");

        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            HashMap<String, String> prefs = WidgetConfigurationActivity.loadPreference(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, prefs);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
