package com.techuva.iot.api_interface;

import com.techuva.iot.app.Constants;
import com.techuva.iot.model.CurrentDataAgriPostParameter;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface CurrentAgriDataInterface {

    @POST(Constants.CurrentDataAgri)
    Call<CurrentDataMainObject>  getStringScalar(@Body CurrentDataPostParameter postParameter);

    @POST(Constants.CurrentData)
    Call<CurrentDataMainObject>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body CurrentDataAgriPostParameter postParameter);

}
