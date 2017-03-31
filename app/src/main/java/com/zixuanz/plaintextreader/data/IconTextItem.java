package com.zixuanz.plaintextreader.data;

/**
 * Created by Zixuan Zhao on 9/14/16.
 */
public class IconTextItem {

    private String name;
    private int icon;

    public IconTextItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
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
}
