package com.example.ninadparab.weatherforcast;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ninadparab.weatherforcast.interfaces.WeatherAPI;
import com.example.ninadparab.weatherforcast.model.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    public static final String API_URL = Util.API_URL;//"http://api.openweathermap.org";
    public static final String API_KEY = Util.API_KEY;//"dddbffd51b50e7288d6711f481b33600";

    long MIN_TIME = 50000;
    long MIN_DISTANCE = 1000;
    int REQUEST_CODE = 1;

    //Member variables
    ImageView mMainActivityIVWeatherWidget;
    TextView mMainActivityTVMinTemp, mMainActivityTVMaxTemp, mMainActivityTVCityName, mMainActivityTVHumedity, mMainActivityTVPreassure, mMainActivityTVLongitude, mMainActivityTVLatitude;
    Button mMainActivityBTNGetWeather;

    //Location and Networking
    LocationManager locationManager;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing variables
        mMainActivityTVMinTemp = findViewById(R.id.mainactivity_tv_mintemp);
        mMainActivityTVMaxTemp = findViewById(R.id.mainactivity_tv_maxtemp);
        mMainActivityTVCityName = findViewById(R.id.mainactivity_tv_cityname);
        mMainActivityIVWeatherWidget = findViewById(R.id.mainactivity_iv_weatherwidget);
        mMainActivityTVHumedity = findViewById(R.id.mainactivity_tv_humidity);
        mMainActivityTVPreassure = findViewById(R.id.mainactivity_tv_preassure);
        mMainActivityTVLatitude = findViewById(R.id.mainactivity_tv_latitude);
        mMainActivityTVLongitude = findViewById(R.id.mainactivity_tv_longitude);
        mMainActivityBTNGetWeather = findViewById(R.id.mainactivity_btn_getweather);
        mMainActivityBTNGetWeather.setOnClickListener(this);

        //Check for permissions
        // Note: if you don't see any text on first screen ebven by giving permission that means you have to set the GPS accuracy to High mode
        if (!Util.checkPermission(MainActivity.this)) {
            Util.requestPermission(MainActivity.this, REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        getLocationData(longitude, latitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getLocationData(final String longitude, String latitude) {

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);
        Call<Response> call = weatherAPI.getWeatherByLocation(latitude, longitude, API_KEY);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> rescall, retrofit2.Response<Response> response) {

                double maxTemp = convertToCelcious(Double.parseDouble(response.body().getMain().getTemp_max()));
                double minTemp = convertToCelcious(Double.parseDouble(response.body().getMain().getTemp_min()));

                if(response.body()!=null){
                    mMainActivityTVCityName.setText(response.body().getName());
                    mMainActivityTVMaxTemp.setText(String.valueOf(maxTemp) + " °C");
                    mMainActivityTVMinTemp.setText(String.valueOf(minTemp) + " °C");
                    mMainActivityTVHumedity.setText(response.body().getMain().getHumidity());
                    mMainActivityTVPreassure.setText(response.body().getMain().getPressure() + " mb");
                    mMainActivityTVLatitude.setText(response.body().getCoord().getLat());
                    mMainActivityTVLongitude.setText(response.body().getCoord().getLon());
                    mMainActivityIVWeatherWidget.setImageResource(getImageIDReferance(response.body().getMain().getTemp()));
                }else{
                    Toast.makeText(MainActivity.this, R.string.unable_to_get_any_info_error_message,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Response> rescall, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.retroifit_error_message, Toast.LENGTH_LONG).show();
            }
        });
    }


    private double convertToCelcious(double kelvin) {
        return Math.ceil(kelvin - 273.0);
    }

    private int getImageIDReferance(String condition) {
        Double temp = Double.parseDouble(condition);
        if (temp > 0 && temp < 300) {
            return R.drawable.w8;
        } else if (temp > 0 && temp < 300) {
            return R.drawable.w5;
        } else if (temp > 300 && temp < 400) {
            return R.drawable.w5;
        } else if (temp > 400 && temp < 500) {
            return R.drawable.w13;
        } else if (temp > 500 && temp < 600) {
            return R.drawable.w12;
        } else if (temp > 600 && temp < 700) {
            return R.drawable.w10;
        } else if (temp > 701 && temp < 771) {
            return R.drawable.w8;
        } else if (temp > 772 && temp < 800) {
            return R.drawable.w4;
        } else if (temp == 800) {
            return R.drawable.w8;
        } else if (temp > 800 && temp <= 804) {
            return R.drawable.w3;
        } else if (temp >= 900 && temp <= 902) {
            return R.drawable.w6;
        } else if (temp == 903) {
            return R.drawable.w7;
        } else if (temp == 904) {
            return R.drawable.w2;
        } else if (temp >= 905 && temp <= 1000) {
            return R.drawable.w6;
        }
        return R.drawable.w3;
    }

    //This will launch next activity
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CitySearchActivity.class);
        startActivity(intent);
    }
}
