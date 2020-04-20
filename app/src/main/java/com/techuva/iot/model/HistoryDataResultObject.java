package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryDataResultObject {

    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("DeviceName")
    @Expose
    private String deviceName;
    @SerializedName("InventoryID")
    @Expose
    private String invenoryID;
    @SerializedName("Data")
    @Expose
    private List<HistoryDataObject> data = null;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<HistoryDataObject> getData() {
        return data;
    }

    public void setData(List<HistoryDataObject> data) {
        this.data = data;
    }

    public String getInvenoryID() {
        return invenoryID;
    }

    public void setInvenoryID(String invenoryID) {
        this.invenoryID = invenoryID;
    }

}