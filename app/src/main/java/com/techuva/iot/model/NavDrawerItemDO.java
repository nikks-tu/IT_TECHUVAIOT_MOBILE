package com.techuva.iot.model;

/**
 * Created by Goutam on 8/26/2015.
 */
public class NavDrawerItemDO
{

    public String title;
    public int icon;
    public String count;
    // boolean to set visiblity of the counter
    public boolean isCounterVisible = false;

    public NavDrawerItemDO(){}

    public NavDrawerItemDO(String title){

        this.title = title;
    }

    public NavDrawerItemDO(String title, boolean isCounterVisible, String count){
        this.title = title;
       // this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

}
