package com.techuva.iot.model;

public class HistoryDataPostParamter {


    private String deviceId;
    private String from_date;
    private String to_date;
    private String pagePerCount;
    private String pageNumber;

    public HistoryDataPostParamter(String deviceId, String from_date, String to_date, String pagePerCount, String pageNumber) {
        this.deviceId = deviceId;
        this.from_date = from_date;
        this.to_date = to_date;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
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



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFromDate() {
        return from_date;
    }

    public void setFromDate(String fromDate) {
        this.from_date = fromDate;
    }

    public String getToDate() {
        return to_date;
    }

    public void setToDate(String toDate) {
        this.to_date = toDate;
    }


}
