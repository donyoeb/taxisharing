package com.google.texxxi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class room_list_adapter extends BaseAdapter {

    private ArrayList<room_list_item> room_list_items = new ArrayList<room_list_item>() ;
    String k;

    public room_list_adapter(){

    }
    @Override
    public int getCount() {
        return room_list_items.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View converview, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if (converview == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context
                    .LAYOUT_INFLATER_SERVICE);
            converview = inflater.inflate(R.layout.room_list_item,viewGroup,false);
        }

        TextView startspot_list = (TextView) converview.findViewById(R.id.startspotlist);  // room_list_item.xml 의 id값
        TextView arrivespot_list = (TextView) converview.findViewById(R.id.arrivespotlist);
        TextView time_list = (TextView) converview.findViewById(R.id.timelist);
        //TextView join_list = (TextView) converview.findViewById(R.id.joinlist);

        room_list_item room_list_item = room_list_items.get(i);


        startspot_list.setText(room_list_item.getStart_list());
        arrivespot_list.setText(room_list_item.getArrive_list());
        time_list.setText(room_list_item.getTime_list_hour()+"시 "+ room_list_item.getTime_list_min()+"분");
        //join_list.setText(room_list_item.getJoin_list());

        return converview;
    }

    public void addItem(String sl , String al , String tlh, String tlm){
        room_list_item item = new room_list_item(sl,al,tlh,tlm);

        item.setStart_list(sl);
        item.setArrive_list(al);
        item.setTime_list_hour(tlh);
        item.setTime_list_min(tlm);

        room_list_items.add(item);
    }
}
