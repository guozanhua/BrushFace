package com.wanding.face.jiabo.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanding.face.R;
import com.wanding.face.jiabo.device.bean.DeviceStateData;

import java.util.List;

/**
 * wifi云打印机设备Item
 */
public class DeviceListAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceStateData> list;
    private LayoutInflater inflater;

    public DeviceListAdapter(Context context, List<DeviceStateData> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        LinearLayout layout;
        TextView tvDeviceId;//设备ID
        TextView tvOnLineState;//在线状态
        TextView tvTerminalState;//设备状态
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceStateData data = list.get(position);
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.device_list_item,null);
            vh.tvDeviceId = convertView.findViewById(R.id.device_list_item_tvDeviceId);
            vh.tvOnLineState = convertView.findViewById(R.id.device_list_item_tvOnLineState);
            vh.tvTerminalState = convertView.findViewById(R.id.device_list_item_tvTerminalState);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvDeviceId.setText(data.getDeviceID());

        vh.tvOnLineState.setText(data.getOnlineState(data.getOnline()));
        vh.tvTerminalState.setText(data.getTerminalState(data.getStatus()));

        return convertView;
    }
}
