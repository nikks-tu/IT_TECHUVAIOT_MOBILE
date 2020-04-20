package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetChannelDataWMotor{

    @SerializedName("INVENTORY_ID")
    @Expose
    private Integer iNVENTORYID;
    @SerializedName("INVENTORY_NAME")
    @Expose
    private String iNVENTORYNAME;
    @SerializedName("DISPLAY_NAME")
    @Expose
    private String dISPLAYNAME;
    @SerializedName("CHANNEL_NUMBER")
    @Expose
    private Integer cHANNELNUMBER;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("CHANNEL_DESCRIPTION")
    @Expose
    private String cHANNELDESCRIPTION;
    @SerializedName("CHANNEL_LABEL")
    @Expose
    private String cHANNELLABEL;
    @SerializedName("CHANNEL_ICON")
    @Expose
    private String cHANNELICON;
    @SerializedName("CHANNEL_DEFAULT_VALUE")
    @Expose
    private String cHANNELDEFAULTVALUE;
    @SerializedName("MinValue")
    @Expose
    private int minValue;
    @SerializedName("MaxValue")
    @Expose
    private int maxValue;
    @SerializedName("UNIT_OF_MEASURE")
    @Expose
    private String uNITOFMEASURE;

    public Integer getINVENTORYID() {
        return iNVENTORYID;
    }

    public void setINVENTORYID(Integer iNVENTORYID) {
        this.iNVENTORYID = iNVENTORYID;
    }

    public String getINVENTORYNAME() {
        return iNVENTORYNAME;
    }

    public void setINVENTORYNAME(String iNVENTORYNAME) {
        this.iNVENTORYNAME = iNVENTORYNAME;
    }

    public String getDISPLAYNAME() {
        return dISPLAYNAME;
    }

    public void setDISPLAYNAME(String dISPLAYNAME) {
        this.dISPLAYNAME = dISPLAYNAME;
    }

    public Integer getCHANNELNUMBER() {
        return cHANNELNUMBER;
    }

    public void setCHANNELNUMBER(Integer cHANNELNUMBER) {
        this.cHANNELNUMBER = cHANNELNUMBER;
    }

    public String getCHANNELDESCRIPTION() {
        return cHANNELDESCRIPTION;
    }

    public void setCHANNELDESCRIPTION(String cHANNELDESCRIPTION) {
        this.cHANNELDESCRIPTION = cHANNELDESCRIPTION;
    }

    public String getCHANNELLABEL() {
        return cHANNELLABEL;
    }

    public void setCHANNELLABEL(String cHANNELLABEL) {
        this.cHANNELLABEL = cHANNELLABEL;
    }

    public String getCHANNELICON() {
        return cHANNELICON;
    }

    public void setCHANNELICON(String cHANNELICON) {
        this.cHANNELICON = cHANNELICON;
    }

    public String getCHANNELDEFAULTVALUE() {
        return cHANNELDEFAULTVALUE;
    }

    public void setCHANNELDEFAULTVALUE(String cHANNELDEFAULTVALUE) {
        this.cHANNELDEFAULTVALUE = cHANNELDEFAULTVALUE;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public String getUNITOFMEASURE() {
        return uNITOFMEASURE;
    }

    public void setUNITOFMEASURE(String uNITOFMEASURE) {
        this.uNITOFMEASURE = uNITOFMEASURE;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}