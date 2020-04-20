package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.GetConsumedDataPostParamter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetConsumedUnits {

    @POST(Constants.GetConsumedData)
    Call<JsonElement> getStringScalar(@Body GetConsumedDataPostParamter postParameter);

    @POST(Constants.GetConsumedData)
    Call<JsonElement> getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body GetConsumedDataPostParamter postParameter);
}
