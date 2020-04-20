package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.HistoryDataMainObject;
import com.techuva.iot.model.HistoryDataPostParamter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface HistoryDataInterface {

    @POST(Constants.HistoryData)
    Call<HistoryDataMainObject>  getStringScalar(@Body HistoryDataPostParamter postParameter);

    @POST(Constants.HistoryData)
    Call<HistoryDataMainObject>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body HistoryDataPostParamter postParameter);

    @POST(Constants.MotorOnOffSummary)
    Call<JsonElement>  getMotorOnOffSummary(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body CurrentDataPostParameter postParameter);

}
