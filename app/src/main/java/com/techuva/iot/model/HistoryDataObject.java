package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryDataObject {

    @SerializedName("channel_number")
    @Expose
    private String channelNumber;
    @SerializedName("Label")
    @Expose
    private String label;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("display_order")
    @Expose
    private Integer displayOrder;
    @SerializedName("Values")
    @Expose
    private List<HistoryDataValueObject> values = null;

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public List<HistoryDataValueObject> getValues() {
        return values;
    }

    public void setValues(List<HistoryDataValueObject> values) {
        this.values = values;
    }

}