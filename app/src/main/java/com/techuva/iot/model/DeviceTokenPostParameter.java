package com.techuva.iot.model;

public class DeviceTokenPostParameter {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DeviceTokenPostParameter(String token) {
        this.token = token;
    }

    private String token;

}
