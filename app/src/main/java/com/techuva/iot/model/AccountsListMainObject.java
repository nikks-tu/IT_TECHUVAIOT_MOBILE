package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountsListMainObject {
    @SerializedName("info")
    @Expose
    private AccountListInfoObject info;
    @SerializedName("result")
    @Expose
    private List<AccountListResultObject> result = null;

    public AccountListInfoObject getInfo() {
        return info;
    }

    public void setInfo(AccountListInfoObject info) {
        this.info = info;
    }

    public List<AccountListResultObject> getResult() {
        return result;
    }

    public void setResult(List<AccountListResultObject> result) {
        this.result = result;
    }

}