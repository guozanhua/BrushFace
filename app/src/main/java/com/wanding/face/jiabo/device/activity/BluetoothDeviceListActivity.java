package com.wanding.face.jiabo.device.activity;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.R;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 蓝牙设备界面
 */
@ContentView(R.layout.activity_bluetooth_device_list)
public class BluetoothDeviceListActivity extends BaseActivity implements View.OnClickListener {

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

    @ViewInject(R.id.bluetooth_device_listView)
    ListView mListView;
    @ViewInject(R.id.bluetooth_device_btScan)
    Button btScan;

    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> DevicesArrayAdapter;

    public static final int REQUEST_ENABLE_BT = 2;
    public static final String EXTRA_DEVICE_ADDRESS = "address";

    List<String> list = new ArrayList<>();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();

        initBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFindBlueToothReceiver);
    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        btScan.setOnClickListener(this);
    }

    /**
     * 注册蓝牙广播
     */
    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
    }

    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    String name = device.getName();
                    String address = device.getAddress();
                    //只添加匹配打印机名称的设备
                    if(Utils.isNotEmpty(name)){

                        DevicesArrayAdapter.add(name + "\n" + address);


                    }
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                setProgressBarIndeterminateVisibility(false);
//                setTitle(R.string.select_bluetooth_device);
                Log.i("tag", "finish discovery" + (DevicesArrayAdapter.getCount()-2));
                if (DevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(
                            R.string.none_bluetooth_device_found).toString();
                    DevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };


    private void initBluetooth(){
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            ToastUtil.showText(this, "Bluetooth is not supported by the device",1);
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,
                        REQUEST_ENABLE_BT);
            } else {
                getDeviceList();
            }
        }
    }

    protected void getDeviceList() {
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        DevicesArrayAdapter = new ArrayAdapter<>(this,R.layout.bluetooth_device_list_item);
        mListView.setAdapter(DevicesArrayAdapter);
        mListView.setOnItemClickListener(mDeviceClickListener);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        DevicesArrayAdapter.add(getString(R.string.str_title_pairedev));
        if (pairedDevices.size() > 0) {
            //  tvPairedDevice.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                String name = device.getName();
                String address = device.getAddress();

                DevicesArrayAdapter.add(name + "\n" + address);

            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired)
                    .toString();
            DevicesArrayAdapter.add(noDevices);
        }
    }

    private void discoveryDevice() {
        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
//        setTitle(R.string.scaning);
        // Turn on sub-title for new devices
        //tvNewDevice.setVisibility(View.VISIBLE);
        DevicesArrayAdapter.clear();
        DevicesArrayAdapter.add(getString(R.string.str_title_newdev));
        // lvNewDevice.setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String noDevices = getResources().getText(R.string.none_paired).toString();
            String noNewDevice = getResources().getText(R.string.none_bluetooth_device_found).toString();
            Log.i("tag", info);
            if (!info.equals(noDevices) && !info.equals(noNewDevice)&&!info.equals(getString(R.string.str_title_newdev))&&!info.equals(getString(R.string.str_title_pairedev))) {
                //停止搜索
                mBluetoothAdapter.cancelDiscovery();
                String address = info.substring(info.length() - 17);
                // Create the result Intent and include the MAC address
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.bluetooth_device_btScan:
                if(Utils.isFastClick()){
                    return;
                }
                discoveryDevice();
                break;
                default:
                    break;
        }
    }

  /*  @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            *//*0键*//*
            Log.e(TAG,"按下键盘回退/删除键");
            finish();
        }
        return true;
    }*/
}
