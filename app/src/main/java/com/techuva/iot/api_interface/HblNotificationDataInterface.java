package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.HblNotificationPostParameter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface HblNotificationDataInterface {

    @POST(Constants.GetHBLNotifications)
    Call<JsonElement>  getStringScalar(@Body HblNotificationPostParameter postParameter);

    @POST(Constants.GetHBLNotifications)
    Call<JsonElement>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body HblNotificationPostParameter postParameter);

}
