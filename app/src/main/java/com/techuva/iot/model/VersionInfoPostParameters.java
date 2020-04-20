package com.techuva.iot.model;

public class VersionInfoPostParameters {


    private String inventoryId;
    private String appVersion;
    private String fontVersion;


    public VersionInfoPostParameters(String inventoryId, String appVersion, String fontVersion) {
        this.inventoryId = inventoryId;
        this.appVersion = appVersion;
        this.fontVersion = fontVersion;
    }


    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getFontVersion() {
        return fontVersion;
    }

    public void setFontVersion(String fontVersion) {
        this.fontVersion = fontVersion;
    }

}
