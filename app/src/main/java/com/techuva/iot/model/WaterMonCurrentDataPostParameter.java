package com.techuva.iot.model;

public class WaterMonCurrentDataPostParameter {


    String deviceId;
    String fromDate;
    String toDate;
    String channelNumber;
    String date;
    String pagePerCount;
    String pageNumber;
    String flag;

    public WaterMonCurrentDataPostParameter(String deviceId, String fromDate, String toDate) {
        this.deviceId = deviceId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public WaterMonCurrentDataPostParameter(String deviceId, String channelNumber, String date, String pagePerCount, String pageNumber, String flag) {
        this.deviceId = deviceId;
        this.channelNumber = channelNumber;
        this.date = date;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
        this.flag = flag;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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


