package com.techuva.iot.model;

public class GraphDataPostParamter {


    private String date;
    private String inventoryName;
    private String channelNumber;

    public GraphDataPostParamter(String date, String inventoryName, String channelNumber) {
        this.date = date;
        this.inventoryName = inventoryName;
        this.channelNumber = channelNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

}
