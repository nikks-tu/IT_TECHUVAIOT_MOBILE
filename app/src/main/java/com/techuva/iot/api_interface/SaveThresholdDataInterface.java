package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.SaveThresholdPostParameter;
import com.techuva.iot.model.UpdateChannelPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface SaveThresholdDataInterface {

    @POST(Constants.SaveThresholdData)
    Call<JsonElement>  getStringScalar(@Body SaveThresholdPostParameter postParameter);

    @POST(Constants.SaveThresholdData)
    Call<JsonElement>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body SaveThresholdPostParameter postParameter);

    @POST(Constants.UpdateChannelDetails)
    Call<JsonElement>  updateChannelData(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body UpdateChannelPostParameters postParameter);

}
