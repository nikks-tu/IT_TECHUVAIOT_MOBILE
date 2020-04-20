package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThresholdResultObject {

    @SerializedName("InventoryID")
    @Expose
    private Integer inventoryID;
    @SerializedName("InventoryName")
    @Expose
    private String inventoryName;
    @SerializedName("ChannelNumber")
    @Expose
    private Integer channelNumber;
    @SerializedName("ChannelLabel")
    @Expose
    private String channelLabel;
    @SerializedName("Threshold")
    @Expose
    private List<ThresholdValuesObject> threshold = null;

    public Integer getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Integer inventoryID) {
        this.inventoryID = inventoryID;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(Integer channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channelLabel) {
        this.channelLabel = channelLabel;
    }

    public List<ThresholdValuesObject> getThreshold() {
        return threshold;
    }

    public void setThreshold(List<ThresholdValuesObject> threshold) {
        this.threshold = threshold;
    }

}
