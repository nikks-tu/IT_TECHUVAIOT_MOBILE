package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DevicesListResultObject {
    @SerializedName("ProductId")
    @Expose
    private Integer productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("Version")
    @Expose
    private String version;
    @SerializedName("InventoryList")
    @Expose
    private List<DevicesInventoryListObject> inventoryList = null;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DevicesInventoryListObject> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<DevicesInventoryListObject> inventoryList) {
        this.inventoryList = inventoryList;
    }

}
