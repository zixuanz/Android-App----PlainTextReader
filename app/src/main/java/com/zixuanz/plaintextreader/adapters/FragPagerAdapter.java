package com.zixuanz.plaintextreader.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewGroup;

import com.zixuanz.plaintextreader.MainActivity;
import com.zixuanz.plaintextreader.fragments.CollectionFragment;
import com.zixuanz.plaintextreader.fragments.FileFragment;
import com.zixuanz.plaintextreader.fragments.SettingFragment;

import android.support.v13.app.FragmentPagerAdapter;


/**
 * Created by Zixuan Zhao on 9/21/16.
 */
public class FragPagerAdapter extends FragmentPagerAdapter {

    private final static int PAGER_COUNT = 4;

    private FileFragment fileFrag = null;
    private CollectionFragment historyFrag = null;
    private CollectionFragment favoriteFrag = null;
    private SettingFragment settingFrag = null;


    public FragPagerAdapter(FragmentManager fm) {
        super(fm);
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        fileFrag = new FileFragment();
        historyFrag = new CollectionFragment().newInstance("history", "10");
        favoriteFrag = new CollectionFragment().newInstance("favorite", "10");
        settingFrag = new SettingFragment();

    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_FILE:
                fragment = fileFrag;
                break;
            case MainActivity.PAGE_HISTORY:
                fragment = historyFrag;
                break;
            case MainActivity.PAGE_FAVORITE:
                fragment = favoriteFrag;
                break;
            case MainActivity.PAGE_SETTING:
                fragment = settingFrag;
                break;
        }
        return fragment;
    }

}
