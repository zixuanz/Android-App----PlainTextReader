package com.zixuanz.plaintextreader.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.zixuanz.plaintextreader.R;
import com.zixuanz.plaintextreader.ReadActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Zixuan Zhao on 9/26/16.
 */

public class FileUtils {

    public static void openFile(String path, Context context){
        openFile(new File(path), context);
    }

    public static void openFile(File file, Context context){
        if(file.toString().endsWith("txt")){
            String text;
            try {
                text = readFile(file, context);
                if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("setting_disable_history", false)){
                    addToHistory(file, context);
                }

                Intent intent = new Intent(context, ReadActivity.class);
                intent.putExtra("Text", text);
                intent.putExtra("Path", file.getAbsolutePath());
                intent.putExtra("name", file.getName());
                context.startActivity(intent);
            }catch (IOException e){
                new AlertDialog.Builder(context)
                        .setMessage(R.string.Info)
                        .setMessage(R.string.file_utils_failread)
                        .setPositiveButton(context.getResources().getString(R.string.OK), null).create().show();
            }
        }

    }

    public static String readFile(String path, Context context) throws IOException {
        return readFile(new File(path), context);
    }

    public static String readFile(File file, Context context) throws IOException {
        int len = 0;
        byte[] temp = new byte[1024];
        StringBuilder sb = new StringBuilder("");
        FileInputStream input = new FileInputStream(file);

        while ((len = input.read(temp)) > 0) {
            sb.append(new String(temp, 0, len));
        }

        input.close();
        return sb.toString();
    }

    //Add opened file into history
    public static void addToHistory(File file, Context context){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
/*
        if(sp.contains(file.getAbsolutePath())){
            editor.remove(file.getAbsolutePath());
            editor.commit();
        }
        */
        Log.d("!!!!!Add to History", "Yes");
        editor.putString(file.getAbsolutePath(), Tools.transDateToStr(new Date()));
        editor.commit();
    }

    public static void addToFavorite(String filepath, Context context){
        addToFavorite(new File(filepath), context);
    }

    public static void addToFavorite(File file, Context context){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(!sp.contains(file.getAbsolutePath())){
            Log.d("!!!!!Add to Favorite", "Yes");
            editor.putString(file.getAbsolutePath(), Tools.transDateToStr(new Date()));
            editor.commit();
        }else{
            Toast.makeText(context, "Already existed in Favorite", Toast.LENGTH_LONG).show();
        }

    }
}
