package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentDataMainObject {

    @SerializedName("info")
    @Expose
    private CurrentDataInfoObject info;
    @SerializedName("result")
    @Expose
    private CurrentDataResultObject result;


    public CurrentDataInfoObject getInfo() {
        return info;
    }

    public void setInfo(CurrentDataInfoObject info) {
        this.info = info;
    }

    public CurrentDataResultObject getResult() {
        return result;
    }

    public void setResult(CurrentDataResultObject result) {
        this.result = result;
    }



}
