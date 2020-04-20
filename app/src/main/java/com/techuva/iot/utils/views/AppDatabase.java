package com.techuva.iot.utils.views;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.techuva.iot.model.ChannelUnitMeasureDO;

import java.util.ArrayList;

public class AppDatabase {

    public static final boolean DEBUG = true;

    /******************** Logcat TAG ************/
    public static final String LOG_TAG = "CARTDATABASE";

    /***************Address Table Fields******************/
    public static final String Column_custId= "custId";

    /***************MeasurementUnit Table Fields******************/
    public static final String Channel_Num= "channelNum";
    public static final String Channel_Unit= "unit";

    /*********************Installation Check*******************/
    public static final String Column_id = "id";
    public static final String Column_state = "state";

    /******************** Database Name ************/
    public static final String DATABASE_NAME = "IOT";

    /******************** Database Version (Increase one if want to also upgrade your database) ************/
    public static final int DATABASE_VERSION = 1;// started at 1

    /** Table names */
    public static final String Table_Cartlist = "cartlist";
    public static final String Table_UserAddress ="userAddress";
    public static final String Table_InstallFlag ="install_Flag_Table";
    public static final String Table_ProductCart ="product_cart";
    public static final String Table_MeasurementUnit ="measurement_unit_table";


    /******************** Set all table with comma seperated like USER_TABLE,ABC_TABLE ************/
    private static final String[] ALL_TABLES = { Table_UserAddress, Table_InstallFlag, Table_ProductCart, Table_MeasurementUnit};


    /** Create table syntax */
    private static final String USER_Address = "create table userAddress(custId INTEGER, addressId TEXT, houseNo TEXT, address TEXT, landmark TEXT, city TEXT, zipcode TEXT, addressType TEXT);";

    private static final String APP_INSTALL = "create table install_Flag_Table(id INTEGER, state INTEGER);";

    private static final String USER_PRODUCT_CART = "create table product_cart(dealerId INTEGER, retailerId INTEGER, productId INTEGER, quantity INTEGER, statusId INTEGER,receivedQuantity INTEGER, dealerNames String, productName String, productDesc String, imageUrl String, dealersName String, orderId INTEGER PRIMARY KEY AUTOINCREMENT);";

    private static final String Measurement_Unit_Table = "create table measurement_unit_table(channelNum INTEGER, unit TEXT);";
    /******************** Used to open database in syncronized way ************/
    private static DataBaseHelper DBHelper = null;

    protected AppDatabase() {
    }
    /******************* Initialize database *************/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }
    /********************** Main Database creation INNER class ********************/
    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {
              //  db.execSQL(USER_CREATECARTLIST);
                db.execSQL(USER_Address);
                db.execSQL(APP_INSTALL);
                db.execSQL(USER_PRODUCT_CART);
                db.execSQL(Measurement_Unit_Table);

            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed


    /********************** Open database for insert,update,delete in syncronized manner ********************/
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }

    /*********************** Escape string for single quotes (Insert,Update)************/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace("'", "''");
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }
    /*********************** UnEscape string for single quotes (show data)************/
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("''", "'");
        }

        return aReturn;
    }


    /**
     * All Operations for Cart(Create, Read, Update, Delete)
     */
    public static void addUnittoTable(ChannelUnitMeasureDO object) {
        final SQLiteDatabase db = open();
        int channelNum = object.getChannelNum();
        String channelUnit = object.getUnit();
//========================================================================================================================
        ContentValues cv = new ContentValues();

        cv.put(Channel_Num, channelNum);
        cv.put(Channel_Unit, channelUnit);
        db.insert(Table_MeasurementUnit, null, cv);

        db.close(); // Closing database connection
    }

//************************* Install Flag **************************************

    //*****************************************************************************
    public static void SaveInstallState(int id,int state)
    {
        final SQLiteDatabase db = open();

        ContentValues value = new ContentValues();
        value.put(Column_id, id);
        value.put(Column_state, state);
        db.insert(Table_InstallFlag, null, value);
        db.close(); // Closing database connection

    }

    public static int getInstallstatecount()
    {
        SQLiteDatabase db = open();
        Cursor cursor;
        cursor=db.rawQuery("SELECT count(*) FROM "+Table_InstallFlag,null );
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        cursor.close();
        return icount;
    }

    public static int getInstallstate(int id)
    {
        int notificationstate=-1;

        final SQLiteDatabase db = open();
        Cursor cursor = db.query(Table_InstallFlag, new String[]{Column_id, Column_state}, Column_id + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                notificationstate=cursor.getInt(1);
            } while (cursor.moveToNext());
        }

        return notificationstate;
    }

    public static int updateInstallourstate(int id,int state)
    {
        final SQLiteDatabase db = open();

        ContentValues values = new ContentValues();
        values.put(Column_state, state);

        // updating row
        return db.update(Table_InstallFlag, values, Column_id + " = ?",new String[] { String.valueOf(id) });
    }

    //***********************************************************************************


    // Getting Unit Cartlist
    public static ArrayList<ChannelUnitMeasureDO> getUnitbyChannelNum(int channelNum) {
        ArrayList<ChannelUnitMeasureDO> unitList = new ArrayList<>();
        final SQLiteDatabase db = open();
        String query="select * from measurement_unit_table where channelNum="+"'"+channelNum+"'";
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChannelUnitMeasureDO data = new ChannelUnitMeasureDO();
                data.channelNum =cursor.getInt(0);
                data.unit = cursor.getString(1);
                unitList.add(data);
            } while (cursor.moveToNext());
        }
        return unitList;
    }


    public static void clearUnitTable()
    {
        SQLiteDatabase db = open();
        db.execSQL("delete from " + Table_MeasurementUnit);
    }


    // Getting cartlist Count
    public static int getUnitlistCount()
    {
        SQLiteDatabase db = open();
        Cursor cursor;
        cursor=db.rawQuery("SELECT count(*) FROM "+ Table_MeasurementUnit,null );
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        cursor.close();
        return icount;
    }

}



