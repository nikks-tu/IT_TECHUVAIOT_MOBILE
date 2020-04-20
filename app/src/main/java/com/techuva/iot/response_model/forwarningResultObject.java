package com.techuva.iot.response_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class forwarningResultObject  {

    @SerializedName("CHANNEL_NUMBER")
    @Expose
    private Integer cHANNELNUMBER;
    @SerializedName("CHANNEL_ICON")
    @Expose
    private String cHANNELICON;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("CHANNEL_LABEL")
    @Expose
    private String cHANNELLABEL;
    @SerializedName("UNIT_OF_MEASURE")
    @Expose
    private String uNITOFMEASURE;

    public Integer getCHANNELNUMBER() {
        return cHANNELNUMBER;
    }

    public void setCHANNELNUMBER(Integer cHANNELNUMBER) {
        this.cHANNELNUMBER = cHANNELNUMBER;
    }

    public String getCHANNELICON() {
        return cHANNELICON;
    }

    public void setCHANNELICON(String cHANNELICON) {
        this.cHANNELICON = cHANNELICON;
    }

    public String  getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCHANNELLABEL() {
        return cHANNELLABEL;
    }

    public void setCHANNELLABEL(String cHANNELLABEL) {
        this.cHANNELLABEL = cHANNELLABEL;
    }

    public String getUNITOFMEASURE() {
        return uNITOFMEASURE;
    }

    public void setUNITOFMEASURE(String uNITOFMEASURE) {
        this.uNITOFMEASURE = uNITOFMEASURE;
    }

}