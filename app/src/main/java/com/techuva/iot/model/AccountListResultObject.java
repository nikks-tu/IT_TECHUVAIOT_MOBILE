package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountListResultObject {
    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("CompanyMail")
    @Expose
    private String companyMail;
    @SerializedName("PocUserName")
    @Expose
    private String pocUserName;
    @SerializedName("UserId")
    @Expose
    private String UserId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyMail() {
        return companyMail;
    }

    public void setCompanyMail(String companyMail) {
        this.companyMail = companyMail;
    }

    public String getPocUserName() {
        return pocUserName;
    }

    public void setPocUserName(String pocUserName) {
        this.pocUserName = pocUserName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
