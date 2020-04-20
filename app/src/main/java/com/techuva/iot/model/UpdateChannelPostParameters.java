package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateChannelPostParameters {

    @SerializedName("INVENTORY_ID")
    @Expose
    private Integer INVENTORY_ID;
    @SerializedName("INVENTORY_NAME")
    @Expose
    private String INVENTORY_NAME;
    @SerializedName("DISPLAY_NAME")
    @Expose
    private String DISPLAY_NAME;
    @SerializedName("CHANNEL_NUMBER")
    @Expose
    private Integer CHANNEL_NUMBER;
    @SerializedName("CHANNEL_DESCRIPTION")
    @Expose
    private String CHANNEL_DESCRIPTION;
    @SerializedName("CHANNEL_LABEL")
    @Expose
    private String CHANNEL_LABEL;
    @SerializedName("CHANNEL_ICON")
    @Expose
    private String CHANNEL_ICON;
    @SerializedName("CHANNEL_DEFAULT_VALUE")
    @Expose
    private String CHANNEL_DEFAULT_VALUE;
    @SerializedName("MinValue")
    @Expose
    private int MinValue;

    public Integer getINVENTORY_ID() {
        return INVENTORY_ID;
    }

    public void setINVENTORY_ID(Integer INVENTORY_ID) {
        this.INVENTORY_ID = INVENTORY_ID;
    }

    public String getINVENTORY_NAME() {
        return INVENTORY_NAME;
    }

    public void setINVENTORY_NAME(String INVENTORY_NAME) {
        this.INVENTORY_NAME = INVENTORY_NAME;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public Integer getCHANNEL_NUMBER() {
        return CHANNEL_NUMBER;
    }

    public void setCHANNEL_NUMBER(Integer CHANNEL_NUMBER) {
        this.CHANNEL_NUMBER = CHANNEL_NUMBER;
    }

    public String getCHANNEL_DESCRIPTION() {
        return CHANNEL_DESCRIPTION;
    }

    public void setCHANNEL_DESCRIPTION(String CHANNEL_DESCRIPTION) {
        this.CHANNEL_DESCRIPTION = CHANNEL_DESCRIPTION;
    }

    public String getCHANNEL_LABEL() {
        return CHANNEL_LABEL;
    }

    public void setCHANNEL_LABEL(String CHANNEL_LABEL) {
        this.CHANNEL_LABEL = CHANNEL_LABEL;
    }

    public String getCHANNEL_ICON() {
        return CHANNEL_ICON;
    }

    public void setCHANNEL_ICON(String CHANNEL_ICON) {
        this.CHANNEL_ICON = CHANNEL_ICON;
    }

    public String getCHANNEL_DEFAULT_VALUE() {
        return CHANNEL_DEFAULT_VALUE;
    }

    public void setCHANNEL_DEFAULT_VALUE(String CHANNEL_DEFAULT_VALUE) {
        this.CHANNEL_DEFAULT_VALUE = CHANNEL_DEFAULT_VALUE;
    }

    public int getMinValue() {
        return MinValue;
    }

    public void setMinValue(int minValue) {
        MinValue = minValue;
    }

    public int getMaxValue() {
        return MaxValue;
    }

    public void setMaxValue(int maxValue) {
        MaxValue = maxValue;
    }

    public String getUNIT_OF_MEASURE() {
        return UNIT_OF_MEASURE;
    }

    public void setUNIT_OF_MEASURE(String UNIT_OF_MEASURE) {
        this.UNIT_OF_MEASURE = UNIT_OF_MEASURE;
    }

    @SerializedName("MaxValue")
    @Expose
    private int MaxValue;

    public UpdateChannelPostParameters(Integer INVENTORY_ID, String INVENTORY_NAME, String DISPLAY_NAME, Integer CHANNEL_NUMBER, String CHANNEL_DESCRIPTION, String CHANNEL_LABEL, String CHANNEL_ICON, String CHANNEL_DEFAULT_VALUE, int minValue, int maxValue, String UNIT_OF_MEASURE) {
        this.INVENTORY_ID = INVENTORY_ID;
        this.INVENTORY_NAME = INVENTORY_NAME;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.CHANNEL_NUMBER = CHANNEL_NUMBER;
        this.CHANNEL_DESCRIPTION = CHANNEL_DESCRIPTION;
        this.CHANNEL_LABEL = CHANNEL_LABEL;
        this.CHANNEL_ICON = CHANNEL_ICON;
        this.CHANNEL_DEFAULT_VALUE = CHANNEL_DEFAULT_VALUE;
        MinValue = minValue;
        MaxValue = maxValue;
        this.UNIT_OF_MEASURE = UNIT_OF_MEASURE;
    }

    @SerializedName("UNIT_OF_MEASURE")
    @Expose
    private String UNIT_OF_MEASURE;



}