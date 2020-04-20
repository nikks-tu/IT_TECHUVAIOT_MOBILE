package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WaterMonValueWithDateObject {

    @SerializedName("Values")
    @Expose
    private ArrayList<WaterMonValuesObject> values = null;
    @SerializedName("Date")
    @Expose
    private String date;

    public ArrayList<WaterMonValuesObject> getValues() {
        return values;
    }

    public void setValues(ArrayList<WaterMonValuesObject> values) {
        this.values = values;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}