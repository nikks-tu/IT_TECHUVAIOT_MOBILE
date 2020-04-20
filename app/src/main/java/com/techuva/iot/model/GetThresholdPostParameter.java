package com.techuva.iot.model;

public class GetThresholdPostParameter {

    private String pagePerCount;
    private String pageNumber;
    private String inventoryId;


    public GetThresholdPostParameter(String inventoryId, String pagePerCount, String pageNumber) {
        this.inventoryId = inventoryId;
        this.pagePerCount = pagePerCount;
        this.pageNumber = pageNumber;
    }


    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getPagePerCount() {
        return pagePerCount;
    }

    public void setPagePerCount(String pagePerCount) {
        this.pagePerCount = pagePerCount;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

}
