package com.techuva.iot.model;

public class SaveThresholdPostParameter {

    private String INVENTORY_ID;
    private String THRESHOLD_VALUE;
    private String THRESHOLD_NAME;
    private String CHANNEL_NUMBER;

    public SaveThresholdPostParameter(String INVENTORY_ID, String THRESHOLD_VALUE, String THRESHOLD_NAME, String CHANNEL_NUMBER) {
        this.INVENTORY_ID = INVENTORY_ID;
        this.THRESHOLD_VALUE = THRESHOLD_VALUE;
        this.THRESHOLD_NAME = THRESHOLD_NAME;
        this.CHANNEL_NUMBER = CHANNEL_NUMBER;
    }


    public String getINVENTORY_ID() {
        return INVENTORY_ID;
    }

    public void setINVENTORY_ID(String INVENTORY_ID) {
        this.INVENTORY_ID = INVENTORY_ID;
    }

    public String getTHRESHOLD_VALUE() {
        return THRESHOLD_VALUE;
    }

    public void setTHRESHOLD_VALUE(String THRESHOLD_VALUE) {
        this.THRESHOLD_VALUE = THRESHOLD_VALUE;
    }

    public String getTHRESHOLD_NAME() {
        return THRESHOLD_NAME;
    }

    public void setTHRESHOLD_NAME(String THRESHOLD_NAME) {
        this.THRESHOLD_NAME = THRESHOLD_NAME;
    }

    public String getCHANNEL_NUMBER() {
        return CHANNEL_NUMBER;
    }

    public void setCHANNEL_NUMBER(String CHANNEL_NUMBER) {
        this.CHANNEL_NUMBER = CHANNEL_NUMBER;
    }

}
