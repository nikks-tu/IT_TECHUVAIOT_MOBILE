package com.techuva.iot.model;

public class HblNotificationPostParameter {

    private String inventoryId;

    public HblNotificationPostParameter(String inventoryId, String channelNumber, String pagePerCount, String pageNumber) {
        this.inventoryId = inventoryId;
        this.channelNumber = channelNumber;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }

    private String channelNumber;
    private String pagePerCount;
    private String pageNumber;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(String pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }
}
