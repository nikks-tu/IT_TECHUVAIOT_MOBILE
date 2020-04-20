package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordInfoObject  {

    @SerializedName("ListCount")
    @Expose
    private Integer listCount;
    @SerializedName("ErrorCode")
    @Expose
    private Integer errorCode;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("PageNumber")
    @Expose
    private Object pageNumber;
    @SerializedName("PagePerCount")
    @Expose
    private Object pagePerCount;
    @SerializedName("FromRecords")
    @Expose
    private Integer fromRecords;
    @SerializedName("ToRecords")
    @Expose
    private Integer toRecords;
    @SerializedName("TotalRecords")
    @Expose
    private Integer totalRecords;

    public Integer getListCount() {
        return listCount;
    }

    public void setListCount(Integer listCount) {
        this.listCount = listCount;
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

    public Object getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Object pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Object getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(Object pagePerCount) {
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

}