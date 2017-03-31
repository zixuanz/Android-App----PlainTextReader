package com.zixuanz.plaintextreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zixuanz.plaintextreader.R;
import com.zixuanz.plaintextreader.data.IconTextItem;

import java.util.List;

/**
 * Created by Zixuan Zhao on 11/28/16.
 */

public class IconTextAdapter extends BaseAdapter{

    private List<IconTextItem> items;
    private Context context;

    public IconTextAdapter(List<IconTextItem> items, Context context){
        this.items = items;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_icon_text,parent,false);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.lv_icon);
            holder.name = (TextView) convertView.findViewById(R.id.lv_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setBackgroundResource(items.get(position).getIcon());
        holder.name.setText(items.get(position).getName());
        return convertView;
    }

    private static class ViewHolder{
        ImageView icon;
        TextView name;
    }
}


