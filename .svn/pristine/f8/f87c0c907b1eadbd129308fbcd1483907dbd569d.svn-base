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
public class CofyScanQrcodeActivity extends BaseActivity implements View.OnClickListener,ICheckListener {

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

    private UserBean userBean;

    Dialog scanQrcodeDialog;

    /**
     * 定制键盘
     */
    public PayKeyboard keyboard;
    public USBDetector detector;

    /**
     * 键盘初始化标记
     * 为0表示界面启动，和界面恢复时键盘初始化，主要作用为初始化时etSumMoney不显示默认值0
     */
    public int onCreateIndex = 0;
    /**
     * 标记键盘执行的标记
     */
    public int keyboardIndex = 0;

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
    protected void onResume() {
        super.onResume();
        openKeyboard();
    }

    @Override
    protected void onStop() {
        Log.e("KeyboardUI","activity destroy!!!!!!");
        super.onStop();
        if(keyboard!=null){
            // keyboard.release();
            keyboard.release();
            keyboard=null;

        }
        if(detector!=null){
            detector.release();
            detector = null;
        }

    }

    /**
     * 定制键盘
     */
    private void openKeyboard(){

        if(keyboard==null||keyboard.isReleased()){
            keyboard = PayKeyboard.get(activity);
            if(keyboard!=null) {
                keyboard.setListener(new DefaultKeyboardListener() {
                    @Override
                    public void onRelease() {
                        super.onRelease();
                        keyboard = null;
                        Log.e(TAG, "KeyboardUI:Keyboard release!!!!!!");
                    }

                    @Override
                    public void onDisplayUpdate(final String text) {
                        super.onDisplayUpdate(text);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                if(onCreateIndex > 0&&!"0".equals(text)){
//                                    keyboardIndex = 0;
//                                    etRefundCode.setText(text);
//                                }else{
//                                    etRefundCode.setText("");
//                                }
//                                etRefundCode.setSelection(etRefundCode.getText().toString().length());
                                Log.e(TAG,String.format("lastupdate  : %s \n ",text));
                            }
                        });
                        Log.i(TAG,"KeyboardUI:"+String.format("display update %s",text));
                    }

                    @Override
                    public void onAvailable() {
                        super.onAvailable();
                        if(keyboard==null){
                            return;
                        }
                        onCreateIndex ++;
                        Log.e(TAG,"键盘可用！");
//                        updateSignal();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                keyboard.showTip("8.a.1.6.6.8.8.9");
//                            }
//                        },1000);
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.i("KeyboardUI", "usb exception!!!!");
                        keyboard = null;
                        super.onException(e);
                    }

                    @Override
                    public void onPay(final IPayRequest request) {
                        super.onPay(request);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(Utils.isFastClick()){
                                    return;
                                }



                                //键盘支付结果回调
                                request.setResult(true);
                            }
                        });


                    }

                    @Override
                    public void onKeyDown(final int keyCode, final String keyName) {
                        super.onKeyDown(keyCode, keyName);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onCreateIndex ++;
                                keyboardIndex ++;
                                Log.e(TAG,String.format("key down event code : %s, name: %s \n ", keyCode, keyName));
                                toActivity(keyCode,keyName);
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

        speak("等待输入退款单号！");
    }

    private void toActivity(int keyCode,String keyName){
        if(Utils.isFastClick()){
            return;
        }
        if(keyCode == 21 &&keyName.equals("ESC")){
            speak("取消");
            activity.finish();
        }
    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        imgScan.setOnClickListener(this);
        btOk.setOnClickListener(this);
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
        scanQrcodeDialog = new Dialog(this,R.style.dialog);
        Window dialogWindow = scanQrcodeDialog.getWindow();
        WindowManager.LayoutParams params = scanQrcodeDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(params);
        scanQrcodeDialog.setContentView(view);
        //点击屏幕和物理返回键dialog不消失
//        scanQrcodeDialog.setCancelable(false);
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

    @Override
    public void onAttach() {
        openKeyboard();
    }
}
