package com.techuva.iot.utils.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.techuva.iot.app.Constants;

public class MPreferences
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    public static String KEY = 	"key";


    public static String AppVersion = 	"1";
    public static String FontVersion = 	"0";

    @SuppressLint("CommitPrefEdits")
    public MPreferences(Context context)
    {
        preferences		=	PreferenceManager.getDefaultSharedPreferences(context);
        edit			=	preferences.edit();
    }

    public  void saveString(String strKey,String strValue)
    {
        edit.putString(strKey, strValue);
        edit.commit();
    }


    public void saveInt(String strKey,int value)
    {
        edit.putInt(strKey, value);
        edit.commit();
    }

    public String getStringFromPreference(String strKey,String defaultValue )
    {
        return preferences.getString(strKey, defaultValue);
    }

    public String getAppVersion(String strKey,String defaultValue )
    {
        return preferences.getString(strKey, defaultValue);
    }

    public String getFontVersion(String strKey,String defaultValue )
    {
        return preferences.getString(strKey, defaultValue);
    }

    public int getIntFromPreference(String strKey,int defaultValue)
    {
        return preferences.getInt(strKey, defaultValue);
    }

    public void setLoginStatus(Boolean loggedIn)
    {
        edit.putBoolean(Constants.IsLoggedIn, true);
        edit.commit();
    }
    public void setDefaultDeviceSelected(Boolean deviceSelected)
    {
        edit.putBoolean(Constants.IsDefaultDeviceSaved, true);
        edit.commit();
    }


    public void createLoginSession( String userId, String userName, String emailId, int deviceID, String deviceInUse)
    {

        edit.putString(Constants.UserID, userId);

        edit.putString(Constants.UserName, userName);

        edit.putString(Constants.UserMailId, emailId);

        edit.putBoolean(Constants.IsLoggedIn, true);

        edit.putInt(Constants.DeviceID, deviceID);

        edit.putString(Constants.DEVICE_IN_USE, deviceInUse);

        edit.commit();
    }


    public void deviceSelectionUpdate(String deviceInUse)
    {
/*
		edit.putString(AppConstants.KEY_CUSTOMER_ID,custId);

		edit.putString(AppConstants.KEY_USER_NAME,custName);

		edit.putString(AppConstants.KEY_USER_EMAIL, emailId);

		edit.putString(AppConstants.KEY_USER_CONTACT, MobileNo);

		edit.putBoolean(AppConstants.KEY_IS_LOGIN, true);*/

        edit.putString(Constants.DeviceID, deviceInUse);

        edit.commit();
    }

 /*   public String getupdatedwalletamount()
    {
        return preferences.getString(AppConstants.KEY_WALLET_AMOUNT,"0");
    }

    public String getCustId()
    {
        return preferences.getString(AppConstants.KEY_CUSTOMER_ID, "NA");
    }
*/
    public void logoutUser()
    {
		/*edit.clear();
		edit.commit();*/
        edit.putBoolean(Constants.IsLoggedIn, false);
        edit.putString(Constants.IsDefaultDeviceSaved, "null");
        edit.commit();

    }
/*

    public void saveLoginCookie(String cookie)
    {

        edit.putString(AppConstants.LOGINCOOKIE, cookie);
        edit.commit();

    }

    public String getLoginCookie()
    {
        return preferences.getString(AppConstants.LOGINCOOKIE,"null");
    }
*/

    /**
     * Get stored session data
     * */
  /*  public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user id
        user.put(AppConstants.KEY_CUSTOMER_ID, preferences.getString(AppConstants.KEY_CUSTOMER_ID, "0"));

        // user name
        user.put(AppConstants.KEY_USER_NAME, preferences.getString(AppConstants.KEY_USER_NAME, "NA"));

        // user email id
        user.put(AppConstants.KEY_USER_EMAIL, preferences.getString(AppConstants.KEY_USER_EMAIL, "NA"));

        // user contact number
        user.put(AppConstants.KEY_USER_CONTACT, preferences.getString(AppConstants.KEY_USER_CONTACT, "0000000000"));

        //Wallet Amt
        user.put(AppConstants.KEY_WALLET_AMOUNT, preferences.getString(AppConstants.KEY_WALLET_AMOUNT, "0"));

        // return user
        return user;
    }*/

    // Get Login State
    public boolean isLoggedIn(){
        return preferences.getBoolean(Constants.IsLoggedIn, false);
    }

    /**
     * Logout User method
     */

    public boolean isDefaultDeviceSelected()
    {
        return preferences.getBoolean(Constants.IsDefaultDeviceSaved, false);
    }

    public static String getAppVersion() {
        return AppVersion;
    }

    public static void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }


    public static String getFontVersion() {
        return FontVersion;
    }

    public static void setFontVersion(String fontVersion) {
        FontVersion = fontVersion;
    }

    public static void setPreferences(Context context, String key, String value) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    /**
     * This method is used to get shared object
     * @param context Application context
     * @param key shared object key
     * @return return value, for default "" asign.
     */
    public static String getPreferences(Context context, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        String json = appSharedPrefs.getString(key, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return json;
    }


}
