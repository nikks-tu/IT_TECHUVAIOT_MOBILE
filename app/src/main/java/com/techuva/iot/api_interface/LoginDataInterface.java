package com.techuva.iot.api_interface;

import com.techuva.iot.app.Constants;
import com.techuva.iot.model.LoginMainObject;
import com.techuva.iot.model.LoginPostParameters;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.Call;


public interface LoginDataInterface {

    @POST(Constants.LoginData)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<LoginMainObject> getStringScalar(@Body LoginPostParameters postParameter);


    @FormUrlEncoded
    @POST(Constants.LoginData)
    Call<LoginMainObject> loginCall(@Header("authorization") String authorization, @Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);

    @FormUrlEncoded
    @POST(Constants.LoginData)
    Call<LoginMainObject> refreshSession(@Header("authorization") String authorization, @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token );



}
