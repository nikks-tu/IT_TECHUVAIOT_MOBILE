package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThresholdMainObject  {

    @SerializedName("result")
    @Expose
    private List<ThresholdResultObject> result = null;
    @SerializedName("info")
    @Expose
    private ThresholdInfoObject info;

    public List<ThresholdResultObject> getResult() {
        return result;
    }

    public void setResult(List<ThresholdResultObject> result) {
        this.result = result;
    }

    public ThresholdInfoObject getInfo() {
        return info;
    }

    public void setInfo(ThresholdInfoObject info) {
        this.info = info;
    }

}