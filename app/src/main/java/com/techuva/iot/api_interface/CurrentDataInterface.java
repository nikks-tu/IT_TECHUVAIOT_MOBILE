package com.techuva.iot.api_interface;

import com.google.gson.JsonElement;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.DevicesListPostParameter;
import com.techuva.iot.model.WaterMonCurrentDataPostParameter;
import com.techuva.iot.response_model.WaterMonMainObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface CurrentDataInterface {

    @POST(Constants.CurrentData)
    Call<CurrentDataMainObject>  getStringScalar(@Body CurrentDataPostParameter postParameter);

    @POST(Constants.CurrentData)
    Call<CurrentDataMainObject>  getStringScalarWithSession(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body CurrentDataPostParameter postParameter);

    @POST(Constants.GetWaterMonValues)
    Call<WaterMonMainObject>  getWaterMonValues(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body WaterMonCurrentDataPostParameter postParameter);

    @POST(Constants.CurrentDataForWaterMon)
    Call<JsonElement>  getCurrentDataForWaterMon(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body CurrentDataPostParameter postParameter);

    @POST(Constants.DataValueWaterMon)
    Call<JsonElement>  getAllDataWaterMon(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body WaterMonCurrentDataPostParameter postParameter);

    @POST(Constants.ChannelDataProcom)
    Call<JsonElement>  getChannelDataForProcom(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body CurrentDataPostParameter postParameter);

    @POST(Constants.OnOffWaterMotor)
    Call<JsonElement>  setOnOffMotor(@Header("authUser") int headerValue, @Header("authorization") String authorization, @Body DevicesListPostParameter postParameter);


}
