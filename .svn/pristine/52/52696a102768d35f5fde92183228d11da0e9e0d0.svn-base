package com.wanding.face.usb.keyboar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.ICheckListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.geekmaker.paykeyboard.PayKeyboard;
import com.geekmaker.paykeyboard.USBDetector;
import com.sym.libs.usbprint.USBPrinter;
import com.sym.libs.util.ToastUtil;

/**
 * Time: 2019/12/5
 * Author:Administrator
 * Description:
 */
public class USBKeyboard implements ICheckListener {

    private static final String TAG = "键盘工具类";
    private Context mContext;
    private static USBKeyboard mInstance;
    private PayKeyboard keyboard;
    private USBDetector detector;

    /**
     * 键盘keyCode和keyName
     */
    public static final int KEY_CODE_REFUND = 12;
    public static final String KEY_NAME_REFUND = "REFUND";
    public static final int KEY_CODE_BACK = 28;
    public static final String KEY_NAME_BACK = "Backspace";
    public static final int KEY_CODE_OPT = 29;
    public static final String KEY_NAME_OPT = "OPT";
    public static final int KEY_CODE_LIST = 30;
    public static final String KEY_NAME_LIST = "LIST";
    public static final int KEY_CODE_ESC = 21;
    public static final String KEY_NAME_ESC = "ESC";
    public static final int KEY_CODE_PAY = 23;
    public static final String KEY_NAME_PAY = "PAY";

    /**
     * 广播注册监听键盘状态
     */
    public static final String IS_USB_OPEN_STATE = "is.usb.open_state";
    public static final String KEYBOARD_STATE_AVAILABLE = "available";
    public static final String KEYBOARD_STATE_RELEASE = "release";
    public static final String KEYBOARD_STATE_EXCEPTION = "exception";

    /**
     * 定义外部类接收键盘按键屏幕的值以及监听回调
     */
    private int keyCode = -1;
    private String keyName = "ESC";
    private String keyboardText = "0";
    private double money = 0;
    private OnKeyboardValueListener onKeyboardValueListener;

    public void getOnKeyboardValueListener(OnKeyboardValueListener onKeyboardValueListener){
        this.onKeyboardValueListener = onKeyboardValueListener;
    }

    public interface OnKeyboardValueListener{
        void getKeyboardValue(int keyCode,String keyName,double money,String text);
    }



    public static USBKeyboard getInstance() {
        if (mInstance == null) {
            synchronized (USBKeyboard.class) {
                if (mInstance == null) {
                    mInstance = new USBKeyboard();
                }
            }
        }
        return mInstance;
    }

    public void openKayboard(Context context){
        mContext = context;
        if(keyboard==null||keyboard.isReleased()){
            keyboard = PayKeyboard.get(mContext);
            if(keyboard!=null) {
                keyboard.setListener(new DefaultKeyboardListener() {
                    Intent intent = null;
                    @Override
                    public void onRelease() {
                        super.onRelease();
                        keyboard = null;
                        intent = new Intent(IS_USB_OPEN_STATE);
                        intent.putExtra("state",KEYBOARD_STATE_RELEASE);
                        mContext.sendBroadcast(intent);
                        Log.e("键盘被释放KeyboardUI", "Keyboard release!!!!!!");
                    }
                    @Override
                    public void onDisplayUpdate(final String text) {
                        super.onDisplayUpdate(text);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                keyboardText = text;
                                if("pay---".equals(text)){
                                    if(keyboard!=null){
                                        keyboard.reset();
                                    }
                                }

//                                onKeyboardValueListener.getKeyboardValue(keyCode,keyName,money,keyboardText);
                                Log.e(TAG,String.format("lastupdate  : %s \n ",text));
                                Log.e(TAG,String.format("money  : %s \n ",money));
                            }
                        });
                        Log.e("键盘显示屏KeyboardUI",String.format("display update %s",text));
                    }

                    @Override
                    public void onAvailable() {
                        super.onAvailable();
                        if(keyboard==null){
                            return;
                        }
                        intent = new Intent(IS_USB_OPEN_STATE);
                        intent.putExtra("state",KEYBOARD_STATE_AVAILABLE);
                        mContext.sendBroadcast(intent);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e("KeyboardUI", "usb exception!!!!");
                        keyboard = null;
                        super.onException(e);
                        intent = new Intent(IS_USB_OPEN_STATE);
                            intent.putExtra("state",KEYBOARD_STATE_EXCEPTION);
                        mContext.sendBroadcast(intent);
                    }

                    @Override
                    public void onPay(final IPayRequest request) {
                        super.onPay(request);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                money = request.getMoney();
                                request.setResult(true);
                                Log.e("键盘刷脸键KeyboardUI",String.format("display update %s",String.valueOf(money)));
//                                final AlertDialog.Builder normalDialog =
//                                        new AlertDialog.Builder(mContext);
//                                normalDialog.setTitle("支付提示");
//                                normalDialog.setMessage(String.format("请支付 %.2f 元", money));
//                                normalDialog.setPositiveButton("支付成功",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                request.setResult(true);
//                                            }
//                                        });
//                                normalDialog.setNegativeButton("支付失败",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                request.setResult(false);
//                                            }
//                                        });
//                                normalDialog.show();
                            }
                        });
                    }

                    @Override
                    public void onKeyDown(final int keyCode, final String keyName) {
                        super.onKeyDown(keyCode, keyName);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(onKeyboardValueListener!=null){
                                    onKeyboardValueListener.getKeyboardValue(keyCode,keyName,money,keyboardText);
                                }
                                Log.e(TAG,String.format("key down event code : %s, name: %s \n ", keyCode, keyName));

                            }
                        });


                    }


                    @Override
                    public void onKeyUp(int keyCode, String keyName) {
                        super.onKeyUp(keyCode, keyName);
                    }
                });
                keyboard.open();

            }
        }else{
            Log.i("KeyboardUI","keyboard exists!!!");
        }
    }


    /**
     * 重置键盘状态
     */
    public void reset(){
        if(keyboard!=null){
            keyboard.reset();
        }
    }

    /**
     * 获取键盘状态
     */


    /**
     * 设置键盘显示内容
     */
    public void showTip(String text){
        if(keyboard!=null){
            keyboard.showTip(text);
        }
    }


    /**
     * 释放键盘资源
     */
    public void release(){
        if(keyboard!=null){
            keyboard.release();
            keyboard=null;

        }
        if(detector!=null){
            detector.release();
            detector = null;
        }
        onKeyboardValueListener  = null;

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    @Override
    public void onAttach() {
        openKayboard(mContext);
    }
}
