package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionInfoResultObject  {

    @SerializedName("fontVersionStatus")
    @Expose
    private Boolean fontVersionStatus;
    @SerializedName("CurrentAppVersion")
    @Expose
    private Double currentAppVersion;
    @SerializedName("FontLink")
    @Expose
    private String fontLink;
    @SerializedName("AppVersionStatus")
    @Expose
    private Boolean appVersionStatus;
    @SerializedName("versionNumber")
    @Expose
    private Double versionNumber;

    public Boolean getFontVersionStatus() {
        return fontVersionStatus;
    }

    public void setFontVersionStatus(Boolean fontVersionStatus) {
        this.fontVersionStatus = fontVersionStatus;
    }

    public Double getCurrentAppVersion() {
        return currentAppVersion;
    }

    public void setCurrentAppVersion(Double currentAppVersion) {
        this.currentAppVersion = currentAppVersion;
    }

    public String getFontLink() {
        return fontLink;
    }

    public void setFontLink(String fontLink) {
        this.fontLink = fontLink;
    }

    public Boolean getAppVersionStatus() {
        return appVersionStatus;
    }

    public void setAppVersionStatus(Boolean appVersionStatus) {
        this.appVersionStatus = appVersionStatus;
    }

    public Double getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Double versionNumber) {
        this.versionNumber = versionNumber;
    }

}