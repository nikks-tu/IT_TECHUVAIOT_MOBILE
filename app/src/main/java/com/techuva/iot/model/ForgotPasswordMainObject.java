package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordMainObject {

    @SerializedName("info")
    @Expose
    private ForgotPasswordInfoObject info;

    @SerializedName("result")
    @Expose
    private String result;

    public ForgotPasswordInfoObject getInfo() {
        return info;
    }

    public void setInfo(ForgotPasswordInfoObject info) {
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


}
