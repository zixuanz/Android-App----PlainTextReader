package com.zixuanz.plaintextreader.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zixuanz.plaintextreader.R;
import com.zixuanz.plaintextreader.data.IconTextDateItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Zixuan Zhao on 9/14/16.
 */
public class IconTextDateAdapter extends BaseAdapter{

    private List<IconTextDateItem> items;
    private Context context;
    SimpleDateFormat df;

    public IconTextDateAdapter(List<IconTextDateItem> items, Context context){
        this.items = items;
        this.context = context;
        if(Build.VERSION.SDK_INT >=24) {
            df = new SimpleDateFormat("MMM dd, yyyy", context.getResources().getConfiguration().getLocales().get(0));
        }else{
            df = new SimpleDateFormat("MMM dd, yyyy", context.getResources().getConfiguration().locale);
        }
    }

    @Override
    public int getCount() {
        return items==null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty(){
        return items.isEmpty();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_icon_text_date,parent,false);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.lvitd_icon);
            holder.name = (TextView) convertView.findViewById(R.id.lvitd_text);
            holder.date = (TextView) convertView.findViewById(R.id.lvitd_date);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        holder.icon.setBackgroundResource(items.get(position).getIcon());
        holder.name.setText(items.get(position).getName());
        holder.date.setText(df.format(items.get(position).getDate()));
        return convertView;
    }

    private static class ViewHolder{
        ImageView icon;
        TextView name;
        TextView date;
    }


}


