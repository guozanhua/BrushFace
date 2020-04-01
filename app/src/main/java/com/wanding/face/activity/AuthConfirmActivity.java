package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.wxpayface.IWxPayfaceCallback;
import com.tencent.wxpayface.WxPayFace;
import com.tencent.wxpayface.WxfacePayCommonCode;
import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.MainActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.bean.AuthConfirmReqDate;
import com.wanding.face.bean.AuthResultResponse;
import com.wanding.face.bean.UserBean;
import com.wanding.face.bean.WdPreAuthHistoryVO;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.payutil.PayParamsReqUtil;
import com.wanding.face.print.USBPrintTextUtil;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.Map;

/**
 * 预授权完成
 */
@ContentView(R.layout.activity_auth_confirm)
public class AuthConfirmActivity extends BaseActivity implements View.OnClickListener {



    @ViewInject(R.id.scanner_hint_imgBack)
    ImageView imgBack;
    @ViewInject(R.id.scanner_hint_imgHint)
    ImageView imgHint;
    @ViewInject(R.id.scanner_hint_msgTitle)
    TextView tvHintMsgTitle;
    @ViewInject(R.id.scanner_hint_msg)
    TextView tvHintMsg;

    @ViewInject(R.id.auth_confirm_authCode)
    ClearEditText etAuthCode;
    @ViewInject(R.id.auth_confirm_authTotalFeeTitle)
    TextView tvAuthTotalFeeTitle;
    @ViewInject(R.id.auth_confirm_authTotalFee)
    ClearEditText etAuthTotalFee;

    @ViewInject(R.id.auth_confirm_btOk)
    Button btOk;

    MySyntherizer synthesizer = MainActivity.synthesizer;

    private UserBean userBean;
    /**
     * 预授权订单对象
     */
    private WdPreAuthHistoryVO authOrder;
    /**
     * 预授权业务操作
     * authAction:1 :预授权，2：预授权撤销，3：预授权完成，4：预授权完成撤销
     */
    private String authAction;

    String etCodeStr;
    String etTotalFeeStr;

