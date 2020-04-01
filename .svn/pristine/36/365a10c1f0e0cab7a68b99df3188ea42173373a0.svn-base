package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.ICheckListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.geekmaker.paykeyboard.PayKeyboard;
import com.geekmaker.paykeyboard.USBDetector;
import com.google.gson.Gson;
import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.MainActivity;
import com.wanding.face.MenuManageActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.SharedPreConstants;
import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.bean.CheckPasswdReqData;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.bean.WdPreAuthHistoryVO;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.payutil.QueryParamsReqUtil;
import com.wanding.face.usb.keyboar.USBKeyboard;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.MySerialize;
import com.wanding.face.utils.SharedPreferencesUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.USBkeyboardUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

/**
 * Time: 2019/11/27
 * Author:Administrator
 * Description: 输入操作员密码
 */
@ContentView(R.layout.activity_enter_passwd)
public class EnterPasswdActivity extends BaseActivity implements View.OnClickListener,USBKeyboard.OnKeyboardValueListener {

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


    @ViewInject(R.id.enter_passwd_etPasswd)
    EditText etPasswd;

    @ViewInject(R.id.enter_passwd_btOk)
    Button btOk;


    private UserBean userBean;
    private String action;
    private OrderDetailData order;//订单对象
    private WdPreAuthHistoryVO authOrder;

    MySyntherizer synthesizer = MainActivity.synthesizer;
    USBKeyboard usbKeyboard = MainActivity.usbKeyboard;
    private boolean isKeyboardValidity = true;
    /**
     * 金额拼接
     */
    public StringBuilder pending = new StringBuilder();
    /**
     * 本地配置文件保存标识判断是否输入管理密码
     */
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("管理员密码");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");
        action = intent.getStringExtra("action");
        order = (OrderDetailData) intent.getSerializableExtra("order");
        authOrder = (WdPreAuthHistoryVO) intent.getSerializableExtra("authOrder");

        sharedPreferencesUtil = new SharedPreferencesUtil(activity,SharedPreConstants.PERMISSIONS_FILE_NAME);
        initListener();

