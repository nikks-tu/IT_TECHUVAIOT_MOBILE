package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DevicesListMainObject {
    @SerializedName("info")
    @Expose
    private DevicesListInfoObject info;
    @SerializedName("result")
    @Expose
    private List<DevicesListResultObject> result = null;

    public DevicesListInfoObject getInfo() {
        return info;
    }

    public void setInfo(DevicesListInfoObject info) {
        this.info = info;
    }

    public List<DevicesListResultObject> getResult() {
        return result;
    }

    public void setResult(List<DevicesListResultObject> result) {
        this.result = result;
    }

}
