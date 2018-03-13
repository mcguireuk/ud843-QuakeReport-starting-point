package com.erin.paige.quakereport;

/**
 * Created by Shaun McGuire on 14/04/2017.
 */

class Earthquakes {

    private double mag;
    private String place;
    private String date;
    private String time;
    private String url;

    public Earthquakes (double mag, String place, String date, String time, String url){

        this.mag = mag;
        this.place = place;
        this.date = date;
        this.time = time;
        this.url = url;

    }

    public double getMag() {return mag;}

    public String getPlace() {return place;}

    public String getDate() {return date;}

    public String getTime() {return time;}

    public String getUrl() {return url;}



}
