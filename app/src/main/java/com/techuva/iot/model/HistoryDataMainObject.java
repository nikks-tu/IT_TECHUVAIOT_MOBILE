package com.techuva.iot.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryDataMainObject {

    @SerializedName("info")
    @Expose
    private HistoryDataInfoObject iNFO;
    @SerializedName("result")
    @Expose
    private HistoryDataResultObject rESULT;

    public HistoryDataInfoObject getINFO() {
        return iNFO;
    }

    public void setINFO(HistoryDataInfoObject iNFO) {
        this.iNFO = iNFO;
    }

    public HistoryDataResultObject getRESULT() {
        return rESULT;
    }

    public void setRESULT(HistoryDataResultObject rESULT) {
        this.rESULT = rESULT;
    }

}
