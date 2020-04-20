package com.techuva.iot.model;

public class GetConsumedDataPostParamter {


    private String date;
    private String toDate;
    private String inventoryName;

    public GetConsumedDataPostParamter(String date, String toDate, String inventoryName) {
        this.date = date;
        this.toDate = toDate;
        this.inventoryName = inventoryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }
}
