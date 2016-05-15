package com.satya.weather.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.satya.weather.R;
import com.satya.weather.ui.manager.GsonRequest;
import com.satya.weather.ui.manager.RequestManager;
import com.satya.weather.ui.model.Coord;
import com.satya.weather.ui.model.Main;
import com.satya.weather.ui.model.Weather;
import com.satya.weather.ui.model.Weather_;
import com.satya.weather.ui.util.Configuration;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";

    private static final String TAG_REQUEST = "WEATHER_TAG";

    private TextView mSearchCityName;

    private TextView mCoord;

    private TextView mWeather;

    private TextView mTemp;

    private TextView mWait;

    private EditText mCityName;

    private AppCompatButton mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mSubmit = (AppCompatButton) findViewById(R.id.submit);
        mSearchCityName = (TextView) findViewById(R.id.searchcity);
        mCoord = (TextView) findViewById(R.id.cood);
        mWeather = (TextView) findViewById(R.id.weather);
        mTemp = (TextView) findViewById(R.id.temp);
        mWait = (TextView) findViewById(R.id.wait);
        mCityName = (EditText) findViewById(R.id.city_name);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWait.setText("Wait...");
                mWait.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(false);
                requestWeather(mCityName.getText().toString());
            }
        });
    }

    private void requestWeather(String cityName) {
        Uri.Builder builder = Uri.parse(Configuration.URL).buildUpon();
        builder.appendQueryParameter(Configuration.CITY_KEY, cityName);
        builder.appendQueryParameter(Configuration.APP_KEY, Configuration.API_KEY_VALUE).build();
        GsonRequest<Weather> gsonObjRequest = new GsonRequest<Weather>(Request.Method.POST, builder.toString(),
                Weather.class, null, null, null, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                mWait.setVisibility(View.GONE);
                mSubmit.setEnabled(true);
                Log.d(TAG, "Weather Successfull : ");
                mSearchCityName.setText(weather.getName());
                StringBuilder builder = new StringBuilder();
                Coord co = weather.getCoord();
                builder.append("Latitude : ").append(co.getLat()).append(" Longitude : ").append(co.getLon());
                mCoord.setText(builder.toString());
                builder = new StringBuilder();
                List<Weather_> weathers = weather.getWeather();
                for(Weather_ w : weathers){
                    builder.append("Expecting : ").append(w.getDescription());
                }
                mWeather.setText(builder.toString());

                builder = new StringBuilder();
                Main temp = weather.getMain();
                builder.append("Temperature : ").append(temp.getTemp());
                mTemp.setText(builder.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mWait.setText("Something went wrong. Please try again");
                mSubmit.setEnabled(true);
                Log.d(TAG, "Weather Failed : " + error.getMessage());
                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        }
        );
        gsonObjRequest.setTag(TAG_REQUEST);
        RequestManager.getInstance(getApplicationContext()).addToRequestQueue(gsonObjRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RequestManager.getInstance(getApplicationContext()).cancelRequest(TAG_REQUEST);
    }
}
