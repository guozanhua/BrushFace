package com.wanding.face.jiabo.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanding.face.R;
import com.wanding.face.jiabo.device.bean.BlueToothBean;

import java.util.ArrayList;

public class BluetoothMatchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BlueToothBean> lsDevice;
    private LayoutInflater inflater;

    public BluetoothMatchAdapter(Context context,
                               ArrayList<BlueToothBean> lsDevice) {
        super();
        this.context = context;
        this.lsDevice = lsDevice;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lsDevice.size();
    }

    @Override
    public Object getItem(int position) {
        return lsDevice.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView tvName;
        ImageView imgChecked;
    }

    @Override
    public View getView(int position, View subView, ViewGroup parent) {
        BlueToothBean device = lsDevice.get(position);
        ViewHolder vh = null;
        if(subView == null)
        {
            subView = inflater.inflate(R.layout.bluetooth_match_item, null);
            vh = new ViewHolder();
            vh.tvName = (TextView) subView.findViewById(R.id.device_name);
            vh.imgChecked = (ImageView) subView.findViewById(R.id.device_checkImg);
            subView.setTag(vh);
        }else
        {
            vh = (ViewHolder) subView.getTag();
        }
        vh.tvName.setText(device.getName());
        String checked = device.getChecked();
        if(checked.equals("0"))
        {
            vh.imgChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_uncheck));
        }else if(checked.equals("1"))
        {
            vh.imgChecked.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_check));
        }
        return subView;
    }
}
