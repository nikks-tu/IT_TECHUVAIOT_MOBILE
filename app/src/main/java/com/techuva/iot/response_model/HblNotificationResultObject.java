package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HblNotificationResultObject {

    @SerializedName("notificationTime")
    @Expose
    private String notificationTime;
    @SerializedName("PushNotoficationFlag")
    @Expose
    private Boolean pushNotoficationFlag;
    @SerializedName("InventoryID")
    @Expose
    private Integer inventoryID;
    @SerializedName("InventoryName")
    @Expose
    private String inventoryName;
    @SerializedName("ChannelNumber")
    @Expose
    private String channelNumber;
    @SerializedName("ChannelLabel")
    @Expose
    private String channelLabel;
    @SerializedName("ChannelValue")
    @Expose
    private String channelValue;

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public Boolean getPushNotoficationFlag() {
        return pushNotoficationFlag;
    }

    public void setPushNotoficationFlag(Boolean pushNotoficationFlag) {
        this.pushNotoficationFlag = pushNotoficationFlag;
    }

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

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channelLabel) {
        this.channelLabel = channelLabel;
    }

    public String getChannelValue() {
        return channelValue;
    }

    public void setChannelValue(String channelValue) {
        this.channelValue = channelValue;
    }


}