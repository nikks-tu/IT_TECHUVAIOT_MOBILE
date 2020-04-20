package com.techuva.iot.model;

/**
 * Created by Nikita on 8/26/2015.
 */
public class NavDrawerItem extends BaseDomain
{
    public String title;
    public int icon;
    public String count;
    // boolean to set visibility of the counter
    public boolean isCounterVisible = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, boolean isCounterVisible, String count){
        this.title = title;
       // this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }

}
