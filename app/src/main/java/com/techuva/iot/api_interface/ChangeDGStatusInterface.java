package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ChangeDGStatusPostParameter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChangeDGStatusInterface {

    @POST(Constants.ChangeDGStatus)
    Call<JsonElement> getStringScalar(@Body ChangeDGStatusPostParameter postParameter);

    @POST(Constants.ChangeDGStatus)
    Call<JsonElement> getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body ChangeDGStatusPostParameter postParameter);
}
