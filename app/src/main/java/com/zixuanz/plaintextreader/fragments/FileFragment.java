package com.zixuanz.plaintextreader.fragments;


import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;

import com.zixuanz.plaintextreader.R;
import com.zixuanz.plaintextreader.adapters.IconTextAdapter;
import com.zixuanz.plaintextreader.data.IconTextItem;
import com.zixuanz.plaintextreader.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Zixuan Zhao on 9/15/16.
 */
public class FileFragment extends ListFragment {

    private final static int icon[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.tab_better_normal, R.mipmap.tab_channel_normal};
    private static File sdCard;

    private FileItem curr;

    private List<IconTextItem> items;
    private IconTextAdapter fileAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParameters();

        fileAdapter = new IconTextAdapter(items, getActivity());
        setListAdapter(fileAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(position == 0 && curr.prev != null) {
            curr.fileInfo = curr.prev.fileInfo;
            curr.prev = curr.prev.prev;
            updateItems();
        }else{
            String name = ((IconTextItem)fileAdapter.getItem(position)).getName();
            clickFile(name);
        }

        fileAdapter.notifyDataSetChanged();
    }


    //initialize all parameters or data needed for display list at beginning
    private void initParameters(){

        curr = new FileItem();
        curr.prev = null;
        curr.parent = null;
        curr.fileInfo = new HashMap<>();

        if(isSDcardOn()){
            sdCard = Environment.getExternalStorageDirectory();
            curr.fileInfo.put("SDCard", sdCard.getAbsolutePath());
        }
        curr.fileInfo.put("Device", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

        items = new ArrayList<>();
        updateItems();

    }

    //update all parameters after fileinfo is updated.
    //If succeed to update, return true. Otherwise, return false
    private boolean updateItems(){

        //make sure items is initialized and empty
        if(!items.isEmpty())
            items.clear();

        if(curr.prev == null){
            int count = 1;
            for(String name : curr.fileInfo.keySet()){
                items.add(new IconTextItem(name, icon[count++]));
            }
        }else{
            items.add(new IconTextItem("...", icon[0]));

            if(curr.fileInfo.isEmpty()){
                return true;
            }

            Iterator iter = curr.fileInfo.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry) iter.next();
                String name = (String)entry.getKey();
                String path = (String)entry.getValue();

                if(new File(path).isDirectory()){
                    items.add(new IconTextItem(name, icon[3]));
                }else{
                    items.add(new IconTextItem(name, icon[4]));
                }
            }
        }

        return true;
    }

    //update fileinfo by parent file's childlist
    private void childFileLists(final String parentPath){
        childFileLists(new File(parentPath));
    }

    private void childFileLists(File parentFile){
        FileItem temp = new FileItem();
        temp.prev = curr.prev;
        temp.parent = curr.parent;
        temp.fileInfo = curr.fileInfo;

        curr.parent = parentFile;
        File childList[] = curr.parent.listFiles();        //add filter later

        curr.fileInfo = new HashMap<>();
        if(childList != null && childList.length != 0){
            for(File file : childList){
                curr.fileInfo.put(file.getName(), file.getAbsolutePath());
            }
        }

        curr.prev = temp;
        updateItems();

    }

    //if a file is not existed, refresh current file list
    private void refreshFileList(){
        if(curr.parent == null){
            initParameters();
        }else{
            childFileLists(curr.parent.getPath());
        }
    }

    //whether there sdcard is on or not
    private boolean isSDcardOn(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //the response for click an item in file list
    private void clickFile(String name){
        String path = curr.fileInfo.get(name);
        File file = new File(path);

        if(!file.exists()){
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.filefrag_file_not_existed)
                    .setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refreshFileList();
                            fileAdapter.notifyDataSetChanged();
                        }
                    }).create().show();
            return;
        }

        if(file.isDirectory()){
            childFileLists(file);
        }else{
            FileUtils.openFile(file, getActivity());
        }
    }

    private static class FileItem{
        FileItem prev;
        File parent;
        HashMap<String, String> fileInfo;
    }




}
