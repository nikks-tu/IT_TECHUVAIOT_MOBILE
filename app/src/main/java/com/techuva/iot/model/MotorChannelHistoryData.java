package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MotorChannelHistoryData {

    @SerializedName("BATCH_ID")
    @Expose
    private Integer bATCHID;
    @SerializedName("INVENTORY_NAME")
    @Expose
    private String iNVENTORYNAME;
    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("3")
    @Expose
    private String _3;
    @SerializedName("4")
    @Expose
    private String _4;
    @SerializedName("RECEIVED_TIME")
    @Expose
    private String rECEIVEDTIME;

    public Integer getBATCHID() {
        return bATCHID;
    }

    public void setBATCHID(Integer bATCHID) {
        this.bATCHID = bATCHID;
    }

    public String getINVENTORYNAME() {
        return iNVENTORYNAME;
    }

    public void setINVENTORYNAME(String iNVENTORYNAME) {
        this.iNVENTORYNAME = iNVENTORYNAME;
    }

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    public String get2() {
        return _2;
    }

    public void set2(String _2) {
        this._2 = _2;
    }

    public String get3() {
        return _3;
    }

    public void set3(String _3) {
        this._3 = _3;
    }

    public String get4() {
        return _4;
    }

    public void set4(String _4) {
        this._4 = _4;
    }

    public String getRECEIVEDTIME() {
        return rECEIVEDTIME;
    }

    public void setRECEIVEDTIME(String rECEIVEDTIME) {
        this.rECEIVEDTIME = rECEIVEDTIME;
    }

}