package com.techuva.iot.model;

import java.util.List;

public class HistoryViewPaginationObject {
    int lastLoadedPage;
    int lastLoadedItemCount;
    String channelName;
    int displayValue;
    int channelNumber;

    private List<HistoryDataValueObject> data = null;

    public List<HistoryDataValueObject> getData() {
        return data;
    }

    public int getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(int displayValue) {
        this.displayValue = displayValue;
    }

    public void setData(List<HistoryDataValueObject> data) {
        this.data = data;
    }

    public int getLastLoadedPage() {
        return lastLoadedPage;
    }

    public void setLastLoadedPage(int lastLoadedPage) {
        this.lastLoadedPage = lastLoadedPage;
    }

    public int getLastLoadedItemCount() {
        return lastLoadedItemCount;
    }

    public void setLastLoadedItemCount(int lastLoadedItemCount) {
        this.lastLoadedItemCount = lastLoadedItemCount;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

}
