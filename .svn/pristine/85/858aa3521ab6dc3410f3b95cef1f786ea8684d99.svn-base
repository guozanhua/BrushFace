package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.wanding.face.bean.CardVerificaResData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.print.USBPrintTextUtil;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.MySerialize;
import com.wanding.face.utils.SharedPreferencesUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.Map;

/**
 * 卡券核销
 */
@ContentView(R.layout.activity_card_verifica)
public class CardVerificaActivity extends BaseActivity implements View.OnClickListener {



    @ViewInject(R.id.scanner_hint_imgBack)
    ImageView imgBack;
    @ViewInject(R.id.scanner_hint_imgHint)
    ImageView imgHint;
    @ViewInject(R.id.scanner_hint_msgTitle)
    TextView tvHintMsgTitle;
    @ViewInject(R.id.scanner_hint_msg)
    TextView tvHintMsg;

    @ViewInject(R.id.card_veridica_etCode)
    ClearEditText etCode;

    @ViewInject(R.id.card_veridica_btOk)
    Button btOk;

    private UserBean userBean;

    MySyntherizer synthesizer = MainActivity.synthesizer;

    String etCodeStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvHintMsgTitle.setVisibility(View.GONE);
        tvHintMsg.setText(getResources().getString(R.string.brush_san_verifica));

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");

        initListener();

        openScanner();
        speak("请将核销劵二维码对准摄像头！");
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
        stopScanner();
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
     * 查询券
     */
    private void queryCode(){
        showCustomDialog();
        final String url = NitConfig.queryCodeUrl;
        Log.e(TAG,"查询券对应地址："+url);
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("mid",userBean.getMid());
                    userJSON.put("code",etCodeStr);
                    userJSON.put("terminal_id",userBean.getTerminal_id());
                    String content = String.valueOf(userJSON);

                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    Log.e("查询券对应订单请求参数：", content);
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("查询券对应订单返回结果：", jsonStr);
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
     * 核销券
     */
    private void cardVerifica(final String id){
        showCustomDialog();
        final String url = NitConfig.consumeCodeUrl;
        Log.e(TAG,"核销券对应地址："+url);
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("mid",userBean.getMid());
                    userJSON.put("code",etCodeStr);
                    userJSON.put("couponId",String.valueOf(id));
                    userJSON.put("terminal_id",userBean.getTerminal_id());
                    String content = String.valueOf(userJSON);

                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    Log.e("核销券请求参数：", content);
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("核销券返回结果：", jsonStr);
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
                    etCode.setText("");
                    etCode.setText(code_msg);
                    etCode.setSelection(etCode.getText().toString().length());
                    speak("扫码成功！");
                    submitCodeVerifica();
                    break;
                case NetworkUtils.MSG_WHAT_ONE:
                    String queryCodeJson = (String) msg.obj;
                    queryCodeJson(queryCodeJson);
                    hideCustomDialog();
                    break;
                case NetworkUtils.MSG_WHAT_TWO:
                    String cardVerificaJson = (String) msg.obj;
                    cardVerificaJson(cardVerificaJson);
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

    private void queryCodeJson(String jsonStr){
        String speakMsg = "";
        try {
            JSONObject job = new JSONObject(jsonStr);
            String code = job.getString("code");
            String msg = job.getString("msg");
            if("000000".equals(code)){
                String subCode = job.getString("subCode");
                String subMsg = job.getString("subMsg");
                if("000000".equals(subCode)){
                    String dataJson = job.getString("data");
                    JSONObject dataJob = new JSONObject(dataJson);

                    String id = dataJob.getString("id");
                    //核劵
                    cardVerifica(id);
                }else{
                    if(Utils.isNotEmpty(subMsg)){
                        speakMsg = subMsg;
                        Toast.makeText(activity, subMsg, Toast.LENGTH_LONG).show();
                    }else{
                        speakMsg = "查询劵失败！";
                        ToastUtil.showText(activity,speakMsg,1);
                    }
                }

            }else{
                if(Utils.isNotEmpty(msg)){
                    speakMsg = msg;
                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                }else{

                    speakMsg = "查询劵失败！";
                    ToastUtil.showText(activity,speakMsg,1);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
            speakMsg = "查询劵失败！";
            ToastUtil.showText(activity,speakMsg,1);
        }catch (Exception e){
            e.printStackTrace();
            speakMsg = "查询劵失败！";
            ToastUtil.showText(activity,speakMsg,1);
        }
        if(Utils.isNotEmpty(speakMsg)){

            speak(speakMsg);
        }
    }

    private void cardVerificaJson(String jsonStr){
        String speakMsg  = "";
        try {
            JSONObject job = new JSONObject(jsonStr);
            String code = job.getString("code");
            String msg = job.getString("msg");
            if("000000".equals(code)){
                String subCode = job.getString("subCode");
                String subMsg = job.getString("subMsg");
                if("000000".equals(subCode)){
                    String dataJson = job.getString("data");
                    Gson gson  =  GsonUtils.getGson();
                    CardVerificaResData resData = gson.fromJson(dataJson, CardVerificaResData.class);
                    //打印核销小票
                    startPrint(resData);
                    speakMsg = "核销成功！";
                    ToastUtil.showText(activity,speakMsg,1);
                    finish();
                }else{
                    if(Utils.isNotEmpty(subMsg)){
                        speakMsg = subMsg;
                        Toast.makeText(activity, subMsg, Toast.LENGTH_LONG).show();
                    }else{
                        speakMsg = "核销失败！";
                        ToastUtil.showText(activity,speakMsg,1);
                    }
                }

            }else{
                if(Utils.isNotEmpty(msg)){
                    speakMsg = msg;
                    Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
                }else{
                    speakMsg = "核销失败！";
                    ToastUtil.showText(activity,speakMsg,1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            speakMsg = "核销失败！";
            ToastUtil.showText(activity,speakMsg,1);
        }catch (Exception e){
            speakMsg = "核销失败！";
            ToastUtil.showText(activity,speakMsg,1);
        }
        if(Utils.isNotEmpty(speakMsg)){

            speak(speakMsg);
        }

    }

    private void startPrint(final CardVerificaResData resData){

        //打印
//        USBPrinter.initPrinter(this);
        USBPrintTextUtil.cardVerificaText(userBean,resData);

       /* if ( DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
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
                    JiaBoPrintTextUtil.cardVerificaText(id,userBean,resData);
                } else {
                    mHandler.obtainMessage( PRINTER_COMMAND_ERROR ).sendToTarget();
                }
            }
        } );*/
    }

    private void submitCodeVerifica(){
        if(Utils.isFastClick()){
            return;
        }
        etCodeStr = etCode.getText().toString().trim();
        if(Utils.isEmpty(etCodeStr)){
            ToastUtil.showText(activity, "请输入核销劵码！", 1);
            speak("请输入核销劵码！");
            return;
        }
        queryCode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scanner_hint_imgBack:
                finish();
                break;
            case R.id.card_veridica_btOk:
                submitCodeVerifica();
                break;
        }
    }

   /* @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("event的值", "event= "+event);

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            *//*0键*//*
            Log.e(TAG,"按下键盘回退/删除键");

            finish();

        }
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            *//*0键*//*
            Log.e(TAG,"按下back键");
            finish();

        }
        return true;
    }*/
}
