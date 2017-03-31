package com.zixuanz.plaintextreader.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zixuanz.plaintextreader.R;


/**
 * Created by Zixuan Zhao on 9/23/16.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    private UpdateFragmentListener callback;
    private CheckBoxPreference disHistory;
    private EditTextPreference editFontSize;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        initInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (UpdateFragmentListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSettings();

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        String key = preference.getKey();

        switch (key){

            case "setting_clear_history": {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.Warning)
                        .setMessage(R.string.pref_clear_history_message)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("history", Context.MODE_PRIVATE);
                                if(sp.edit().clear().commit()){
                                    Toast.makeText(getActivity(), R.string.pref_clear_history_succ, Toast.LENGTH_SHORT).show();
                                    callback.clearHistory();
                                }else{
                                    Toast.makeText(getActivity(), R.string.pref_clear_history_fail, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), R.string.pref_user_cancel, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();

                break;
            }

            case "setting_clear_favorite":{
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.Warning)
                        .setMessage(R.string.pref_clear_favorite_message)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
                                if(sp.edit().clear().commit()){
                                    Toast.makeText(getActivity(), R.string.pref_clear_favorite_succ, Toast.LENGTH_SHORT).show();
                                    callback.clearFavorite();
                                }else{
                                    Toast.makeText(getActivity(), R.string.pref_clear_favorite_fail, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), R.string.pref_user_cancel, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();

                break;
            }

            case "setting_clear_cache":{
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.Warning)
                        .setMessage(R.string.pref_clear_cache_message)
                        .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("record", Context.MODE_PRIVATE);
                                if(sp.edit().clear().commit()){
                                    Toast.makeText(getActivity(), R.string.pref_clear_cache_succ, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), R.string.pref_clear_cache_fail, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), R.string.pref_user_cancel, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();

                break;
            }


        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        switch (key){
            case "setting_disable_history":{
                callback.allowHistory(Boolean.parseBoolean(newValue.toString()));
                break;
            }
            case "setting_font_size":{
                if(Integer.parseInt(newValue.toString()) < getResources().getDimension(R.dimen.def_read_font_size)){
                    return false;
                    //newValue = (int) getResources().getDimension(R.dimen.def_read_font_size);
                }else if(Integer.parseInt(newValue.toString()) > getResources().getDimension(R.dimen.max_read_font_size)){
                    return false;
                    //newValue = (int) getResources().getDimension(R.dimen.max_read_font_size);
                }
                //getPreferenceManager().getSharedPreferences().edit().putString("setting_font_size", String.valueOf(newValue)).commit();
                preference.setSummary(String.valueOf(newValue) + "px");
                break;
            }

        }
        return true;
    }

    private void initInstance(){
        disHistory = (CheckBoxPreference) findPreference("setting_disable_history");
        editFontSize = (EditTextPreference) findPreference("setting_font_size");
        editFontSize.setOnPreferenceChangeListener(this);
    }


    private void loadSettings(){
        if(disHistory.isChecked()){
            disHistory.setSummary(getResources().getString(R.string.setting_dishistory_sumoff));
        }else{
            disHistory.setSummary(getResources().getString(R.string.setting_dishistory_sumon));
        }

        editFontSize.setSummary(editFontSize.getText() + "px");

    }

    public interface UpdateFragmentListener {
        void clearHistory();
        void clearFavorite();

        void allowHistory(boolean flag);
    }

}
