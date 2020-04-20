package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumedDataValueObject {

    @SerializedName("DATE")
    @Expose
    private String dATE;
    @SerializedName("SUM")
    @Expose
    private String sUM;

    public String getDATE() {
        return dATE;
    }

    public void setDATE(String dATE) {
        this.dATE = dATE;
    }

    public String getSUM() {
        return sUM;
    }

    public void setSUM(String sUM) {
        this.sUM = sUM;
    }

}