package com.techuva.iot.response_model;

public class ConsumedDataHistoryObject {

    public String channelValue;
    public String units;
    public String date;
/*
    public ConsumedDataHistoryObject(String channelValue, String date) {
        this.channelValue = channelValue;
        this.date = date;
    }*/

    public String getChannelValue() {
        return channelValue;
    }

    public void setChannelValue(String channelValue) {
        this.channelValue = channelValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }


}
