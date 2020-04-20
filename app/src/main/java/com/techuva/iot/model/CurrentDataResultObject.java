package com.techuva.iot.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CurrentDataResultObject {



    @SerializedName("Values")
    @Expose
    private List<CurrentDataValueObject> values = null;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("DeviceName")
    @Expose
    private String deviceName;
    @SerializedName("InventoryID")
    @Expose
    private String inventoryID;

    public List<CurrentDataValueObject> getValues() {
        return values;
    }

    public void setValues(List<CurrentDataValueObject> values) {
        this.values = values;
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
