package com.satya.weather.ui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by uagarwal on 5/12/2016.
 */
public class Rain {

    @SerializedName("3h")
    @Expose
    private double _3h;

    public double get3h() {
        return _3h;
    }

    public void set3h(double _3h) {
        this._3h = _3h;
    }

}

