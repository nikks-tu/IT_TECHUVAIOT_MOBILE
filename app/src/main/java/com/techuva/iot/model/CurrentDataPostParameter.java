package com.techuva.iot.model;

public class CurrentDataPostParameter {


    private String deviceId;
    private String userId;
    private String channelNo;
    private String fromDate;
    private String toDate;
    private String pageNumber;
    private String description;


    public CurrentDataPostParameter(String deviceId, String fromDate, String toDate, String pagePerCount, String pageNumber, String description) {
        this.deviceId = deviceId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
        this.description = description;
    }
    public CurrentDataPostParameter(String deviceId, String userId, String channelNo) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.channelNo = channelNo;
    }

    public CurrentDataPostParameter(String deviceId) {
        this.deviceId = deviceId;
    }

    public CurrentDataPostParameter(String deviceId, String userId) {
        this.deviceId = deviceId;
        this.userId = userId;
    }

    private String pagePerCount;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

}
