package com.zixuanz.plaintextreader.data;


import java.util.Date;

/**
 * Created by Zixuan Zhao on 11/26/16.
 */

public class IconTextDateItem {

    private String name;
    private int icon;
    private Date date;
    private String path;

    public IconTextDateItem(String name, int icon, Date date, String path){
        this.setName(name);
        this.setIcon(icon);
        this.setDate(date);
        this.setPath(path);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
