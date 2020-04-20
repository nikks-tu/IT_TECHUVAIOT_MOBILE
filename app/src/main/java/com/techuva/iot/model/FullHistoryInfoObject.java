package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FullHistoryInfoObject{

    @SerializedName("ErrorCode")
    @Expose
    private Integer errorcode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errormessage;
    @SerializedName("Value")
    @Expose
    private Object value;
    @SerializedName("ListCount")
    @Expose
    private String listCount;
    @SerializedName("PageNumber")
    @Expose
    private String pageNumber;
    @SerializedName("PagePerCount")
    @Expose
    private String pagePerCount;
    @SerializedName("FromRecords")
    @Expose
    private String fromRecords;
    @SerializedName("ToRecords")
    @Expose
    private String toRecords;
    @SerializedName("TotalRecords")
    @Expose
    private String totalRecords;

    public Integer getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(Integer errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getListCount() {
        return listCount;
    }

    public void setListCount(String listCount) {
        this.listCount = listCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(String pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public String getFromRecords() {
        return fromRecords;
    }

    public void setFromRecords(String fromRecords) {
        this.fromRecords = fromRecords;
    }

    public String getToRecords() {
        return toRecords;
    }

    public void setToRecords(String toRecords) {
        this.toRecords = toRecords;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }


}