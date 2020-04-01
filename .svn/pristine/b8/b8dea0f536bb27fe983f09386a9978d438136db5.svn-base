package com.wanding.face.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.ICheckListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.geekmaker.paykeyboard.PayKeyboard;
import com.geekmaker.paykeyboard.USBDetector;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;
import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.MainActivity;
import com.wanding.face.R;
import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.payutil.PayUtils;
import com.wanding.face.usb.keyboar.USBKeyboard;
import com.wanding.face.usb.keyboar.USBKeyboard.OnKeyboardValueListener;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * Time: 2019/11/28
 * Author:Administrator
 * Description: 扫码界面
 */
@ContentView(R.layout.activity_scan_qrcode)
public class ScanQrcodeActivity extends BaseActivity implements View.OnClickListener,OnKeyboardValueListener {

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

    @ViewInject(R.id.scan_qrcode_imgScan)
    ImageView imgScan;
    @ViewInject(R.id.scan_qrcode_etRefundCode)
    ClearEditText etRefundCode;
    @ViewInject(R.id.scan_qrcode_btOk)
    Button btOk;

    /**
     * 百度语音合成
     */
    MySyntherizer synthesizer = MainActivity.synthesizer;
    USBKeyboard usbKeyboard = MainActivity.usbKeyboard;
    private boolean isKeyboardValidity = true;


    private UserBean userBean;

    Dialog scanQrcodeDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("扫码退款");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");
        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");

        initListener();

        etRefundCode.setEnabled(false);
        startCodeScanner();
    }

    @Override
    protected void onStart() {
        super.onStart();

        speak("等待输入退款单号！");
        Log.e(TAG,"onStart....");
    }

    @Override
    protected void onResume() {
        super.onResume();
        isKeyboardValidity = true;
        Log.e(TAG,"onResume....");
    }



    @Override
    protected void onStop() {
        super.onStop();
        isKeyboardValidity = false;
        Log.e(TAG,"onStop....");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(usbKeyboard!=null){
//
//            usbKeyboard.release();
//            usbKeyboard = null;
//        }

        scanQrcodeDialog = null;

        Log.e(TAG,"onDestroy....");
    }

    /**
     * 定制键盘
     */
    @Override
    public void getKeyboardValue(int keyCode, String keyName, double money, String text) {
        Log.e(TAG,"接收的金额："+String.valueOf(money));
        Log.e(TAG,"屏幕更新的值："+text);
        if(isKeyboardValidity) {
            if (USBKeyboard.KEY_CODE_ESC == keyCode && USBKeyboard.KEY_NAME_ESC.equals(keyName)) {
                //点击取消键显示
                speak("取消");
                if(usbKeyboard!=null){
                    usbKeyboard.release();
                    usbKeyboard = null;
                }
                finish();


            }
        }
    }

    /**
     * 青蛙自带键盘
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ADD){
            /*0键*/
            Log.e(TAG,"按下+号键，定义为取消操作");
            speak("取消");
            finish();

        }
        /**
         * 这里直接return true，防止事件向上传递，改变最终输入结果
         */
