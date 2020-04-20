package com.techuva.iot.app;

/**
 * Created by user on 8/29/2019.
 */
public interface Constants {
    //Test URL
    String USER_MGMT_URL = "http://182.18.177.27/TUUserManagement/api/user/";
   // String BASE_URL_TOKEN = "http://182.18.177.27:8586/";
    String BASE_URL_TOKEN = "http://54.214.254.175:8586/";
    //Production URL
    /*String BASE_URL_TOKEN = "https://service.datapollz.com";
    String USER_MGMT_URL = "https://winservices.datapollz.com//TUIoTAdminAPI/api/user/";*/
    String CurrentData = "currentdata";
    String ChannelDataProcom = "getProcomChannelData";
    String CurrentDataAgri = "currentdataAgri";
    String LoginData = "login";
    String ForgetPassword = "forgotpassword";
    String HistoryData = "inventoryHistoricalData";
    String HistoryDataAllChannels = "GetPivotRawSensorData";
    String HistoryDataAllChannelsForAgri = "GetPivotRawSensorDataAgri";
    String ListofDevices = "productinventorydashboard";
    String CompanyList = "companydashboard/";
    String VersionCheck = "VersionCheckInfo";
    String GetGraphData = "getconsumeddetailsGraphData";
    String ChangeDGStatus ="InsertGeneratorStatus";
    String GetConsumedData = "getconsumeddetails";
    String SaveDeviceToken = "saveMobileToken";
    String GetHBLNotifications = "threshold/getInventoryThresholdNotificationDetails";
    String GetHBLThresholdList = "threshold/getInventoryThresholdValuesDetails";
    String GetHBLThresholdNamesList = "NotificationThreshold/getDropdown";
    String SaveThresholdData = "threshold/savethresholdData";
    String CurrentDataForWaterMon = "currentDataForWaterMon";
    int SelectedChannel = 1;
    //String Table_Data_URL = "https://winservices.datapollz.com//TUIoTHomeAPI/api//IOT_API/";
    String Ref_Type = "MOBILE";
    String DeviceID = "DeviceID";
    String InventoryName = "InventoryName";
    String InventoryTypeName = "InventoryTypeName";
    String InventoryProvisionedOn = "InventoryProvisionedOn";
    String CompanyID ="CompanyID";
    String UserID ="UserID";
    String UserIDSelected ="UserIDSelected";
    String UserName ="UserName";
    String UserMailId ="UserMailId";
    String AppVersion = "1";
    String FontVersion = "0";
    String InventoryTypeId = "InventoryTypeId";
    String ForwarningDataCall = "getDeviceReportConvertData";
    String LeafWetnessCall = "getLeafWetnessInfo";
    String GetWaterMonValues ="currentDataByDate";
    String DataValueWaterMon = "IOT/WCM/getConsumptionInDay";
    String SingleAccount="SingleAccount";
    String IsLoggedIn = "IsLoggedIn";
    String IsHomeSelected = "IsHomeSelected";
    String IsDefaultDeviceSaved = "IsDefaultDeviceSaved";
    String Template = "Template";
    //Token 90 Day
    String AuthorizationKey = "Basic dGVjaHV2YS1jbGllbnQtbW9iaWxlOnNlY3JldA==";
    //Token 1 Hr
    //String AuthorizationKey = "Basic dGVjaHV2YS1jbGllbnQ6c2VjcmV0";
    String GrantType= "password";
    String GrantTypeRefresh= "refresh_token";
    String AccessToken ="AccessToken";
    String RefreshToken ="RefreshToken";
    String SecondsToExpireToken ="SecondsToExpireToken";
    String DateToExpireToken ="DateToExpireToken";
    String TokenExpireMsg = "Token Expired/Invalid";
    String NoUserRole = "No Role Assigned to User";
    String IsSessionExpired = "IsSessionExpired";
    String NULL_STRING = "null";
    String DEVICE_IN_USE = "";
    String COMPANY_ID = "COMPANY_ID";
    String ChannelNumKWH = "ChannelNumKWH";
    String NotificationCount = "NotificationCount";
    String SelectedChannelWaterLevel = "SelectedChannelWaterLevel";
    String OnOffWaterMotor = "WMM/insertMotorStatus";
    String MotorOnOffSummary = "WMM/getMotorOnOffSummary";
    String UpdateChannelDetails = "WMM/updateChannelInfo";
}
