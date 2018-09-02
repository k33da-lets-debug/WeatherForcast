package com.example.ninadparab.weatherforcast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ninadparab.weatherforcast.interfaces.WeatherAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.ninadparab.weatherforcast.model.Response;

public class CitySearchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView citySearchActivityTVMinTemp, citySearchActivityTVMaxTemp, citySearchActivityTVPreassure, citySearchActivityTVHumidity, citySearchActivityTVWindSpeed, citySearchActivityTVCountry, citySearchActivityTVDescription, citySearchActivityTVCityName;
    private ImageView citySearchActivityIVWidget;
    private EditText citySearchActivityETSearchText;
    private ImageButton citySearchActivityIVSearchButton;
    private Retrofit retrofit;
    private String cityName;
    public static final String API_URL = Util.API_URL; //"http://api.openweathermap.org";
    public static final String API_KEY =Util.API_KEY; //"dddbffd51b50e7288d6711f481b33600";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        citySearchActivityTVMinTemp = findViewById(R.id.citysearchactivity_tv_mintemp);
        citySearchActivityTVMaxTemp = findViewById(R.id.citysearchactivity_tv_maxtemp);
        citySearchActivityTVPreassure = findViewById(R.id.citysearchactivity_tv_preassure);
        citySearchActivityTVHumidity = findViewById(R.id.citysearchactivity_tv_humidity);
        citySearchActivityTVWindSpeed = findViewById(R.id.citysearchactivity_tv_windspeed);
        citySearchActivityTVCountry = findViewById(R.id.citysearchactivity_tv_country);
        citySearchActivityTVDescription = findViewById(R.id.citysearchactivity_iv_description);
        citySearchActivityTVCityName = findViewById(R.id.citysearchactivity_tv_cityname);
        citySearchActivityIVSearchButton = findViewById(R.id.citysearchactivity_iv_searchbutton);
        citySearchActivityIVWidget = findViewById(R.id.citysearchactivity_iv_widgit);
        citySearchActivityETSearchText = findViewById(R.id.citysearchactivity_et_searchtext);
        citySearchActivityIVSearchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //General validation
        if (String.valueOf(citySearchActivityETSearchText.getText()).equals("") && String.valueOf(citySearchActivityETSearchText).equals(null)) {
            Toast.makeText(CitySearchActivity.this, "Please enter city name", Toast.LENGTH_LONG).show();
        } else {
            cityName = String.valueOf(citySearchActivityETSearchText.getText());
            getWeatherByCity();
        }
    }

    private void getWeatherByCity() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherCitySearchApi = retrofit.create(WeatherAPI.class);
        Call<Response> call = weatherCitySearchApi.getWeatherByCityName(cityName, API_KEY);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if (String.valueOf(response.code()).equals("404")) {
                    Toast.makeText(CitySearchActivity.this, "Please enter valid city name", Toast.LENGTH_LONG).show();
                } else {
                    citySearchActivityTVMinTemp.setText(response.body().getMain().getTemp_min());
                    citySearchActivityTVMaxTemp.setText(response.body().getMain().getTemp_max());
                    citySearchActivityTVPreassure.setText(response.body().getMain().getPressure());
                    citySearchActivityTVHumidity.setText(response.body().getMain().getHumidity());
                    citySearchActivityTVWindSpeed.setText(response.body().getWind().getSpeed());
                    citySearchActivityTVCountry.setText(response.body().getSys().getCountry());
                    citySearchActivityTVDescription.setText(response.body().getWeather()[0].getDescription());
                    citySearchActivityTVCityName.setText(response.body().getName());
                    citySearchActivityIVWidget.setImageResource(getImageIDReferance(response.body().getMain().getTemp()));
                    citySearchActivityETSearchText.setText("");
                }

            }
            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                call.cancel();
                Toast.makeText(CitySearchActivity.this, R.string.retroifit_error_message, Toast.LENGTH_LONG).show();
            }
        });

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
}
