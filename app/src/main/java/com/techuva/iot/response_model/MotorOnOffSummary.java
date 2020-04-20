package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MotorOnOffSummary  {

    @SerializedName("RECEIVED_TIME")
    @Expose
    private String rECEIVEDTIME;
    @SerializedName("STATUS")
    @Expose
    private String sTATUS;
    @SerializedName("REASON")
    @Expose
    private String rEASON;

    public String getRECEIVEDTIME() {
        return rECEIVEDTIME;
    }

    public void setRECEIVEDTIME(String rECEIVEDTIME) {
        this.rECEIVEDTIME = rECEIVEDTIME;
    }

    public String getSTATUS() {
        return sTATUS;
    }

    public void setSTATUS(String sTATUS) {
        this.sTATUS = sTATUS;
    }

    public String getrEASON() {
        return rEASON;
    }

    public void setrEASON(String rEASON) {
        this.rEASON = rEASON;
    }


}