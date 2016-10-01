package com.example.dilrajsingh.json;

/**
 * Created by Dilraj Singh on 18/08/2016.
 */
public class Weather {

    private float temp, press, tmax, tmin;
    private long hum;

    public Weather(){}

    public float getTmin() {
        return tmin;
    }

    public void setTmin(float tmin) {
        this.tmin = tmin;
    }

    public long getHum() {
        return hum;
    }

    public void setHum(long hum) {
        this.hum = hum;
    }

    public float getPress() {
        return press;
    }

    public void setPress(float press) {
        this.press = press;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTmax() {
        return tmax;
    }

    public void setTmax(float tmax) {
        this.tmax = tmax;
    }
}
