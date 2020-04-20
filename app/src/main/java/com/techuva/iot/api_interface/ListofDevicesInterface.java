package com.techuva.iot.api_interface;

import com.techuva.iot.app.Constants;
import com.techuva.iot.model.DevicesListMainObject;
import com.techuva.iot.model.DevicesListPostParameter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ListofDevicesInterface {

    @POST(Constants.ListofDevices)
    Call<DevicesListMainObject> getStringScalar(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DevicesListPostParameter postParameter);
}
