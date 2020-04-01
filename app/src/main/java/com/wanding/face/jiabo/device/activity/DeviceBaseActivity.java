package com.wanding.face.jiabo.device.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;

import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.R;
import com.wanding.face.jiabo.device.DeviceConnFactoryManager;
import com.wanding.face.jiabo.device.PrinterCommand;
import com.wanding.face.jiabo.device.ThreadFactoryBuilder;
import com.wanding.face.jiabo.device.ThreadPool;
import com.wanding.face.jiabo.device.UsbManagerUtil;
import com.wanding.face.utils.SharedPreferencesUtil;
import com.wanding.face.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

/**
 * 打印机
 * https://blog.csdn.net/kk_369147/article/details/78316593（USB通信）
 * https://blog.csdn.net/yang_jian1314/article/details/72896135（USB通信）
 * https://blog.csdn.net/andrexpert/article/details/85311122（USB通信）
 *
 * https://blog.csdn.net/weixin_33922670/article/details/89656857
 */
public class DeviceBaseActivity extends BaseActivity {


    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    public int	id = 0;
    private UsbManager usbManager;

    private PendingIntent mPermissionIntent;
    public ThreadPool threadPool;
    public int	counts;
    public boolean	continuityprint = false;
    public int	printcount	= 0;

    /**
     * 打印机连接权限
     */
    private static final int REQUEST_CODE = 0x004;
    ArrayList<String> per	= new ArrayList<>();
    private	String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
    };

    /**
     * 已连接USB
     */
    private static final int CONN_STATE_CONNECTED = 0x101;

    /**
     * 已断开USB 连接状态断开
     */
    public static final int CONN_STATE_DISCONN = 0x007;


    /**
     * 使用打印机指令错误
     */
    public static final int PRINTER_COMMAND_ERROR = 0x008;

    /**
     * 请先连接打印机
     */
    public static final int	CONN_PRINTER		= 0x12;


    /**
     * 保存打印机状态
     */
    public SharedPreferencesUtil sharedPreferencesUtil;




    /**
     * 定义其他Activity获取连接状态广播动作
     */
    public static final String CONN_STATE_ACTION = "device.conn.action";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            sharedPreferencesUtil = new SharedPreferencesUtil(activity, Constants.DEVICE_FILE_NAME);
            usbManager = (UsbManager) getSystemService( Context.USB_SERVICE );
            checkPermission();
            requestPermission();
            //监听USB设备插拔状态以及连接自定义连接状态
            printRegisterReceiver();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }


    public void printRegisterReceiver() {
        try{
            IntentFilter filter = new IntentFilter();
            filter.addAction( UsbManager.ACTION_USB_DEVICE_DETACHED );//当USB设备拨出时执行
            filter.addAction( DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE );
            filter.addAction( DeviceConnFactoryManager.ACTION_CONN_STATE );
            filter.addAction( UsbManager.ACTION_USB_DEVICE_ATTACHED );//当USB设备插入时执行
            registerReceiver( receiver, filter );
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent )
        {
            String action = intent.getAction();
            switch ( action )
            {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED: // 插入USB设备
                    mHandler.obtainMessage( CONN_STATE_CONNECTED ).sendToTarget();
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED://拔出USB
                    mHandler.obtainMessage( CONN_STATE_DISCONN ).sendToTarget();
                    break;
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

                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra( DeviceConnFactoryManager.STATE, -1 );
                    int deviceId = intent.getIntExtra( DeviceConnFactoryManager.DEVICE_ID, -1 );
                    Intent in = null;
                    switch ( state )
                    {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if ( id == deviceId )
                            {

                                //连接状态：未连接
//                                mHandler.obtainMessage( CONN_STATE_CONNECTED ).sendToTarget();
                                in = new Intent(CONN_STATE_ACTION);
                                in.putExtra("data", Constants.DEVICE_STATE_DISCONNET);
                                sendBroadcast(in);
                                hideCustomDialog();
                                sharedPreferencesUtil.put(Constants.DEVICE_STATE_KEY,Constants.DEVICE_STATE_DISCONNET);
                                sharedPreferencesUtil.put(Constants.DEVICE_TYPE_KEY,"");

                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            //连接状态：连接中
//                            mHandler.obtainMessage( CONN_STATE_CONNECTED ).sendToTarget();
                            in = new Intent(CONN_STATE_ACTION);
                            in.putExtra("data", Constants.DEVICE_STATE_CONNING);
                            sendBroadcast(in);
                            showCustomDialog("连接中...");
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            //连接状态：已连接
//                            mHandler.obtainMessage( CONN_STATE_CONNECTED ).sendToTarget();
                            in = new Intent(CONN_STATE_ACTION);
                            in.putExtra("data", Constants.DEVICE_STATE_CONNETED);
                            sendBroadcast(in);
                            hideCustomDialog();
                            //获取连接方式
                            String connetedType = getConnetedType();
                            sharedPreferencesUtil.put(Constants.DEVICE_STATE_KEY,Constants.DEVICE_STATE_CONNETED);
                            sharedPreferencesUtil.put(Constants.DEVICE_TYPE_KEY,connetedType);
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
                            //连接状态：连接失败
//                            mHandler.obtainMessage( CONN_STATE_CONNECTED ).sendToTarget();
                            in = new Intent(CONN_STATE_ACTION);
                            in.putExtra("data", Constants.DEVICE_STATE_CONN_FAIL);
                            sendBroadcast(in);
                            hideCustomDialog();
                            sharedPreferencesUtil.put(Constants.DEVICE_STATE_KEY,Constants.DEVICE_STATE_CONN_FAIL);
                            sharedPreferencesUtil.put(Constants.DEVICE_TYPE_KEY,"");
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };




    private void checkPermission()
    {
        for ( String permission : permissions )
        {
            if ( PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission( this, permission ) )
            {
                per.add( permission );
            }
        }
    }


    private void requestPermission()
    {
        if ( per.size() > 0 )
        {
            String[] p = new String[per.size()];
            ActivityCompat.requestPermissions( this, per.toArray( p ), REQUEST_CODE );
        }
    }



    public void getUsbDeviceList() {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        Log.e(TAG,"设备count:"+count);
        if (count > 0) {
            if(count==5){
                while (deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    String devicename = device.getDeviceName();
                    if (checkUsbDevicePidVid(device)) {
                        Log.e(TAG,"获取打印设备名称："+devicename);
                        closeport();
                        /* 通过USB设备名找到USB设备 */
                        UsbDevice usbDevice = UsbManagerUtil.getUsbDeviceFromName( activity, devicename );
                        /* 判断USB设备是否有权限 */
                        if ( usbManager.hasPermission( usbDevice ) )
                        {
                            usbConn( usbDevice );
                        } else {
                            /* 请求权限 */
                            mPermissionIntent = PendingIntent.getBroadcast( this, 0, new Intent( Constants.ACTION_USB_PERMISSION ), 0 );
                            usbManager.requestPermission( usbDevice, mPermissionIntent );
                        }
                    }else{

                        Log.e(TAG, getResources().getString(R.string.not_found_device));

                    }
                }
            }else{
                ToastUtil.showText(activity,getResources().getString(R.string.not_found_device),1);
            }

        } else {
            Log.e(TAG, getResources().getString(R.string.not_found_device));
            ToastUtil.showText(activity,getResources().getString(R.string.not_found_device),1);

        }
    }

    public Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage( Message msg )
        {
            switch ( msg.what )
            {
                case CONN_STATE_CONNECTED://已连接USB
//                    Intent intent = new Intent(CONN_STATE_ACTION);
//                    intent.putExtra("data", "conning");
//                    sendBroadcast(intent);
                    break;
                case CONN_STATE_DISCONN://连接断开
                    try{
                        if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null || !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState() )
                        {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort( id );

                            ToastUtil.showText( activity, getResources().getString( R.string.str_disconnect_success ),1);
                            hideCustomDialog();
                            sharedPreferencesUtil.put(Constants.DEVICE_STATE_KEY,Constants.DEVICE_STATE_DISCONNET);
                            sharedPreferencesUtil.put(Constants.DEVICE_TYPE_KEY,"");

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                case PRINTER_COMMAND_ERROR:
                    ToastUtil.showText( activity, getResources().getString( R.string.str_choice_printer_command),1);
                    break;
                case CONN_PRINTER:
                    ToastUtil.showText( activity, getString( R.string.str_cann_printer),1 );
                    break;
                case Constants.MESSAGE_UPDATE_PARAMETER:
                    String strIp = msg.getData().getString( "Ip" );
                    String strPort = msg.getData().getString( "Port" );
                    /* 初始化端口信息 */
                    new DeviceConnFactoryManager.Build()
                            /* 设置端口连接方式 */
                            .setConnMethod( DeviceConnFactoryManager.CONN_METHOD.WIFI )
                            /* 设置端口IP地址 */
                            .setIp( strIp )
                            /* 设置端口ID（主要用于连接多设备） */
                            .setId( id )
                            /* 设置连接的热点端口号 */
                            .setPort( Integer.parseInt( strPort ) )
                            .build();
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
                default:
                    new DeviceConnFactoryManager.Build()
                            /* 设置端口连接方式 */
                            .setConnMethod( DeviceConnFactoryManager.CONN_METHOD.WIFI )
                            /* 设置端口IP地址 */
                            .setIp( "192.168.2.227" )
                            /* 设置端口ID（主要用于连接多设备） */
                            .setId( id )
                            /* 设置连接的热点端口号 */
                            .setPort( 9100 )
                            .build();
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
        }
    };

    public void sendContinuityPrint()
    {
        ThreadPool.getInstantiation().addTask( new Runnable()
        {
            @Override
            public void run()
            {
                if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null
                        && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState() )
                {
                    ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder( "MainActivity_sendContinuity_Timer" );
                    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor( 1, threadFactoryBuilder );
                    scheduledExecutorService.schedule( threadFactoryBuilder.newThread( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            counts--;
                            if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC )
                            {
                                //已连接成功，发送票据
//                                sendReceiptWithResponse();
                            } else if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC )
                            {
                                /* 标签模式可直接使用LabelCommand.addPrint()方法进行打印 */
//                                sendLabel();
                                /*
                                 *                                for(int i=0;i<sl.length;i++){ //  8个商品，8个数量
                                 *                                    if(sss(i)>=36){
                                 *                                        sendLabel(i,"第"+i+"个商品");
                                 *                                        break;
                                 *                                    }
                                 *                                }
                                 */
                            }else {
//                                sendCpcl();
                            }
                        }
                    } ), 1000, TimeUnit.MILLISECONDS );
                }
            }
        } );
    }

    public boolean checkUsbDevicePidVid(UsbDevice dev) {
        int pid = dev.getProductId();
        int vid = dev.getVendorId();
        return ((vid == 34918 && pid == 256) || (vid == 1137 && pid == 85)
                || (vid == 6790 && pid == 30084)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 512)
                || (vid == 26728 && pid == 256) || (vid == 26728 && pid == 768)
                || (vid == 26728 && pid == 1024) || (vid == 26728 && pid == 1280)
                || (vid == 26728 && pid == 1536));
    }

    /**
     * 重新连接回收上次连接的对象，避免内存泄漏
     */
    public void closeport()
    {
        try{
            if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null &&DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort != null )
            {
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].reader.cancel();
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort.closePort();
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * usb连接
     *
     * @param usbDevice
     */
    public void usbConn( UsbDevice usbDevice )
    {
        new DeviceConnFactoryManager.Build()
                .setId( id )
                .setConnMethod( DeviceConnFactoryManager.CONN_METHOD.USB )
                .setUsbDevice( usbDevice )
                .setContext( this )
                .build();
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
    }

    public String getConnDeviceInfo()
    {
        String str = "";
        DeviceConnFactoryManager	deviceConnFactoryManager	= DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
        if ( deviceConnFactoryManager != null
                && deviceConnFactoryManager.getConnState() )
        {
            if ( "USB".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "USB\n";
                str	+= "USB Name: " + deviceConnFactoryManager.usbDevice().getDeviceName();
            } else if ( "WIFI".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "WIFI\n";
                str	+= "IP: " + deviceConnFactoryManager.getIp() + "\t";
                str	+= "Port: " + deviceConnFactoryManager.getPort();
            } else if ( "BLUETOOTH".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "BLUETOOTH\n";
                str	+= "MacAddress: " + deviceConnFactoryManager.getMacAddress();
            } else if ( "SERIAL_PORT".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "SERIAL_PORT\n";
                str	+= "Path: " + deviceConnFactoryManager.getSerialPortPath() + "\t";
                str	+= "Baudrate: " + deviceConnFactoryManager.getBaudrate();
            }
        }
        return(str);
    }

    public String getConnetedType()
    {
        String str = "";
        DeviceConnFactoryManager	deviceConnFactoryManager	= DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
        if ( deviceConnFactoryManager != null
                && deviceConnFactoryManager.getConnState() )
        {
            if ( "USB".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "USB";
            } else if ( "WIFI".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "WIFI";
            } else if ( "BLUETOOTH".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "蓝牙";
            } else if ( "SERIAL_PORT".equals( deviceConnFactoryManager.getConnMethod().toString() ) )
            {
                str	+= "串口";
            }
        }
        return(str);
    }

}
