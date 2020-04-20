package com.techuva.iot.response_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentDataResponseModel {


    @SerializedName("INFO")
        private INFO info;
        //Results
        @SerializedName("RESULT")
        private RESULT results;


    public INFO getInfo() {
        return info;
    }

    public RESULT getResults() {
        return results;
    }


        public class INFO{

            //errorcode
            @SerializedName("errorCode")
            private String errorCode;
            //errormessage
            @SerializedName("errorMessage")
            private String errorMessage;

            public String getErrorCode() {
                return errorCode;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

        }

        public class RESULT{

            //DeviceID
            @SerializedName("DeviceID")
            private String DeviceID;

            //DeviceName
            @SerializedName("DeviceName")
            private String DeviceName;

            //Values
            @SerializedName("Values")
            List<Values> Values;


            public String getDeviceID() {
                return DeviceID;
            }

            public String getDeviceName() {
                return DeviceName;
            }

            public List<Values> getValues() {
                return Values;
            }
        }

        public class Values{

            //date
            @SerializedName("date")
            private String date;
            //ChannelNumber
            @SerializedName("ChannelNumber")
            private String ChannelNumber;
            //Label
            @SerializedName("Label")
            private String Label;
            //Value
            @SerializedName("Value")
            private String Value;
            //icon
            @SerializedName("icon")
            private String icon;
            //display_order
            @SerializedName("display_order")
            private String display_order;
            //time
            @SerializedName("time")
            private String time;

            public String getDate() {
                return date;
            }

            public String getChannelNumber() {
                return ChannelNumber;
            }

            public String getLabel() {
                return Label;
            }

            public String getValue() {
                return Value;
            }

            public String getIcon() {
                return icon;
            }

            public String getDisplay_order() {
                return display_order;
            }

            public String getTime() {
                return time;
            }

        }

}
