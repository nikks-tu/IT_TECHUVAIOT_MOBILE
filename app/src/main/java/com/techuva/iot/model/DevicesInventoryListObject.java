package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DevicesInventoryListObject {
    @SerializedName("InventoryId")
    @Expose
    private Integer inventoryId;
    @SerializedName("InventoryName")
    @Expose
    private String inventoryName;
    @SerializedName("InventoryTypeId")
    @Expose
    private Integer inventoryTypeId;
    @SerializedName("InventoryTypeName")
    @Expose
    private String inventoryTypeName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("DisplayName")
    @Expose
    private String displayName;
    @SerializedName("TempateId")
    @Expose
    private Integer tempateId;
    @SerializedName("TemplateRef")
    @Expose
    private String templateRef;
    @SerializedName("ProvisionedOn")
    @Expose
    private String provisionedOn;

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getInventoryTypeId() {
        return inventoryTypeId;
    }

    public void setInventoryTypeId(Integer inventoryTypeId) {
        this.inventoryTypeId = inventoryTypeId;
    }

    public String getInventoryTypeName() {
        return inventoryTypeName;
    }

    public void setInventoryTypeName(String inventoryTypeName) {
        this.inventoryTypeName = inventoryTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getTempateId() {
        return tempateId;
    }

    public void setTempateId(Integer tempateId) {
        this.tempateId = tempateId;
    }

    public String getTemplateRef() {
        return templateRef;
    }

    public void setTemplateRef(String templateRef) {
        this.templateRef = templateRef;
    }

    public String getProvisionedOn() {
        return provisionedOn;
    }

    public void setProvisionedOn(String provisionedOn) {
        this.provisionedOn = provisionedOn;
    }

}
