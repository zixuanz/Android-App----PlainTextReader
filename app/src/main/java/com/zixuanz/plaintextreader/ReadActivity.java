package com.zixuanz.plaintextreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.zixuanz.plaintextreader.utils.FileUtils;

import java.text.ParseException;
import java.util.Date;

public class ReadActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private TextView title;
    private NestedScrollView scrollView;
    private TextView text;
    private TextView tts, like, increase, decrease;

    private String filepath = "";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharedPreferences settingSp;

    private int fontSize, maxFontSize, minFontSize;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        initData();
        initViews();



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_read, menu);
    }

    //add to favorite list
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.context_menu_add_favorite:
                editor.putString(filepath, new Date().toString());
                if(editor.commit()){
                    Toast.makeText(ReadActivity.this, R.string.read_file_add_success, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ReadActivity.this, R.string.read_file_add_fail, Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSettings();
        loadSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sp.edit();
        editor.putInt(filepath, scrollView.getScrollY());
        editor.commit();


        Log.d("ReadActivity:::", "onPause()");
        float h = text.getLineSpacingExtra();
        Log.d("!!!!LineSpacingExtra", String.valueOf(h));
        int height = text.getLineHeight();
        Log.d("!!!!!Lineheight", String.valueOf(height));
        height = text.getLineCount();
        Log.d("!!!!line count", String.valueOf(height));
        h = text.getPaint().getFontSpacing();
        Log.d("!!!!font spacing", String.valueOf(h));

        height = scrollView.getMaxScrollAmount();
        Log.d("!!!!MaxScrollAmount", String.valueOf(height));
        height = scrollView.getMeasuredHeight();
        Log.d("!!!!MeasureHeight", String.valueOf(height));
        height = scrollView.getHeight();
        Log.d("!!!!ScrollHeight", String.valueOf(height));
        height = scrollView.getScrollY();
        Log.d("!!!!ScrollY", String.valueOf(height));
        height = scrollView.getMaxScrollAmount();
        Log.d("!!!!ScrollBottom", String.valueOf(height));

    }

    @Override
    public void onClick(View v) {

        if(v.equals(like)){
            FileUtils.addToFavorite(filepath, ReadActivity.this);
        }

        if (v.equals(increase)){
            fontSize *= 1.5;
            if(fontSize > maxFontSize){
                fontSize = maxFontSize;
            }
            text.setTextSize(fontSize);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_font_size)+fontSize+"px", Toast.LENGTH_SHORT).show();
        }

        if (v.equals(decrease)){
            fontSize *= 0.6;
            if(fontSize < minFontSize){
                fontSize = minFontSize;
            }
            text.setTextSize(fontSize);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_font_size)+fontSize+"px", Toast.LENGTH_SHORT).show();
        }


    }

    private void initData(){

        sp = ReadActivity.this.getSharedPreferences("record", MODE_PRIVATE);
        editor = getApplicationContext().getSharedPreferences("favorite", MODE_PRIVATE).edit();
        settingSp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        maxFontSize = getResources().getDimensionPixelOffset(R.dimen.max_read_font_size);
        minFontSize = getResources().getDimensionPixelOffset(R.dimen.def_read_font_size);

        updateSettings();
    }

    private void initViews(){
        toolbar = (Toolbar) findViewById(R.id.tb_read);
        setSupportActionBar(toolbar);


        title = (TextView) findViewById(R.id.tv_read_title);
        tts = (TextView) findViewById(R.id.tv_read_tts);
        like = (TextView) findViewById(R.id.tv_read_like);
        increase = (TextView) findViewById(R.id.tv_read_increase);
        decrease = (TextView) findViewById(R.id.tv_read_decrease);

        tts.setOnClickListener(this);
        like.setOnClickListener(this);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);


        initTextArea();
    }

    private void initTextArea(){
        scrollView = (NestedScrollView) findViewById(R.id.sv_read);

        text = (TextView) findViewById(R.id.tv_read_text);

        //textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        //textView.getLayout().getLineTop(40);


        Intent intent = getIntent();

        if (intent!=null && intent.hasExtra("Text")) {
            text.setText(intent.getStringExtra("Text"));
            filepath = intent.getStringExtra("Path");
            title.setText(intent.getStringExtra("name"));

            text.post(new Runnable() {
                @Override
                public void run() {
                    int position = sp.getInt(filepath, 0);
                    scrollView.smoothScrollTo(0, position);
                }
            });

        }
        registerForContextMenu(text);
    }



    private void loadSettings(){
        text.setTextSize(fontSize);
    }

    private void updateSettings(){
        fontSize = Integer.parseInt(settingSp.getString("setting_font_size", "-1"));
        if(fontSize == -1){
            fontSize = getResources().getDimensionPixelSize(R.dimen.def_read_font_size);
        }


    }


}
