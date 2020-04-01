package com.wanding.face.jiabo.device.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.R;
import com.wanding.face.jiabo.device.adapter.BluetoothMatchAdapter;
import com.wanding.face.jiabo.device.bean.BlueToothBean;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 蓝牙管理界面
 */
@ContentView(R.layout.activity_bluetooth_manage)
public class BluetoothActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.menu_title_imageView)
    ImageView imgBack;
    @ViewInject(R.id.menu_title_layout)
    LinearLayout titleLayout;
    @ViewInject(R.id.menu_title_tvTitle)
    TextView tvTitle;
    @ViewInject(R.id.menu_title_imgTitleImg)
    ImageView imgTitleImg;
    @ViewInject(R.id.menu_title_tvOption)
    TextView tvOption;

    @ViewInject(R.id.bluetooth_manage_switch)
    Switch bluetoothSwitch;
    /**
     * matchList：已匹配设备
     * noMatchList：未匹配设备
     */
    @ViewInject(R.id.bluetooth_manage_matchList)
    ListView matchList;
    @ViewInject(R.id.bluetooth_manage_noMatchList)
    ListView noMatchList;
    @ViewInject(R.id.bluetooth_manage_btScan)
    Button btScan;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothService bluetoothService = null;


    /**
     * 用于存放未配对蓝牙设备
     */
    private ArrayList<BluetoothDevice> listNoMatch = new ArrayList<BluetoothDevice>();
    /**
     * 用于存放已配对蓝牙设备
     */
    private ArrayList<BluetoothDevice> listMatch = new ArrayList<BluetoothDevice>();

    /**
     * 用于存放已配对蓝牙设备
     */
    private ArrayList<BlueToothBean> lsBluetooth =  new ArrayList<BlueToothBean>();

    private BluetoothMatchAdapter mAdapter;

    /**
     * 打开蓝牙请求码
     */
    private static final int OPEN_BLUETOOTH_REQUEST = 0x001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText(getString(R.string.bluetooth_device));
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        initListener();

        initDevice();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBluetoothReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        bluetoothSwitch.setOnCheckedChangeListener(this);
        btScan.setOnClickListener(this);
    }

    /**  初始化蓝牙打印设置  */
    private void initDevice(){
        bluetoothService = new BluetoothService(bluetoothAdapter);


        //检测设备是否有蓝牙设备存在
        if(bluetoothAdapter != null)
        {
            //检查蓝牙是否打开
            if (this.bluetoothService.isOpen())
            {
                updateUI(true);
            }else
            {
                updateUI(false);
            }

        }else
        {
            ToastUtil.showText(activity,"当前设备不支持蓝牙打印",1);
            finish();
        }

    }

    /**
     * 注册蓝牙事件广播
     */
    private void registerBluetoothReceiver(){
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);    //搜索中
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始搜索
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);    //结束搜索
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //监听蓝牙绑定的状态
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 蓝牙广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.e(TAG,"搜索设备中");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    addBandDevices(device);
                } else {
                    addUnbondDevices(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.e(TAG,"开始搜索");
                showCustomDialog("搜索设备中...");

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.e(TAG,"搜索完毕！");
                hideCustomDialog();


                addUnbondDevicesToListView();
                addBondDevicesToListView();
                // bluetoothAdapter.cancelDiscovery();
            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    ToastUtil.showText(context,"打开蓝牙！",1);


                    matchList.setEnabled(true);
                    noMatchList.setEnabled(true);
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                    ToastUtil.showText(context,"关闭蓝牙！",1);


                    matchList.setEnabled(false);
                    noMatchList.setEnabled(false);
                }
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 更新蓝牙设备的绑定状态
                if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.e(TAG,"正在配对");
                } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.e(TAG,"完成配对");
                    // 连接成功 将绑定好的设备添加的已绑定list集合
                    listMatch.add(device);
                    // 将绑定好的设备从未绑定list集合中移除
                    listNoMatch.remove(device);
                    addBondDevicesToListView();
                    addUnbondDevicesToListView();
                } else if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.e(TAG,"取消配对");
                }
            }
        }
    };

    /**
     * 添加未绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addUnbondDevices(BluetoothDevice device) {
        String name = device.getName();
        Log.e(TAG,"未绑定设备名称：" + name);
        if (!listNoMatch.contains(device)&&Utils.isNotEmpty(name)) {
            listNoMatch.add(device);
        }
    }

    /**
     * 添加已绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addBandDevices(BluetoothDevice device) {
        String name = device.getName();
        Log.e(TAG,"已绑定设备名称：" + name);
        if (!listMatch.contains(device)) {
            listMatch.add(device);
        }
    }

    /**
     * 添加已绑定蓝牙设备到ListView
     */
    private void addBondDevicesToListView() {
        /**   使用自定义实体，自定义适配器显示数据 */
        int count = this.listMatch.size();
        Log.e(TAG,"已绑定设备数量：" + count+"");
        lsBluetooth.clear();
        for (int i = 0; i < count; i++) {
            BluetoothDevice device = listMatch.get(i);
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            BlueToothBean blue= new BlueToothBean();
            blue.setName(deviceName);
            blue.setAddress(deviceAddress);
            blue.setChecked("0");
            lsBluetooth.add(blue);


        }
        mAdapter = new BluetoothMatchAdapter(activity, lsBluetooth);
        // 把适配器装载到listView中
        this.matchList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                /**   使用自定义实体，自定义适配器显示数据时获取实体 */
                BlueToothBean device;
                try {
                    Log.e("psotion:", position+"");
                    Log.e("lsDevice", lsBluetooth+"");
                    Log.e("lsDevice的长度", lsBluetooth.size()+"");
                    device = lsBluetooth.get(position);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 添加未绑定蓝牙设备到ListView
     */
    private void addUnbondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = listNoMatch.size();
        Log.e(TAG,"未绑定设备数量：" + count+"");
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName", this.listNoMatch.get(i).getName());
            data.add(map);// 把item项的数据加到data中
        }
        String[] from = { "deviceName" };
        int[] to = { R.id.device_name };
        SimpleAdapter simpleAdapter = new SimpleAdapter(activity, data,
                R.layout.bluetooth_nomatch_item, from, to);

        // 把适配器装载到listView中
        noMatchList.setAdapter(simpleAdapter);
        // 为每个item绑定监听，用于设备间的配对
        noMatchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    String address = listNoMatch.get(position).getAddress();
                    // Create the result Intent and include the MAC address
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    // Set result and finish this Activity
                    setResult(Activity.RESULT_OK, intent);
                    finish();
//                    Log.e(TAG,"开始配对");
//                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
//                    createBondMethod.invoke(listNoMatch.get(position));

                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showText(activity,"配对失败！",1);
                }
            }
        });
    }

    /**
     * 根据蓝牙打开或关闭不同状态更改界面UI
     * true: 打开状态
     * false:关闭状态
     */
    private void updateUI(boolean flag){

        if(flag){
            bluetoothSwitch.setChecked(true);
            bluetoothSwitch.setSwitchTextAppearance(activity,R.style.switch_true);
            btScan.setBackgroundResource(R.drawable.button_style_0dp);
            btScan.setTextColor(ContextCompat.getColor(activity,R.color.white_ffffff));
            btScan.setEnabled(true);
        }else{
            bluetoothSwitch.setChecked(false);
            bluetoothSwitch.setSwitchTextAppearance(activity,R.style.switch_false);
            btScan.setBackgroundResource(R.drawable.bg_gray_frame1dp_radius0);
            btScan.setTextColor(ContextCompat.getColor(activity,R.color.white_f8f8f8));
            btScan.setEnabled(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.bluetooth_manage_btScan:
                if(Utils.isFastClick()){
                    return;
                }
                listMatch.clear();
                listNoMatch.clear();
                bluetoothService.searchDevices();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            if (!this.bluetoothService.isOpen())
            {
                // 蓝牙关闭的情况
                this.bluetoothService.openBluetooth(activity,OPEN_BLUETOOTH_REQUEST);
            }

            Log.e(TAG,"打开蓝牙！");
        }else{
            if (this.bluetoothService.isOpen())
            {
                // 蓝牙打开的情况
                this.bluetoothService.closeBluetooth();
            }
            updateUI(false);
            Log.e(TAG,"关闭蓝牙！");
        }


    }

    /**
     * 监听蓝牙打开是否成功
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (OPEN_BLUETOOTH_REQUEST == requestCode) {

            if (resultCode == RESULT_CANCELED) {
                Log.e(TAG,"打开蓝牙失败");
                updateUI(false);

            } else {
                Log.e(TAG,"打开蓝牙成功");
                updateUI(true);
            }
        }

    }
}