        //关闭系统键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(Constants.ACTION_PASSWD_FAST_REFUND.equals(action)||Constants.ACTION_PASSWD_DETAIL_REFUND.equals(action)){
            //如果是退款操作
            if(sharedPreferencesUtil.contain(SharedPreConstants.PERMISSIONS_REFUND_KEY_NAME)){
                boolean refundPermissions = (boolean) sharedPreferencesUtil.getSharedPreference(SharedPreConstants.PERMISSIONS_REFUND_KEY_NAME,false);
                if(refundPermissions){

                    speak("请输入管理员密码！");
                }else{

                    nextToAction();
                }
            }else{
                speak("请输入管理员密码！");
            }

        }else if(Constants.ACTION_PASSWD_AUTH_CANCEL.equals(action)||Constants.ACTION_PASSWD_AUTH_CONFIRM_CANCEL.equals(action)){
            //如果是预授权操作
            if(sharedPreferencesUtil.contain(SharedPreConstants.PERMISSIONS_AUTH_KEY_NAME)){
                boolean authPermissions = (boolean) sharedPreferencesUtil.getSharedPreference(SharedPreConstants.PERMISSIONS_AUTH_KEY_NAME,false);
                if(authPermissions){

                    speak("请输入管理员密码！");
                }else{

                    nextToAction();
                }
            }else{

                speak("请输入管理员密码！");
            }
        }

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
        Log.e(TAG,"onDestroy....");
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

    private void initListener(){
        imgBack.setOnClickListener(this);
        btOk.setOnClickListener(this);
        //初始化键盘
        usbKeyboard.getOnKeyboardValueListener(this);
    }

    /**
     * 定制键盘
     */
    @Override
    public void getKeyboardValue(int keyCode, String keyName, double money, String text) {
        Log.e(TAG,"接收的金额："+String.valueOf(money));
        Log.e(TAG,"屏幕更新的值："+text);
        if(isKeyboardValidity) {
            if (keyCode == -1 && "ESC".equals(keyName)) {
                //初始化显示
                etPasswd.setText("");
            } else if (USBKeyboard.KEY_CODE_ESC == keyCode && USBKeyboard.KEY_NAME_ESC.equals(keyName)) {
                //点击取消键显示
                speak("取消");
                if(usbKeyboard!=null){
                    usbKeyboard.release();
                    usbKeyboard = null;
                }
                finish();


            } else if (USBKeyboard.KEY_CODE_BACK == keyCode && USBKeyboard.KEY_NAME_BACK.equals(keyName)) {
                //点击退格键显示
                if ("0".equals(text)) {

                    etPasswd.setText("");
                } else {
                    etPasswd.setText(text);
                }
            } else if(USBKeyboard.KEY_CODE_PAY==keyCode&&USBKeyboard.KEY_NAME_PAY.equals(keyName)){
                //点击支付键
                if(Utils.isFastClick()){
                    return;
                }
                try{
                    Double dou_passwd = money;
                    int int_passwd = dou_passwd.intValue();
                    String valueStr = String.valueOf(int_passwd);
                    checkPasswd(valueStr);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                etPasswd.setText(text);
                etPasswd.setSelection(etPasswd.getText().toString().length());
            }
        }
    }

    /**
     * 青蛙自带键盘
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("event的值", "event= "+event);
        String value = USBkeyboardUtil.getKeyValue(event,pending);
        if(Utils.isEmpty(value)){
            etPasswd.setText("");
        }else{
            etPasswd.setText(value);
            etPasswd.setSelection(etPasswd.getText().toString().length());
        }
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            Log.e(TAG,"按下键盘回退/删除键");
            if (pending.length() != 0) {
                pending = pending.delete(pending.length() - 1, pending.length());
                etPasswd.setText("￥"+pending);
                if("0".equals(pending.toString())){
                    //清空pending
                    pending.delete( 0, pending.length() );
                }
                if(pending.length()<=0){
                    etPasswd.setText("");
                }
            }
            etPasswd.setSelection(etPasswd.getText().toString().length());

        }
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ADD){
            Log.e(TAG,"按下+号键，定义为取消操作");
            speak("取消");
            finish();

        }
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if(event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER||event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                Log.e(TAG,"按下回车键");

                upEnterAction();
            }
        }
        /**
         * 这里直接return true，防止事件向上传递，改变最终输入结果
         */
//        return super.dispatchKeyEvent(event);
        return true;
    }

    /**
     * 验证密码是否正确
     */
    private void checkPasswd(String passwd){
        showCustomDialog();
        final String url = NitConfig.checkPasswdUrl;
        Log.e(TAG,"请求地址："+url);
        final CheckPasswdReqData reqData = QueryParamsReqUtil.checkPasswdReq(passwd);
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = FastJsonUtil.toJSONString(reqData);
                    Log.e("发起请求参数：", content);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);
                    Log.e("返回字符串结果：", jsonStr);
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


    private void sendMessage(int what,String text){
        Message msg = new Message();
        msg.what = what;
        msg.obj = text;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String errorJsonText = "";
            switch (msg.what){
                case NetworkUtils.MSG_WHAT_ONE:
                    String checkPasswdJson=(String) msg.obj;
                    checkPasswdJson(checkPasswdJson);
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
                    break;
            }
        }
    };

    private void checkPasswdJson(String json){
        String msg = "";
        try {
            JSONObject job = new JSONObject(json);
            String status = job.getString("status");
            String message = job.getString("message");
            if(status.equals("200")){

                nextToAction();
            }else{
                if(Utils.isNotEmpty(message)){
                    ToastUtil.showText(activity,message,1);
                    msg = message;
                }else{
                    ToastUtil.showText(activity,"密码错误",1);
                    msg = "密码错误";
                }

                speak(msg);
            }



        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.showText(activity,"密码校验接口返回错误",1);
            msg = "密码校验接口返回错误";
            speak(msg);

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showText(activity,"密码校验接口返回错误",1);
            msg = "密码校验接口返回错误";
            speak(msg);
        }

    }

    private void nextToAction(){
        Intent intent = new Intent();
        if(Utils.isNotEmpty(action)){
            if(Constants.ACTION_PASSWD_MODIFY_PASSWD.equals(action)){
                intent.setClass(activity,ModifyPasswdActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
            }else  if(Constants.ACTION_PASSWD_FAST_REFUND.equals(action)){
                intent.setClass(activity,ScanQrcodeActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
            }else  if(Constants.ACTION_PASSWD_DETAIL_REFUND.equals(action)){
                intent.setClass(activity,RefundActivity.class);
                intent.putExtra("userBean",userBean);
                intent.putExtra("order",order);
                startActivity(intent);
            }else if(Constants.ACTION_PASSWD_AUTH_CANCEL.equals(action)){

                String authAction = "2";
                intent.setClass(activity,AuthConfirmActivity.class);
                intent.putExtra("userBean",userBean);
                intent.putExtra("authOrder",authOrder);
                intent.putExtra("authAction",authAction);
                startActivity(intent);

            }else if(Constants.ACTION_PASSWD_AUTH_CONFIRM_CANCEL.equals(action)){
                String authAction = "4";
                intent.setClass(activity,AuthConfirmActivity.class);
                intent.putExtra("userBean",userBean);
                intent.putExtra("authOrder",authOrder);
                intent.putExtra("authAction",authAction);
                startActivity(intent);
            }else if(Constants.ACTION_PASSWD_PERMISSIONS.equals(action)){
                intent.setClass(activity,PermissionsSettingActivity.class);
                startActivity(intent);
            }

        }else{
            ToastUtil.showText(activity,"操作出错！",1);
        }

        finish();

    }

    /**
     * 点击确定键
     */
    private void upEnterAction(){
        if(Utils.isFastClick()){
            return;
        }
        String etPasswdStr = etPasswd.getText().toString().trim();
        if(Utils.isEmpty(etPasswdStr)){
            String msg = "请输入管理员密码！";
            speak(msg);
            ToastUtil.showText(activity,msg,1);
            return;
        }
        checkPasswd(etPasswdStr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.enter_passwd_btOk:
                upEnterAction();
                break;
                default:
                    break;
        }
    }



}
