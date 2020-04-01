package com.wanding.face.jiabo.device.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;


/**  打印处理工具类  */
public class BluetoothService {
	

	private BluetoothAdapter bluetoothAdapter;



    
    public BluetoothService(BluetoothAdapter bluetoothAdapter) {
         this.bluetoothAdapter = bluetoothAdapter;

    } 
    

    
    /**
     * 打开蓝牙
     */
    @SuppressLint("NewApi")
    public void openBluetooth(Activity activity,int reqestCode) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, reqestCode);
	}

    /**
     * 关闭蓝牙
     */
    @SuppressLint("NewApi")
    public void closeBluetooth() {
    	this.bluetoothAdapter.disable();
    }
    
    /**  
     * 判断蓝牙是否打开  
     *   
     * @return boolean  
     */    
    public boolean isOpen() {    
    	return this.bluetoothAdapter.isEnabled();    
    } 
    
    /**  
     * 搜索蓝牙设备  
     */    
    public void searchDevices() {
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去    
        this.bluetoothAdapter.startDiscovery();    
    }  
    

    

}
