package com.techuva.iot.model;

public class ChangeDGStatusPostParameter {

    private String DEVICE_NAME;
    private String STATUS;

    public ChangeDGStatusPostParameter(String DEVICE_NAME, String STATUS) {
        this.DEVICE_NAME = DEVICE_NAME;
        this.STATUS = STATUS;
    }

    public String getDEVICE_NAME() {
        return DEVICE_NAME;
    }

    public void setDEVICE_NAME(String DEVICE_NAME) {
        this.DEVICE_NAME = DEVICE_NAME;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }



}
