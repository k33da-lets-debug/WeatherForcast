package com.example.ninadparab.weatherforcast.interfaces;

import com.example.ninadparab.weatherforcast.model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("/data/2.5/weather")
    Call<Response> getWeatherByLocation(@Query("lat") String latitude, @Query("lon") String longitude, @Query("appid") String api_key);

    @GET("/data/2.5/weather")
    Call<Response> getWeatherByCityName(@Query("q") String cityname, @Query("appid") String api_key);
}
