package com.techuva.iot.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.techuva.iot.R;
import com.techuva.iot.api_interface.ForgotPasswordDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ForgotPassPostParameters;
import com.techuva.iot.model.ForgotPasswordMainObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ForgetPasswordActivity extends AppCompatActivity {

    LinearLayout ll_root_view_forgot, ll_layout_entry, ll_button_submit, ll_signin;
    ImageView iv_userNameforgot;
    EditText edt_username_forgot;
    String EmailId ="";
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        InitViews();

        ll_signin.setOnClickListener(v -> {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        });

        ll_button_submit.setOnClickListener(v -> {
            getInputData();
        });

    }

    private void getInputData() {
        EmailId = edt_username_forgot.getText().toString();
        if(EmailId.length() > 0)
        {
            serviceCall();
        } else
        {
           // Toast.makeText(context, R.string.enter_email, Toast.LENGTH_SHORT ).show();
            Toast toast = Toast.makeText(context, R.string.enter_reg_email, Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
            toast.show();
        }
    }


    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.USER_MGMT_URL)
                //.baseUrl("http://182.18.177.27/TUUserManagement/api/user/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForgotPasswordDataInterface service = retrofit.create(ForgotPasswordDataInterface .class);

        Call<ForgotPasswordMainObject> call = service.getStringScalar(new ForgotPassPostParameters(EmailId));
        call.enqueue(new Callback<ForgotPasswordMainObject>() {
            @Override
            public void onResponse(Call<ForgotPasswordMainObject> call, Response<ForgotPasswordMainObject> response) {
                if(response.body()!=null){
                   if(response.body().getInfo().getErrorCode()==0)
                    {
                        showCustomDialog(response.body().getInfo().getErrorMessage());
                    }
                    else if(response.body().getInfo().getErrorCode().equals(1))
                    {
                        Toast toast = Toast.makeText(context, response.body().getInfo().getErrorMessage(), Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();
                        // Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<ForgotPasswordMainObject> call, Throwable t) {
                Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });

    }

    private void InitViews() {
        context = ForgetPasswordActivity.this;
        ll_root_view_forgot = findViewById(R.id.ll_root_view_forgot);
        ll_layout_entry = findViewById(R.id.ll_layout_entry);
        ll_button_submit = findViewById(R.id.ll_button_submit);
        ll_signin = findViewById(R.id.ll_signin);
        iv_userNameforgot = findViewById(R.id.iv_userNameforgot);
        edt_username_forgot = findViewById(R.id.edt_username_forgot);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }



    private void showCustomDialog(String text) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        TextView tv_alertText = dialogView.findViewById(R.id.tv_alertText);
        TextView button_ok = dialogView.findViewById(R.id.button_ok);
        tv_alertText.setText(text);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent
                );
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