//        return super.dispatchKeyEvent(event);
        return true;
    }


    private void initListener(){
        imgBack.setOnClickListener(this);
        imgScan.setOnClickListener(this);
        btOk.setOnClickListener(this);
        //初始化键盘
        usbKeyboard.getOnKeyboardValueListener(this);
    }

    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak(String text) {

        int result = synthesizer.speak(text);
        checkResult(result, "speak");
    }

    /**
     * 暂停播放
     */
    private void stop() {

        int result = synthesizer.stop();
        checkResult(result, "speak");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
//            toPrint("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
            Log.e("error code :", result+" method:" + method );
        }
    }

    /**
     * 开启扫码
     */
    private void startCodeScanner(){
        showScanQrcodeDialog();
        WxPayFace.getInstance().startCodeScanner(new IWxPayfaceCallback() {
            @Override
            public void response(Map info) throws RemoteException {
                if (info == null) {
                    showToast("扫码失败,扫码返回为空！");
                    finish();
                    return;
                }
                String code = (String)info.get(Constants.RETURN_CODE);
                String msg = (String)info.get(Constants.RETURN_MSG);
                Log.e(TAG, "response | getWxpayfaceRawdata " + code + " | " + msg);
                if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
                    showToast("扫码失败！");
                    finish();
                    return;
                }
                //{return_code=SUCCESS, code_msg=134665614656016576, return_msg=scan code success}
                Log.e("获取扫码返回:",info.toString());
                String code_msg = info.get("code_msg").toString();
                if(Utils.isNotEmpty(code_msg)){

                    int what = NetworkUtils.MSG_WHAT_ONEHUNDRED;
                    String text = code_msg;
                    sendMessage(what,text);

                }else{
                    ToastUtil.showText(activity,"扫码返回结果为空！",1);
                }
                if(scanQrcodeDialog!=null&&scanQrcodeDialog.isShowing()){
                    scanQrcodeDialog.dismiss();
                }

            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    /**
     * 停止扫码
     */
    private void stopScanner(){
        try{
            WxPayFace.getInstance().stopCodeScanner();
        }catch(Exception e){
            e.printStackTrace();
            showToast("停止扫码SDK调用失败！！");
        }
    }



    public void sendMessage(int what,String text){
        Message msg = new Message();
        msg.what = what;
        msg.obj = text;
        handler.sendMessage(msg);
    }


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String errorJsonText = "";
            switch (msg.what){
                case NetworkUtils.MSG_WHAT_ONEHUNDRED:
                    speak("扫码成功！");
                    stopScanner();
                    String code_msg = (String) msg.obj;
                    etRefundCode.setText(code_msg);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    checkedOrderId();
                    break;
                case NetworkUtils.MSG_WHAT_ONE:

                    break;

                case NetworkUtils.REQUEST_JSON_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);


                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_IO_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);

                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_CODE:
                    errorJsonText = (String) msg.obj;
                    ToastUtil.showText(activity,errorJsonText,1);

                    hideCustomDialog();
                    break;
                default:
                    finish();
                    break;
            }
        }
    };

    /**
     * 扫码提示对话框
     */
    private void showScanQrcodeDialog(){
        View view = LayoutInflater.from(this).inflate(R.layout.scan_qrcode_hint_dialog, null);
        ImageView imgClose = view.findViewById(R.id.scan_qrcode_hint_dialog_imgClose);
        TextView tvScanHintTitle = view.findViewById(R.id.scan_qrcode_hint_dialog_tvScanHintTitle);
        scanQrcodeDialog = new Dialog(this,R.style.dialog);
        Window dialogWindow = scanQrcodeDialog.getWindow();
        WindowManager.LayoutParams params = scanQrcodeDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(params);
        scanQrcodeDialog.setContentView(view);
        //点击屏幕和物理返回键dialog不消失
        scanQrcodeDialog.setCancelable(false);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanQrcodeDialog!=null&&scanQrcodeDialog.isShowing()){
                    scanQrcodeDialog.dismiss();
                }
                stopScanner();
            }
        });
        scanQrcodeDialog.show();
    }

    private void checkedOrderId(){
        String etRefundCodeStr = etRefundCode.getText().toString();
        if(Utils.isEmpty(etRefundCodeStr)){
            ToastUtil.showText(activity,"请先输入退款单号！",1);
            speak("请先输入退款单号");
        }
        OrderDetailData order = new OrderDetailData();
        order.setOrderId(etRefundCodeStr);
        order.setGoodsPrice("");


        Intent intent = new Intent();
        intent.setClass(activity,RefundActivity.class);
        intent.putExtra("userBean",userBean);
        intent.putExtra("order",order);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.scan_qrcode_imgScan:
                if(Utils.isFastClick()){
                    return;
                }
                etRefundCode.setText("");
                startCodeScanner();
                break;
            case R.id.scan_qrcode_btOk:
                if(Utils.isFastClick()){
                    return;
                }

                checkedOrderId();


                break;
        }
    }



}
