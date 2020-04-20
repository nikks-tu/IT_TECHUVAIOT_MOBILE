package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThresholdInfoObject {

    @SerializedName("ListCount")
    @Expose
    private Integer listCount;
    @SerializedName("PageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("PagePerCount")
    @Expose
    private Integer pagePerCount;
    @SerializedName("FromRecords")
    @Expose
    private Integer fromRecords;
    @SerializedName("ToRecords")
    @Expose
    private Integer toRecords;
    @SerializedName("TotalRecords")
    @Expose
    private Integer totalRecords;
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;

    public Integer getListCount() {
        return listCount;
    }

    public void setListCount(Integer listCount) {
        this.listCount = listCount;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(Integer pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public Integer getFromRecords() {
        return fromRecords;
    }

    public void setFromRecords(Integer fromRecords) {
        this.fromRecords = fromRecords;
    }

    public Integer getToRecords() {
        return toRecords;
    }

    public void setToRecords(Integer toRecords) {
        this.toRecords = toRecords;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}