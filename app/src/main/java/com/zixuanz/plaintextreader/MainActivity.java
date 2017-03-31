package com.zixuanz.plaintextreader;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zixuanz.plaintextreader.adapters.FragPagerAdapter;
import com.zixuanz.plaintextreader.fragments.CollectionFragment;
import com.zixuanz.plaintextreader.fragments.SettingFragment;
import com.zixuanz.plaintextreader.utils.PermissionRequest;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener, SettingFragment.UpdateFragmentListener{

    public static final int PAGE_FILE = 0;
    public static final int PAGE_HISTORY = 1;
    public static final int PAGE_FAVORITE = 2;
    public static final int PAGE_SETTING = 3;

    private FragPagerAdapter fragPagerAdapter;

    private ViewPager viewPager;
    private TextView titleBar;

    private RadioGroup menuGroup;
    private RadioButton menuFile, menuHistory, menuFavorite, menuSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MainActivity:::", "Start");
        setContentView(R.layout.activity_main);
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionRequest.requestPermission(MainActivity.this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_file:
                viewPager.setCurrentItem(PAGE_FILE);
                break;
            case R.id.rb_history:
                viewPager.setCurrentItem(PAGE_HISTORY);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case R.id.rb_favorite:
                viewPager.setCurrentItem(PAGE_FAVORITE);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case R.id.rb_setting:
                viewPager.setCurrentItem(PAGE_SETTING);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.requestPermissionResult(MainActivity.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            switch (viewPager.getCurrentItem()) {
                case PAGE_FILE:
                    titleBar.setText(getResources().getString(R.string.menu_file));
                    menuFile.setChecked(true);
                    break;
                case PAGE_HISTORY:
                    titleBar.setText(getResources().getString(R.string.menu_history));
                    menuHistory.setChecked(true);
                    break;
                case PAGE_FAVORITE:
                    titleBar.setText(getResources().getString(R.string.menu_favorite));
                    menuFavorite.setChecked(true);
                    break;
                case PAGE_SETTING:
                    titleBar.setText(getResources().getString(R.string.menu_setting));
                    menuSetting.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void clearHistory() {
        ((CollectionFragment)fragPagerAdapter.getItem(PAGE_HISTORY)).clearCollection();
    }

    @Override
    public void clearFavorite() {
        ((CollectionFragment)fragPagerAdapter.getItem(PAGE_FAVORITE)).clearCollection();
    }

    @Override
    public void allowHistory(boolean flag) {
        ((CollectionFragment)fragPagerAdapter.getItem(PAGE_HISTORY)).disableCollection(flag);
    }

    private void initComponent(){
        titleBar = (TextView) findViewById(R.id.tv_title);

        menuGroup = (RadioGroup) findViewById(R.id.rg_main);
        menuFile = (RadioButton) findViewById(R.id.rb_file);
        menuHistory = (RadioButton) findViewById(R.id.rb_history);
        menuFavorite = (RadioButton) findViewById(R.id.rb_favorite);
        menuSetting = (RadioButton) findViewById(R.id.rb_setting);
        menuGroup.setOnCheckedChangeListener(this);

        fragPagerAdapter = new FragPagerAdapter(getFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragPagerAdapter);
        viewPager.setCurrentItem(PAGE_FILE);
        viewPager.addOnPageChangeListener(this);

        menuFile.setChecked(true);
    }
}
