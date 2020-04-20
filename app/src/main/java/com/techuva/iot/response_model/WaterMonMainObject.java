package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.techuva.iot.model.CurrentDataInfoObject;

public class WaterMonMainObject {

    @SerializedName("result")
    @Expose
    private WaterMonResultObject result;
    @SerializedName("info")
    @Expose
    private CurrentDataInfoObject info;

    public WaterMonResultObject getResult() {
        return result;
    }

    public void setResult(WaterMonResultObject result) {
        this.result = result;
    }

    public CurrentDataInfoObject getInfo() {
        return info;
    }

    public void setInfo(CurrentDataInfoObject info) {
        this.info = info;
    }

}