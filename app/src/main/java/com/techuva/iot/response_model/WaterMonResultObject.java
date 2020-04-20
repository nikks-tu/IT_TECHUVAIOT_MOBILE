package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WaterMonResultObject  {

    @SerializedName("ValuesByDate")
    @Expose
    private ArrayList<WaterMonValueWithDateObject> valuesByDate = null;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("DeviceName")
    @Expose
    private String deviceName;
    @SerializedName("InventoryID")
    @Expose
    private String inventoryID;

    public ArrayList<WaterMonValueWithDateObject> getValuesByDate() {
        return valuesByDate;
    }

    public void setValuesByDate(ArrayList<WaterMonValueWithDateObject> valuesByDate) {
        this.valuesByDate = valuesByDate;
    }

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

    public String getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(String inventoryID) {
        this.inventoryID = inventoryID;
    }

}