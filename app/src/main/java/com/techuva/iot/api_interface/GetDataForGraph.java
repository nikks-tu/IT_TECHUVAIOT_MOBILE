package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.GraphDataPostParamter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GetDataForGraph {

    @POST(Constants.GetGraphData)
    Call<JsonElement> getStringScalar(@Body GraphDataPostParamter postParameter);

    @POST(Constants.GetGraphData)
    Call<JsonElement> getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body GraphDataPostParamter postParameter);
}
