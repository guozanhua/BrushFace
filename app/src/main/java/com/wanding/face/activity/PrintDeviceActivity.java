/*
package com.wanding.face.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanding.face.BaseActivity;
import com.wanding.face.R;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.jiabo.device.DeviceConnFactoryManager;
import com.wanding.face.jiabo.device.ThreadPool;
import com.wanding.face.jiabo.device.activity.BluetoothDeviceListActivity;
import com.wanding.face.jiabo.device.activity.DeviceBaseActivity;
import com.wanding.face.jiabo.device.activity.WifiDeviceListActivity;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.DevicePopupWindow;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

*/
/**
 * 打印机设置
 *//*

@ContentView(R.layout.activity_print_device_setting)
public class PrintDeviceActivity extends DeviceBaseActivity implements View.OnClickListener ,DevicePopupWindow.OnSelectClickListener{

    public static final int BLUETOOTH_REQUEST_CODE = 0x001;

    @ViewInject(R.id.setting_device_content)
    LinearLayout myLayout;
    @ViewInject(R.id.print_device_setting_connectTypeLayout)
    RelativeLayout connectTypeLayout;

    @ViewInject(R.id.print_device_setting_tvConnectType)
    TextView tvConnectType;
    @ViewInject(R.id.print_device_setting_tvConnectState)
    TextView tvConnectState;


    private float alpha = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册获取USB连接设备实时状态广播
        IntentFilter filter = new IntentFilter(CONN_STATE_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initListener(){
        connectTypeLayout.setOnClickListener(this);
    }



    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            tvConnectState.setText(intent.getExtras().getString("data"));
        }
    };


   */
/* private BroadcastReceiver Receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent )
        {
            String action = intent.getAction();
            switch ( action )
            {
                case Constants.ACTION_USB_PERMISSION:
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra( UsbManager.EXTRA_DEVICE );
                        if ( intent.getBooleanExtra( UsbManager.EXTRA_PERMISSION_GRANTED, false ) )
                        {
                            if ( device != null )
                            {
                                System.out.println( "permission ok for device " + device );
                                usbConn( device );
                            }
                        } else {
                            System.out.println( "permission denied for device " + device );
                        }
                    }
                    break;
                *//*
*/
/* Usb连接断开、蓝牙连接断开广播 *//*
*/
/*
                case ACTION_USB_DEVICE_DETACHED:
                    mHandler.obtainMessage( CONN_STATE_DISCONN ).sendToTarget();
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra( DeviceConnFactoryManager.STATE, -1 );
                    int deviceId = intent.getIntExtra( DeviceConnFactoryManager.DEVICE_ID, -1 );
                    switch ( state )
                    {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if ( id == deviceId )
                            {


                                sharedPreferencesUtil.put(Constants.SHARE_DEV_STATE_CONNECT_KEY,Constants.SHARE_DEV_STATE_DISCONNECT);
                                tvConnectState.setText( getString( R.string.str_conn_state_disconnect ) );
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            tvConnectState.setText( getString( R.string.str_conn_state_connecting ) );
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            tvConnectState.setText( getString( R.string.str_conn_state_connected ) + "\n" + getConnDeviceInfo() );
                            //已连接成功
                            sharedPreferencesUtil.put(Constants.SHARE_DEV_STATE_CONNECT_KEY,Constants.SHARE_DEV_STATE_CONNECTED);

                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
                            ToastUtil.showText( activity, getString( R.string.str_conn_fail ) ,1);
                            *//*
*/
/* wificonn=false; *//*
*/
/*
                            tvConnectState.setText( getString( R.string.str_conn_state_disconnect ) );
                            tvConnectType.setText(getResources().getString(R.string.setting_no_connect_type));
                            sharedPreferencesUtil.put(Constants.SHARE_DEV_STATE_CONNECT_KEY,Constants.SHARE_DEV_STATE_DISCONNECT);
                            break;
                        default:
                            break;
                    }
                    break;
                case DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE:
                    if ( counts >= 0 )
                    {
                        if ( continuityprint )
                        {
                            printcount++;
                            ToastUtil.showText( activity, getString( R.string.str_continuityprinter ) + " " + printcount,1 );

                        }
                        if ( counts != 0 )
                        {
                            sendContinuityPrint();
                        }else {
                            continuityprint = false;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };*//*



    private Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            String errorJsonText = "";
            String url = "";
            switch (msg.what){
                case 1:
                    backgroundAlpha((float)msg.obj);
                    break;
            }
        }
    };

    */
/**
     * 背景渐变暗
     *//*

    private void setWindowBackground(){
        DevicePopupWindow popupWindow = new DevicePopupWindow(this);
        popupWindow.showPhotoWindow();
        popupWindow.setOnSelectClickListener(this);
        popupWindow.showAtLocation(myLayout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        popupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(1f);
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(alpha > 0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = NetworkUtils.MSG_WHAT_ONE;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha-=0.01f;
                    msg.obj = alpha ;
                    handler.sendMessage(msg);
                }
            }

        }).start();
    }

    */
/**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     *//*

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    */
/**
     * 返回或者点击空白位置的时候将背景透明度改回来
     *//*

    class poponDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            new Thread(new Runnable(){
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while(alpha<1f){
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("HeadPortrait","alpha:"+alpha);

                        Message msg = handler.obtainMessage();
                        msg.what = NetworkUtils.MSG_WHAT_ONE;
                        alpha+=0.01f;
                        msg.obj =alpha ;
                        handler.sendMessage(msg);
                    }
                }

            }).start();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK )
        {
            switch ( requestCode )
            {
                */
/*蓝牙连接*//*

                case BLUETOOTH_REQUEST_CODE: {
                    closeport();
                    */
/*获取蓝牙mac地址*//*

                    String macAddress = data.getStringExtra( BluetoothDeviceListActivity.EXTRA_DEVICE_ADDRESS );
                    */
/* 初始化话DeviceConnFactoryManager *//*

                    new DeviceConnFactoryManager.Build()
                            .setId( id )
                            */
/* 设置连接方式 *//*

                            .setConnMethod( DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH )
                            */
/* 设置连接的蓝牙mac地址 *//*

                            .setMacAddress( macAddress )
                            .build();
                    */
/* 打开端口 *//*

                    Log.d(TAG, "onActivityResult: 连接蓝牙"+id);
                    threadPool = ThreadPool.getInstantiation();
                    threadPool.addTask( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    } );

                    break;
                }
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.print_device_setting_connectTypeLayout:
                if(Utils.isFastClick()){
                    return;
                }
                setWindowBackground();
                break;
                default:
                    break;
        }
    }

    @Override
    public void selectListener(int type) {
        if(Utils.isFastClick()){
            return;
        }
        if(type == 1){
//            tvConnectType.setText(getResources().getString(R.string.setting_usb_connect_type));
            getUsbDeviceList();
        }else if(type == 2){
//            tvConnectType.setText(getResources().getString(R.string.setting_bluetooth_connect_type));
            startActivityForResult(new Intent(activity,BluetoothDeviceListActivity.class),BLUETOOTH_REQUEST_CODE);
        }else if(type == 3){
//            tvConnectType.setText(getResources().getString(R.string.setting_serialport_connect_type));
        }else if(type == 4){
            tvConnectType.setText(getResources().getString(R.string.setting_wifi_connect_type));
            startActivity(new Intent(activity,WifiDeviceListActivity.class));
        }else{
//            tvConnectType.setText(getResources().getString(R.string.setting_no_connect_type));
        }

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            */
/*0键*//*

            Log.e(TAG,"按下键盘回退/删除键");
            finish();
        }
        return true;
    }
}
*/