    /**
     * 支付成功返回实体
     */
    AuthResultResponse authResultResponse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvHintMsgTitle.setVisibility(View.GONE);
        tvHintMsg.setText(getResources().getString(R.string.auth_code_hint));
        btOk.setText(R.string.auth_manage_authConfirm);
        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");
        authOrder = (WdPreAuthHistoryVO) intent.getSerializableExtra("authOrder");
        authAction = intent.getStringExtra("authAction");
        initView();
        initListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        AnimationSet animationSet = (AnimationSet)AnimationUtils.loadAnimation(this, R.anim.up_anim);
        imgHint.startAnimation(animationSet);
        Log.e(TAG,"onStart....");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭扫码（从快捷键F2进入）
        if(authOrder==null){
            stopScanner();
        }

    }

    private void initView(){
        if(authOrder!=null){
            //从订单详情进入（不开启扫码）
            if(Utils.isNotEmpty(authAction))
            {
                if("1".equals(authAction))
                {
                    btOk.setText(R.string.auth_manage_auth);

                }else if("2".equals(authAction))
                {
                    tvAuthTotalFeeTitle.setVisibility(View.GONE);
                    etAuthTotalFee.setVisibility(View.GONE);

                    etAuthCode.setEnabled(false);
                    etAuthCode.setText(authOrder.getMchntOrderNo());
                    etAuthTotalFee.setEnabled(false);
                    etAuthTotalFee.setText(DecimalUtil.StringToPrice(authOrder.getOrderAmt()));
                    btOk.setText(R.string.auth_manage_authCancel);
                    speak("请点击预授权撤销！");
                }else if("3".equals(authAction))
                {

                    etAuthCode.setEnabled(false);
                    etAuthCode.setText(authOrder.getMchntOrderNo());
                    btOk.setText(R.string.auth_manage_authConfirm);
                    speak("请输入消费金额！");
                }else if("4".equals(authAction))
                {
                    etAuthCode.setEnabled(false);
                    etAuthCode.setText(authOrder.getMchntOrderNo());
                    btOk.setText(R.string.auth_manage_authConfirmCancel);
                    speak("请输入消费金额！");
                }
            }
        }else{
            //从快捷键进入
            if(Utils.isNotEmpty(authAction))
            {
                if("1".equals(authAction))
                {
                    btOk.setText(R.string.auth_manage_auth);

                }else if("2".equals(authAction))
                {

                    tvAuthTotalFeeTitle.setVisibility(View.GONE);
                    etAuthTotalFee.setVisibility(View.GONE);
                    btOk.setText(R.string.auth_manage_authCancel);
                    speak("请输入原授权单号！");
                }else if("3".equals(authAction))
                {

                    btOk.setText(R.string.auth_manage_authConfirm);
                    speak("请输入原授权单号！");
                }else if("4".equals(authAction))
                {
                    btOk.setText(R.string.auth_manage_authConfirmCancel);
                    speak("请输入原授权单号！");
                }
            }
            //启动扫码（从快捷键F2进入）
            openScanner();
        }


    }

    private void initListener(){
        imgBack.setOnClickListener(this);
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
     * 开启摄像头扫码
     */
    private void openScanner(){
        WxPayFace.getInstance().startCodeScanner(new IWxPayfaceCallback() {
            @Override
            public void response(Map info) throws RemoteException {
                if (!isSuccessInfo(info)) {
                    return;
                }
                //{return_code=SUCCESS, code_msg=134665614656016576, return_msg=scan code success}
                Log.e("获取扫码返回:",info.toString());
                String code_msg = info.get("code_msg").toString();
                if(Utils.isNotEmpty(code_msg)){

                    int msg = NetworkUtils.MSG_WHAT_ONEHUNDRED;
                    String text = code_msg;
                    sendMessage(msg,text);



                }else{
                    ToastUtil.showText(activity,"扫码返回结果为空！",1);
                }


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



    private boolean isSuccessInfo(Map mapInfo) {
        if (mapInfo == null) {
            showToast("调用返回为空, 请查看日志");
            new RuntimeException("调用返回为空").printStackTrace();
            return false;
        }
        String code = (String)mapInfo.get(Constants.RETURN_CODE);
        String msg = (String)mapInfo.get(Constants.RETURN_MSG);
        Log.e(TAG, "response | getWxpayfaceRawdata " + code + " | " + msg);
        if (code == null || !code.equals(WxfacePayCommonCode.VAL_RSP_PARAMS_SUCCESS)) {
            showToast("调用返回非成功信息, 请查看日志");
            new RuntimeException("调用返回非成功信息: " + msg).printStackTrace();
            finish();
            return false;
        }
        Log.e(TAG, "调用返回成功");
        return true;
    }

    /**
     * 预授权完成
     */
    private void authConfirm(){
        showCustomDialog();
        //支付时金额以分为单位
        String totalFeeStr = DecimalUtil.elementToBranch(etTotalFeeStr);
        final AuthConfirmReqDate request = PayParamsReqUtil.authConfirmReq(userBean,etCodeStr,totalFeeStr);
        final String url = NitConfig.authConfirmUrl;
        Log.e(TAG,"请求地址："+url);
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = FastJsonUtil.toJSONString(request);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    Log.e("请求参数：", content);
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("返回结果：", jsonStr);
                    int msg = NetworkUtils.MSG_WHAT_ONE;
                    String text = jsonStr;
                    sendMessage(msg,text);
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_JSON_CODE,NetworkUtils.REQUEST_JSON_TEXT);
                }catch (IOException e){
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_IO_CODE,NetworkUtils.REQUEST_IO_TEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_CODE,NetworkUtils.REQUEST_TEXT);
                }
            }
        }.start();
    }

    /**
     * 预授权撤销
     */
    private void authCancel() {
        showCustomDialog();
        final String url = NitConfig.authCancelUrl;
        Log.e("请求地址：", url);
        final AuthConfirmReqDate request = PayParamsReqUtil.authCancelReq(userBean,etCodeStr);
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = FastJsonUtil.toJSONString(request);
                    Log.e("发起请求参数：", content);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("返回字符串结果：", jsonStr);
                    int msg = NetworkUtils.MSG_WHAT_TWO;
                    String text = jsonStr;
                    sendMessage(msg,text);

                } catch (JSONException e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_JSON_CODE,NetworkUtils.REQUEST_JSON_TEXT);
                }catch (IOException e){
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_IO_CODE,NetworkUtils.REQUEST_IO_TEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_CODE,NetworkUtils.REQUEST_TEXT);
                }
            }
        }.start();

    }

    /**
     * 预授权完成撤销
     */
    private void authConfirmCancel() {
        showCustomDialog();
        final String url = NitConfig.authConfirmCancelUrl;
        Log.e("请求地址：", url);
        //支付时金额以分为单位
        String totalFeeStr = DecimalUtil.elementToBranch(etTotalFeeStr);
        final AuthConfirmReqDate request = PayParamsReqUtil.authConfirmCancelReq(userBean,etCodeStr,totalFeeStr);
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = FastJsonUtil.toJSONString(request);
                    Log.e("发起请求参数：", content);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("返回字符串结果：", jsonStr);
                    int msg = NetworkUtils.MSG_WHAT_THREE;
                    String text = jsonStr;
                    sendMessage(msg,text);

                } catch (JSONException e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_JSON_CODE,NetworkUtils.REQUEST_JSON_TEXT);
                }catch (IOException e){
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_IO_CODE,NetworkUtils.REQUEST_IO_TEXT);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_CODE,NetworkUtils.REQUEST_TEXT);
                }
            }
        }.start();

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
                    String code_msg = (String) msg.obj;
                    etAuthCode.setText("");
                    etAuthCode.setText(code_msg);
                    etAuthCode.setSelection(etAuthCode.getText().toString().length());
                    break;
                case NetworkUtils.MSG_WHAT_ONE:
                    String authConfirmJson = (String) msg.obj;
                    authConfirmJson(authConfirmJson);
                    hideCustomDialog();
                    break;
                case NetworkUtils.MSG_WHAT_TWO:
                    String authCancelJson = (String) msg.obj;
                    authCancelJson(authCancelJson);
                    hideCustomDialog();
                    break;
                case NetworkUtils.MSG_WHAT_THREE:
                    String authConfirmCancelJson=(String) msg.obj;
                    authConfirmJson(authConfirmCancelJson);
                    hideCustomDialog();
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

    private void authConfirmJson(String jsonStr){
        String speakMsg = "";
        try{
            Gson gjson  =  GsonUtils.getGson();
            authResultResponse = gjson.fromJson(jsonStr, AuthResultResponse.class);
            //return_code	响应码：“01”成功 ，02”失败，请求成功不代表业务处理成功
            String return_codeStr = authResultResponse.getReturn_code();
            //return_msg	返回信息提示，“支付成功”，“支付中”，“请求受限”等
            String return_msgStr = authResultResponse.getReturn_msg();
            //result_code	业务结果：“01”成功 ，02”失败 ，“03”支付中
            String result_codeStr = authResultResponse.getResult_code();
            String result_msgStr = authResultResponse.getResult_msg();
            if("01".equals(return_codeStr)) {
                if("01".equals(result_codeStr)){
                    //语音提示
                    startSpeakMsg();
                    //打印小票
                    startPrint();
                    ToastUtil.showText(activity, "交易成功！", 1);

                }else{
                    Toast.makeText(activity, result_msgStr, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(activity, return_msgStr, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            speak("交易结果返回错误");
            ToastUtil.showText(activity,"交易结果返回错误！",1);

        }
        finish();

    }

    private void authCancelJson(String jsonStr){
        try{
            Gson gjson  =  GsonUtils.getGson();
            authResultResponse = gjson.fromJson(jsonStr, AuthResultResponse.class);
            //return_code	响应码：“01”成功 ，02”失败，请求成功不代表业务处理成功
            String return_codeStr = authResultResponse.getReturn_code();
            //return_msg	返回信息提示，“支付成功”，“支付中”，“请求受限”等
            String return_msgStr = authResultResponse.getReturn_msg();
            //result_code	业务结果：“01”成功 ，02”失败 ，“03”支付中
            String result_codeStr = authResultResponse.getResult_code();
            String result_msgStr = authResultResponse.getResult_msg();
            if("01".equals(return_codeStr)) {
                if("01".equals(result_codeStr)){
                    if(Utils.isEmpty(authResultResponse.getTotal_amount())){
                        if(Utils.isNotEmpty(authOrder.getOrderAmt())){
                            String totalFee = DecimalUtil.StringToPrice(authOrder.getOrderAmt());
                            String totalAmount = DecimalUtil.elementToBranch(totalFee);
                            authResultResponse.setTotal_amount(totalAmount);
                        }else{
                            authResultResponse.setTotal_amount("");
                        }

                    }
                    //语音提示
                    startSpeakMsg();
                    //打印小票
                    startPrint();
                    ToastUtil.showText(activity, "交易成功！", 1);

                }else{
                    Toast.makeText(activity, result_msgStr, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(activity, return_msgStr, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            speak("交易结果返回错误");
            ToastUtil.showText(activity,"交易结果返回错误！",1);

        }
        finish();
    }
    private void authConfirmCancelJson(String jsonStr){
        try {
            JSONObject job = new JSONObject(jsonStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startSpeakMsg(){
        try{
            if(Utils.isNotEmpty(authAction))
            {
                if("1".equals(authAction))
                {



                }else if("2".equals(authAction))
                {
                    String totalFee = authResultResponse.getTotal_amount();
                    if(Utils.isNotEmpty(totalFee)){
                        speak("交易成功，预授权撤销"+DecimalUtil.branchToElement(totalFee)+"元");
                    }else{
                        speak("交易成功");
                    }



                }else if("3".equals(authAction))
                {
                    String totalFee = authResultResponse.getConsume_fee();
                    if(Utils.isNotEmpty(totalFee)){
                        speak("交易成功，预授权完成"+DecimalUtil.branchToElement(totalFee)+"元");
                    }else{
                        speak("交易成功");
                    }

                }else if("4".equals(authAction))
                {
                    String totalFee = authResultResponse.getRefund_fee();
                    if(Utils.isNotEmpty(totalFee)){
                        speak("交易成功，预授权完成撤销"+DecimalUtil.branchToElement(totalFee)+"元");
                    }else{
                        speak("交易成功");
                    }

                }
            }
        }catch (Exception e){
            speak("交易金额返回错误！");
        }

    }


    private void startPrint(){

//        USBPrinter.initPrinter(this);
        try{
            if(Utils.isNotEmpty(authAction))
            {
                if("1".equals(authAction))
                {
                    USBPrintTextUtil.authOrderText(userBean,authResultResponse);

                }else if("2".equals(authAction))
                {

                    USBPrintTextUtil.authCancelOrderText(userBean,authResultResponse);

                }else if("3".equals(authAction))
                {
                    USBPrintTextUtil.authConfirmOrderText(userBean,authResultResponse);

                }else if("4".equals(authAction))
                {
                    USBPrintTextUtil.authConfirmCancelOrderText(userBean,authResultResponse);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.showText(activity,"打印发生错误！",1);
        }


        /*if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState() )
        {
            ToastUtil.showText( activity, getString( R.string.str_cann_printer ),1 );
            return;
        }
        threadPool = ThreadPool.getInstantiation();
        threadPool.addTask( new Runnable()
        {
            @Override
            public void run()
            {
                if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.ESC )
                {
                    if(Utils.isNotEmpty(authAction))
                    {
                        if("1".equals(authAction))
                        {
                            JiaBoPrintTextUtil.authOrderText(id,userBean,authResultResponse);

                        }else if("2".equals(authAction))
                        {
                            JiaBoPrintTextUtil.authCancelOrderText(id,userBean,authResultResponse);

                        }else if("3".equals(authAction))
                        {
                            JiaBoPrintTextUtil.authConfirmOrderText(id,userBean,authResultResponse);

                        }else if("4".equals(authAction))
                        {
                            JiaBoPrintTextUtil.authConfirmCancelOrderText(id,userBean,authResultResponse);

                        }
                    }
                } else {
                    mHandler.obtainMessage( PRINTER_COMMAND_ERROR ).sendToTarget();
                }
            }
        } );*/
    }

//    /**
//     * 青蛙自带键盘
//     */
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ADD){
//            /*0键*/
//            Log.e(TAG,"按下+号键，定义为取消操作");
//            speak("取消");
//            finish();
//
//        }
//        if(event.getAction() == KeyEvent.ACTION_DOWN){
//            if(event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER||event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
//                Log.e(TAG,"按下回车键");
//
//                upEnterAction();
//            }
//        }
//        /**
//         * 这里直接return true，防止事件向上传递，改变最终输入结果
//         */
////        return super.dispatchKeyEvent(event);
//        return true;
//    }

    /**
     * 点击确定按钮操作
     */
    private void upEnterAction(){
        if(Utils.isFastClick()){
            return;
        }
        etCodeStr = etAuthCode.getText().toString().trim();
        etTotalFeeStr = etAuthTotalFee.getText().toString().trim();
        if(Utils.isEmpty(etCodeStr)){
            speak("请输入原授权单号！");
            ToastUtil.showText(activity, "原授权码不能为空！", 1);
            return;
        }

        if(Utils.isNotEmpty(authAction))
        {
            if("1".equals(authAction))
            {

            }else if("2".equals(authAction))
            {

                authCancel();

            }else if("3".equals(authAction))
            {
                if(Utils.isEmpty(etTotalFeeStr)){
                    speak("请输入授权金额！");
                    ToastUtil.showText(activity, "授权金额不能为空！", 1);
                    return;
                }
                authConfirm();

            }else if("4".equals(authAction))
            {
                if(Utils.isEmpty(etTotalFeeStr)){
                    speak("请输入授权金额！");
                    ToastUtil.showText(activity, "授权金额不能为空！", 1);
                    return;
                }
                authConfirmCancel();

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanner_hint_imgBack:
                finish();
                break;
            case R.id.auth_confirm_btOk:
                upEnterAction();
                break;
        }
    }

}
