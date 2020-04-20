package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionInfoMainObject {

    @SerializedName("info")
    @Expose
    private VersionInfoObject info;
    @SerializedName("result")
    @Expose
    private VersionInfoResultObject result;

    public VersionInfoObject getInfo() {
        return info;
    }

    public void setInfo(VersionInfoObject info) {
        this.info = info;
    }

    public VersionInfoResultObject getResult() {
        return result;
    }

    public void setResult(VersionInfoResultObject result) {
        this.result = result;
    }

}