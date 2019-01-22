package com.manam.andorid.mobeeverification.smsreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maria Abraham on 21/01/19.
 */
public class SMSAdapter extends BaseAdapter {

    private List<Object> smsList;
    private Context context;
    private LayoutInflater layoutInflater;
    private Animation animation;

    public SMSAdapter(Context context, List<Object> list, Animation animation) {
        this.context = context;
        this.smsList = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.animation = animation;
    }

    @Override
    public int getCount() {
        return smsList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return smsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case 0:
                    convertView = layoutInflater.inflate(R.layout.item_sms_list, parent, false);
                    break;
                case 1:
                    convertView = layoutInflater.inflate(R.layout.header_sms_list, parent, false);
                    break;
            }
        }
        switch (getItemViewType(position)) {
            case 0:
                SMSModel smsModel = (SMSModel) getItem(position);
                TextView sms = (TextView) convertView.findViewById(R.id.txt_item);
                sms.setText(String.format("%s  received at: %s", smsModel.getFullText(), smsModel.getDate()));
                break;
            case 1:
                TextView title = (TextView) convertView.findViewById(R.id.txt_header);
                String titleString = (String) getItem(position);
                title.setText(titleString);
                break;
        }
        if (position == 1 && animation != null) {
            convertView.startAnimation(animation);
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (smsList.get(position) instanceof SMSModel) {
            return 0;
        }
        return 1;
    }
}
