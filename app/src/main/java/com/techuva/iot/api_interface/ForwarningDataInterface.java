package com.techuva.iot.api_interface;

import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ForwarningPostParameters;
import com.techuva.iot.response_model.ForwarningMainObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ForwarningDataInterface {

    @POST(Constants.ForwarningDataCall)
    Call<ForwarningMainObject>  getStringScalar(@Body ForwarningPostParameters postParameter);

    @POST(Constants.ForwarningDataCall)
    Call<ForwarningMainObject>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body ForwarningPostParameters postParameter);

}
