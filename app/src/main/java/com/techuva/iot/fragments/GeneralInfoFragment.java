package com.techuva.iot.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.activity.TokenExpireActivity;
import com.techuva.iot.adapter.GridAdapterGeneralInfo;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.response_model.ProcomChannelDataResultObject;
import com.techuva.iot.utils.views.MApplication;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GeneralInfoFragment extends Fragment {
    // TODO: Rename and change types of parameters
    GridView grid_bottom;
    int UserId;
    ArrayList<ProcomChannelDataResultObject> resultObjectArrayList;
    ProcomChannelDataResultObject dataObject;
    private String deviceId, userId, authorityKey, channelNumbers;
    GridAdapterGeneralInfo gridAdapterDashboard;

    private OnFragmentInteractionListener mListener;

    public GeneralInfoFragment() {
        // Required empty public constructor
    }

    public static GeneralInfoFragment newInstance(String param1, String param2) {
        GeneralInfoFragment fragment = new GeneralInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserId = Integer.parseInt(MApplication.getString(getContext(), Constants.UserID));
        deviceId = MApplication.getString(getContext(), Constants.DeviceID);
        userId = MApplication.getString(getContext(), Constants.UserID);
        channelNumbers = "67,68";
        authorityKey ="Bearer " +MApplication.getString(getContext(), Constants.AccessToken);
        //serviceCall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_general_info, null, false);
        // Inflate the layout for this fragment
        grid_bottom = contentView.findViewById(R.id.grid_bottom);
        serviceCall();
        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        //Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void onFragmentInteraction(Uri uri);
    }


    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);

        //Call<CurrentDataMainObject> call = service.getStringScalar(new CurrentDataPostParameter(deviceId,userId));
        Call<JsonElement> call = service.getChannelDataForProcom(UserId, authorityKey, new CurrentDataPostParameter(deviceId,userId, channelNumbers));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                if (response.code()==401)
                {
                    MApplication.setBoolean(getContext(), Constants.IsSessionExpired, true);
                    Intent intent = new Intent(getContext(), TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    int errorCode = infoObject.get("ErrorCode").getAsInt();
                    if(errorCode==0)
                    {
                        JsonArray result = jsonObject.get("result").getAsJsonArray();
                        resultObjectArrayList = new ArrayList<>();
                        for (int i=0; i<result.size(); i++)
                        {
                            JsonObject object = result.get(i).getAsJsonObject();
                            int channelNumber = Integer.parseInt(object.get("ChannelNumber").getAsString());
                            if(channelNumber<100)
                            {
                                dataObject = new ProcomChannelDataResultObject();
                                dataObject.setChannelNumber(object.get("ChannelNumber").getAsInt());
                                dataObject.setLabel(object.get("Label").getAsString());
                                dataObject.setValue(object.get("Value").getAsString());
                                dataObject.setIcon(object.get("icon").getAsString());
                                dataObject.setDate(object.get("date").getAsString());
                                dataObject.setTime(object.get("time").getAsString());
                                dataObject.setDisplayOrder(object.get("display_order").getAsInt());
                                dataObject.setUnitOfMeasure(object.get("unit_of_measure").getAsString());
                                resultObjectArrayList.add(dataObject);
                            }
                        }
                        try {
                            dataResponse(resultObjectArrayList);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }else {
                    Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //  Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void dataResponse(ArrayList<ProcomChannelDataResultObject> object) throws java.text.ParseException {
         if(object!=null && object.size()>0)
        {
            gridAdapterDashboard = new GridAdapterGeneralInfo(Objects.requireNonNull(getContext()), R.layout.grid_item_general_info, object);
            grid_bottom.setAdapter(gridAdapterDashboard);
        }
    }
}
