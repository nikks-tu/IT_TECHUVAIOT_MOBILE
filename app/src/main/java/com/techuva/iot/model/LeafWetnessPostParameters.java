package com.techuva.iot.model;

public class LeafWetnessPostParameters {

    private String channelId;
    private String deviceId;
    private String dataReceivedDate;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDataReceivedDate() {
        return dataReceivedDate;
    }

    public void setDataReceivedDate(String dataReceivedDate) {
        this.dataReceivedDate = dataReceivedDate;
    }


    public LeafWetnessPostParameters(String channelId, String deviceId, String dataReceivedDate) {
        this.channelId = channelId;
        this.deviceId = deviceId;
        this.dataReceivedDate = dataReceivedDate;
    }

}
