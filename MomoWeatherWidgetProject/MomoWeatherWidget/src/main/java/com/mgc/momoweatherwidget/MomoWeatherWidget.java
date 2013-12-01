package com.mgc.momoweatherwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

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

        HashMap<String, String> results = getCurrentCondition(mCityUri);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);

//        views.setImageViewBitmap(R.id.weather_icon, getWeatherIcon(results.get("icon")));
//        views.setTextViewText(R.id.period, setPeriod(results.get("weekday"), results.get("day"), results.get("month"), results.get("year")));
        if (mTempFormat == "C") {
            views.setTextViewText(R.id.textView_temp, results.get("temp_c"));
        } else if (mTempFormat == "F") {
            views.setTextViewText(R.id.textView_temp, results.get("temp_f"));
        }
//        views.setTextViewText(R.id.location, results.get("city"));
        views.setTextViewText(R.id.textView_weather, results.get("weather"));

        Intent intent = new Intent(context, WidgetConfigurationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle extras = new Bundle();
        extras.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtras(extras);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

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

    static HashMap<String, String> getCurrentCondition(String mCityUri) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String jsonString = new WeatherStub().execute(mCityUri).get().toString();
            JSONObject jsonWeather = new JSONObject(jsonString);

            JSONObject current_observation = jsonWeather.getJSONObject("current_observation");

            JSONObject display_location = current_observation.getJSONObject("display_location");
            map.put("city", display_location.getString("city"));

            map.put("weather", current_observation.getString("weather"));

            map.put("temp_f", current_observation.getString("temp_f"));
            map.put("temp_c", current_observation.getString("temp_c"));

            map.put("icon", current_observation.getString("icon_url"));

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
        Log.d(TAG, "number of widget on host: " + N);
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            HashMap<String, String> prefs = WidgetConfigurationActivity.loadPreference(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, prefs);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            WidgetConfigurationActivity.deletePreference(context, appWidgetIds[i]);
        }

    }
}
