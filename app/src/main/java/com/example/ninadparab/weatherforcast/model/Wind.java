package com.example.ninadparab.weatherforcast.model;

public class Wind
{
    private String gust;

    private String speed;

    private String deg;

    public String getGust ()
    {
        return gust;
    }

    public void setGust (String gust)
    {
        this.gust = gust;
    }

    public String getSpeed ()
    {
        return speed;
    }

    public void setSpeed (String speed)
    {
        this.speed = speed;
    }

    public String getDeg ()
    {
        return deg;
    }

    public void setDeg (String deg)
    {
        this.deg = deg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [gust = "+gust+", speed = "+speed+", deg = "+deg+"]";
    }
}