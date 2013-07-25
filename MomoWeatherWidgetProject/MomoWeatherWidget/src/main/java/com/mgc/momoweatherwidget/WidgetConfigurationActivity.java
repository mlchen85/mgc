package com.mgc.momoweatherwidget;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class WidgetConfigurationActivity extends Activity {

    private static final String TAG = WidgetConfigurationActivity.class.getName();
    private static final int FORMAT_CELSIUS = 0;
    private static final int FORMAT_FAHRENHEIT = 1;
    EditText et_city;
    RadioButton btn_celsius;
    RadioButton btn_fahrenheit;
    Button btn_find_city;
    Button btn_done;
    TextView tv_city;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private int temp_format = 0;
    private String cityUri = "";
    private String cityName = "";


    public WidgetConfigurationActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        this.setContentView(R.layout.config_activity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        tv_city = (TextView) findViewById(R.id.textView_config_city);
        et_city = (EditText) findViewById(R.id.editText_config_city);
        btn_celsius = (RadioButton) findViewById(R.id.radioButton_config_celsius);
        btn_fahrenheit = (RadioButton) findViewById(R.id.radioButton_config_fahrenheit);
        btn_find_city = (Button) findViewById(R.id.button_config_find_city);
        btn_done = (Button) findViewById(R.id.button_config_done);

        btn_celsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_fahrenheit.setChecked(false);
                temp_format = FORMAT_CELSIUS;
                Log.d(TAG, "Temperature Format: Celsius");
            }
        });

        btn_fahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_celsius.setChecked(false);
                temp_format = FORMAT_FAHRENHEIT;
                Log.d(TAG, "Temperature Format: Fahrenheit");
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = WidgetConfigurationActivity.this;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

                MomoWeatherWidget.updateAppWidget(context, appWidgetManager, appWidgetId, cityUri, cityName, temp_format);

                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, result);
                finish();

            }
        });

        btn_find_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    String city = et_city.getText().toString();
                    String jsonString = new FindCityStub().execute(city).get();
                    JSONObject result = new JSONObject(jsonString);
                    JSONArray array = result.getJSONArray("RESULTS");
                    final String[] strings = new String[array.length()];
                    final HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        strings[i] = item.getString("name");
                        map.put(item.getString("name"), item.getString("l"));
                    }

                    final Context context = WidgetConfigurationActivity.this;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Select your city:");
                    builder.setItems(strings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cityName = strings[i];
                            cityUri = map.get(cityName);
                            et_city.setText(cityName);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }


    }
}
