package com.techuva.iot.model;

public class DevicesListPostParameter {

    private String userId;
    private int companyId;
    private String refType;

    private String id;
    private String status;


    public DevicesListPostParameter(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public DevicesListPostParameter(String userId, int companyId, String refType) {
        this.userId = userId;
        this.companyId = companyId;
        this.refType = refType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
