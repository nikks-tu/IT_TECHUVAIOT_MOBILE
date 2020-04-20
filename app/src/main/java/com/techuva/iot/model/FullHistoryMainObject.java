package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FullHistoryMainObject   {

    @SerializedName("info")
    @Expose
    private FullHistoryInfoObject info;
    @SerializedName("result")
    @Expose
    private List<FullHistoryResultObject> result = null;

    public FullHistoryInfoObject getINFO() {
        return info;
    }

    public void setInfo(FullHistoryInfoObject info) {
        this.info = info;
    }

    public  List<FullHistoryResultObject>  getRESULT() {
        return result;
    }

    public void setResult(List<FullHistoryResultObject> result) {
        this.result = result;
    }

}