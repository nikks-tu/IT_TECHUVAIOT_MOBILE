package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.GetThresholdPostParameter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetThresholdData {

    @POST(Constants.GetHBLThresholdList)
    Call<JsonElement> getStringScalar(@Body GetThresholdPostParameter postParameter);

    @POST(Constants.GetHBLThresholdList)
    Call<JsonElement> getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body GetThresholdPostParameter postParameter);
}
