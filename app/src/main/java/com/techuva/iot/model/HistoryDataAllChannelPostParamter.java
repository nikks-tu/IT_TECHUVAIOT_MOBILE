package com.techuva.iot.model;

public class HistoryDataAllChannelPostParamter {


    private String Searchval;
    private String FromDate;
    private String ToDate;
    private String pagepercount;
    private String pagenumber;

    private String deviceId;
    private String from_date;
    private String to_date;
    private String pagePerCount;
    private String pageNumber;



   /* public HistoryDataAllChannelPostParamter(String Searchval, String FromDate, String ToDate, String pagepercount, String pagenumber) {
        this.Searchval = Searchval;
        this.FromDate = FromDate;
        this.ToDate = ToDate;
        this.pagepercount = pagepercount;
        this.pagenumber = pagenumber;
    }*/

    public HistoryDataAllChannelPostParamter(String deviceId, String  from_date, String to_date, String pagePerCount, String pageNumber)
    {
        this.deviceId = deviceId;
        this.from_date = from_date;
        this.to_date = to_date;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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




    public String getSearchval() {
        return Searchval;
    }

    public void setSearchval(String searchval) {
        Searchval = searchval;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getPagepercount() {
        return pagepercount;
    }

    public void setPagepercount(String pagepercount) {
        this.pagepercount = pagepercount;
    }

    public String getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }

}
