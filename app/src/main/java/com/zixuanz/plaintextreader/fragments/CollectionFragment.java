package com.zixuanz.plaintextreader.fragments;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zixuanz.plaintextreader.R;
import com.zixuanz.plaintextreader.adapters.IconTextDateAdapter;
import com.zixuanz.plaintextreader.data.IconTextDateItem;
import com.zixuanz.plaintextreader.utils.FileUtils;
import com.zixuanz.plaintextreader.utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Zixuan Zhao on 9/22/16.
 */
public class CollectionFragment extends ListFragment implements SharedPreferences.OnSharedPreferenceChangeListener, AdapterView.OnItemLongClickListener{

    private String title, sizeTitle;

    private IconTextDateAdapter collectAdapter;

    Map<String, String> data;
    private ArrayList<IconTextDateItem> collectData;
    //private int collectSize;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getArguments().getString("title");
        sizeTitle = getArguments().getString("sizeTitle");

        initData();

        collectAdapter = new IconTextDateAdapter(collectData, getActivity());
        setListAdapter(collectAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().setOnItemLongClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        final String path = collectData.get(position).getPath();
        Log.d("!!!!!!file name", path);
        File file = new File(path);

        if(file.exists()){
            FileUtils.openFile(file, getActivity());
        }else{
            if(editor.remove(path).commit()){
                Toast.makeText(getActivity(), R.string.collect_file_removed_yes, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), R.string.collect_file_removed_no, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


        final String path = collectData.get(position).getPath();

        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.collect_list_remove)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(editor.remove(path).commit()){
                            Toast.makeText(getActivity(), R.string.collect_list_removed_yes, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), R.string.collect_list_removed_no, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.No, null)
                .create().show();

        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String path) {

        //new in sp
        //update
        //remove

        Log.d("CollectionFragment:::", path);
        String value = sp.getString(path, "NULL");
        if(!data.containsKey(path) && !value.equals("NULL")){
            data.put(path, value);
            collectData.add(0, new IconTextDateItem(new File(path).getName(), Tools.setIcon(path), Tools.transStrToDate(value), path));
        }else if(data.containsKey(path) && !value.equals("NULL")){
            for(IconTextDateItem item : collectData){
                if(item.getPath().equals(path)){
                    collectData.remove(item);
                    break;
                }
            }
            collectData.add(0, new IconTextDateItem(new File(path).getName(), Tools.setIcon(path), Tools.transStrToDate(value), path));
        }else if(data.containsKey(path) && value.equals("NULL")){
            for(IconTextDateItem item : collectData){
                if(item.getPath().equals(path)){
                    collectData.remove(item);
                    data.remove(path);
                    break;
                }
            }
        }

        collectAdapter.notifyDataSetChanged();

    }

    public static CollectionFragment newInstance(String title, String sizeTitle){
        CollectionFragment fragment = new CollectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("sizeTitle", sizeTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initData(){

        //initialize all required variables
        sp = getActivity().getApplicationContext().getSharedPreferences(title, Context.MODE_PRIVATE);
        editor = sp.edit();
        if(title.equals("history")){
            disableCollection(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("setting_disable_history", false));
        }else{
            sp.registerOnSharedPreferenceChangeListener(this);
        }
        /*
        collectSize = Integer.parseInt(sizeTitle);
        if(collectSize == -1){
            Toast.makeText(getActivity(), "Failed to read list. Please try again.", Toast.LENGTH_SHORT);
            Log.d("!!!!!!Collectsize", String.valueOf(collectSize));
            collectSize = 10;
        }
        */
        data = new HashMap<>();
        initCollection();
    }


    private void initCollection(){

        Map<String, ?> temp = sp.getAll();

        int icon = 0;
        Date parseDate = null;
        PriorityQueue<IconTextDateItem> items = new PriorityQueue<>(10, new Comparator<IconTextDateItem>() {
            @Override
            public int compare(IconTextDateItem o1, IconTextDateItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        for(Map.Entry<String, ?> entry : temp.entrySet()){
            String path = entry.getKey();
            String date = entry.getValue().toString();
            File file = new File(String.valueOf(entry.getKey()));

            if(file.exists()){
                icon = Tools.setIcon(path);
                parseDate = Tools.transStrToDate(date);
                items.add(new IconTextDateItem(file.getName(), icon, parseDate, path));
                data.put(path, date);
            }else{
                editor.remove(path);
            }
        }
        editor.commit();
        collectData = new ArrayList<>(items);

    }

    public void clearCollection(){
        collectData.clear();
        data.clear();
        collectAdapter.notifyDataSetChanged();
    }

    public void disableCollection(boolean flag){
        if(flag){
            sp.unregisterOnSharedPreferenceChangeListener(this);
        }else{
            sp.registerOnSharedPreferenceChangeListener(this);
        }

    }

}
