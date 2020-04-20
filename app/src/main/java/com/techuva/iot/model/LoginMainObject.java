package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginMainObject {

    @SerializedName("info")
    @Expose
    private LoginInfoObject info;
    @SerializedName("result")
    @Expose
    private LoginResultObject result;

    public LoginInfoObject getInfo() {
        return info;
    }

    public void setInfo(LoginInfoObject info) {
        this.info = info;
    }

    public LoginResultObject getResult() {
        return result;
    }

    public void setResult(LoginResultObject result) {
        this.result = result;
    }


}
