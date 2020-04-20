package com.techuva.iot.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techuva.iot.R;
import com.techuva.iot.adapter.NavDrawerListAdapter;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.NavDrawerItem;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class BaseActivity extends AppCompatActivity {

    public TextView txt_usrname, txt_deviceInUse;

    public LinearLayout llContent, ll_userDetail;

    public LayoutInflater inflater;

    public Context basecontext;

    public Toolbar mtoolbar;

    public String[] nav_item_titlenames;

    public Bundle savedInstanceState;

    public ListView mdrawerlistview;

    public ImageView iv_filter;

    public DrawerLayout mdrawerlayout;

    public ActionBarDrawerToggle mdrawertoogle;

    public View header_view,footer_view;

    private AnimationDrawable animationDrawable;

    public ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter adapter;

    public View customtoast;

    public AlertDialog alertDialog;

    public AlertDialog.Builder alertBuilder;

    public Dialog dialog;

    // PreferenceUtils preferenceUtils;

    public HashMap<String,String> Userdata;

    String UserName, DeviceInUse, UserMobile, WalletAmt, productName ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        basecontext = BaseActivity.this;
        productName = MApplication.getString(basecontext, Constants.InventoryTypeName);

        //preferenceUtils = new PreferenceUtils(basecontext);
        this.savedInstanceState  =  savedInstanceState;

        inflater = this.getLayoutInflater();

        navDrawerItems = new ArrayList<>();

        baseInitializeControls();

        generateiconandstringfordrawer();

        initialize();

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        //updatesidepanelwalletamt();
       /* if(mtoolbar!=null)
        {
            setSupportActionBar(mtoolbar);
        }
*/
        try {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        getSupportActionBar().hide();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initdrawer();

        mdrawertoogle.syncState();

        mdrawertoogle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


        mdrawerlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawerItem drawerobj = (NavDrawerItem) parent.getItemAtPosition(position);
                view.setSelected(true);
               /* if(MApplication.getBoolean(basecontext, Constants.IsHomeSelected))
                {
                    TextView tv = view.findViewById(R.id.drawertitle);
                    tv.setTextColor(getResources().getColor(R.color.colorPrimary));
             //       MApplication.setBoolean(basecontext, Constants.IsHomeSelected, false);
                }*/

               //Toast.makeText(basecontext, "pos "+ position, Toast.LENGTH_SHORT).show();
                slidelist_item_click(drawerobj.title);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    public  void slidelist_item_click(String title)
    {
        switch (title)
        {
            case "Home":
                goto_home();
                mdrawerlayout.closeDrawers();
                break;
            case "Devices":
                goto_devices();
                mdrawerlayout.closeDrawers();
                break;
            case "Device History":
                goto_devicesHisory();
                mdrawerlayout.closeDrawers();
                break;
            case "Manage Threshold":
                goto_manageThreshold();
                mdrawerlayout.closeDrawers();
                break;
           /* case "Forewarning Details":
                goto_forewarningDetails();
                mdrawerlayout.closeDrawers();
                break;*/
           /* case "About App":
                goto_aboutApp();
                mdrawerlayout.closeDrawers();
                break;
            case "Terms and Conditions":
                goto_TermsandCondition();
                mdrawerlayout.closeDrawers();
                break;*/
            case "Logout":
                goto_logout_method();
                adapter.notifyDataSetChanged();
                mdrawerlayout.closeDrawers();

        }
    }

    protected abstract void goto_manageThreshold();

    public void showAlertDialog(String strMessage, String firstBtnName)
    {
        runOnUiThread(new RunshowCustomDialogs(strMessage, firstBtnName));
    }

    class RunshowCustomDialogs implements Runnable
    {
        private String strMessage;// Message to be shown in dialog
        private String firstBtnName;
        private int titleGravity;
        private boolean isShowNestedDialog;
        private String dialogFrom;

        public RunshowCustomDialogs(String strMessage, String firstBtnName)
        {
            this.strMessage 	= strMessage;
            this.firstBtnName 	= firstBtnName;
        }

        @Override
        public void run()
        {
            closeAlertDialog();
            alertBuilder = new AlertDialog.Builder(basecontext);
            alertBuilder.setCancelable(true);

            final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.notification_dailog, null);

            TextView dialogtvTitle = linearLayout.findViewById(R.id.tvTitle);
            TextView btnYes = linearLayout.findViewById(R.id.btnYes);


            if(titleGravity!=0)
            {
                // Only in the case of Crash Report Dialog, i am customizing it with custom padding.
                dialogtvTitle.setGravity(titleGravity);
                dialogtvTitle.setPadding(35, 35, 0, 35);

            }
            dialogtvTitle.setText(strMessage);
            btnYes.setText(firstBtnName);
            btnYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    alertDialog.cancel();

                }
            });

            try
            {
                alertDialog = alertBuilder.create();
                alertDialog.setView(linearLayout,0,0,0,0);
                alertDialog.setInverseBackgroundForced(true);
                alertDialog.show();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void closeAlertDialog()
    {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public abstract void initialize();

    public abstract void goto_home();
    public abstract void goto_devices();
    public abstract void goto_devicesHisory();
    public abstract void goto_forewarningDetails();
    public abstract void goto_aboutApp();
    public abstract void goto_TermsandCondition();
    public abstract void goto_logout_method();



    private void initdrawer() {

        mdrawertoogle=new ActionBarDrawerToggle(this,mdrawerlayout, mtoolbar,R.string.opendrawer,R.string.closedrawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }


        };

        mdrawerlayout.setDrawerListener(mdrawertoogle);
    }

    private void baseInitializeControls() {
        Typeface faceLight = Typeface.createFromAsset(basecontext.getAssets(),
                "fonts/AvenirLTStd-Light.otf");


        llContent= findViewById(R.id.llContent);

        header_view = getLayoutInflater().inflate(R.layout.header_navi, null, false);

        footer_view = getLayoutInflater().inflate(R.layout.footer_navi,null, false);

        mdrawerlistview =  findViewById(R.id.left_drawer);

        mtoolbar=  findViewById(R.id.toolbar_drawer);

        mdrawerlayout=  findViewById(R.id.drawerLayout);

        iv_filter =  findViewById(R.id.iv_filter);

        adapter = new NavDrawerListAdapter(basecontext, navDrawerItems);

        ll_userDetail = header_view.findViewById(R.id.ll_userDetail);

        txt_usrname=  header_view.findViewById(R.id.tv_userNameNav);

        txt_deviceInUse = header_view.findViewById(R.id.tv_deviceInUse);

        txt_deviceInUse.setTypeface(faceLight);

        txt_usrname.setTypeface(faceLight);

        mdrawerlistview.addHeaderView(header_view, null, false);

        mdrawerlistview.addFooterView(footer_view,null,false);

        mdrawerlistview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mdrawerlistview.setAdapter(adapter);
    }

    private void generateiconandstringfordrawer() {

        //TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        if(MApplication.getBoolean(basecontext, Constants.IsLoggedIn))
        {
            nav_item_titlenames = getResources().getStringArray(R.array.navdraweritemsafterlogin);
            UserName = MApplication.getString(basecontext, Constants.UserName);
            DeviceInUse = MApplication.getString(basecontext, Constants.DEVICE_IN_USE);
            txt_usrname.setText(UserName);
            txt_deviceInUse.setText(DeviceInUse);
        }
        else
        {
            nav_item_titlenames = getResources().getStringArray(R.array.navdraweritemsbeforelogin);
            ll_userDetail.setVisibility(View.GONE);
        }
        //nav_item_titlenames = getResources().getStringArray(R.array.navdraweritemsbeforelogin);
        // adding nav drawer items to list
        // Home
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[0], R.drawable.home));
        // Devices
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[1], R.drawable.devices));
        // Device History
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[2], R.drawable.device_history));
        // Device History
        if(productName.equals("HBL")){
            navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[3], R.drawable.device_history));
        }
        // About App
        /* navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[3], R.drawable.about_app));
        // Terms & Conditions
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[4], R.drawable.terms));
        // Privacy Policy
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[5], R.drawable.privacy_policy));
        // IOT Products
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[6], R.drawable.iot_devices));
        // Report Issue
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[7], R.drawable.report_issue));*/
        // Logout
        navDrawerItems.add(new NavDrawerItem(nav_item_titlenames[4], R.drawable.logout));
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        mdrawertoogle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mdrawertoogle.syncState();
    }

    public void showLoaderNew() {
        runOnUiThread(new Runloader(getResources().getString(R.string.loading)));
    }

    class Runloader implements Runnable {
        private String strrMsg;

        public Runloader(String strMsg) {
            this.strrMsg = strMsg;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                if (dialog == null)
                {
                    dialog = new Dialog(basecontext,R.style.Theme_AppCompat_Light_DarkActionBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                dialog.show();

                ImageView imgeView =  dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading =  dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);

                imgeView.setBackgroundResource(R.drawable.frame);

                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(() -> {
                    if (animationDrawable != null)
                        animationDrawable.start();
                });
            } catch (Exception e)
            {

            }
        }
    }

    public void hideloader() {
        runOnUiThread(() -> {
            try
            {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }); }
}
