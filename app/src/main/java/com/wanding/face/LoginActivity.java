package com.wanding.face;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wanding.face.activity.BannerImageActivity;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.MD5;
import com.wanding.face.utils.MySerialize;
import com.wanding.face.utils.SharedPreferencesUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;
import com.wanding.face.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener{

    private static final int SETTINGS_REQEST_CODE = 0x001;

    private ClearEditText etAccount;
    private ClearEditText etPasswd;
    private Button btLogin;
    private TextView tvVersion;


    String accountStr = "";
    String passwdStr = "";
    private SharedPreferencesUtil sharedPreferencesUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferencesUtil = new SharedPreferencesUtil(activity,"userInfo");
        mToolbar = null;
        initData();

    }



    /**
     * 初始化应用信息
     */
    private void initData(){



        if(sharedPreferencesUtil.contain("account")){
            if(!NetworkUtils.isNetworkAvailable(this)){
                //设置网络
                showNetworkSettingDialog();
                return;
            }
            Log.e("登录保存的状态key：", "Key存在");
            accountStr = (String) sharedPreferencesUtil.getSharedPreference("account","");
            passwdStr = (String) sharedPreferencesUtil.getSharedPreference("passwd","");
            doLogin(accountStr,passwdStr);
        }else{
            Log.e("登录保存的状态key：", "Key不存在");
            setContentView(R.layout.activity_login);
            initView();
            initListener();
        }
    }

    /** 初始化View,控件 */
    private void initView(){
        etAccount = (ClearEditText) findViewById(R.id.login_activity_etAccount);
        etPasswd = (ClearEditText) findViewById(R.id.login_activity_etPasswd);
        btLogin = (Button) findViewById(R.id.login_activity_btSubmit);
        tvVersion = (TextView) findViewById(R.id.login_activity_tvVersion);
        String versionStr = "";
        try {
            versionStr = Utils.getVersionName(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(NitConfig.isFormal){
            tvVersion.setText("version-release-"+versionStr);
        }else{
            tvVersion.setText("version-debug-"+versionStr);
        }


    }

    /** 注册监听  */
    private void initListener(){
        btLogin.setOnClickListener(this);
    }

    //尝试登录
    private void attemptLogin() {
        //记录焦点
        boolean cancel = false;
        View focusView = null;

        accountStr = etAccount.getText().toString();
        passwdStr = etPasswd.getText().toString();
        if(Utils.isEmpty(accountStr)){
            ToastUtil.showText(activity,"用户名不能为空！",1);
            return;
        }
        if(Utils.isEmpty(passwdStr)){
            ToastUtil.showText(activity,"密码不能为空！",1);
            return;
        }
        //都不为空的情况下判断用户名密码是否正确（格式是否正确，比如用户名为手机号时手机号是否为11位等）
        //这里直接提交服务器验证
        doLogin(accountStr,passwdStr);


        if (!TextUtils.isEmpty(passwdStr) && !isPasswordValid(passwdStr)) {
//            etPasswd.setError(getString(R.string.error_invalid_password));
            focusView = etPasswd;
            cancel = true;
        }
        if (TextUtils.isEmpty(accountStr)) {
//            etAccount.setError(getString(R.string.error_field_required));
            focusView = etAccount;
            cancel = true;
        } else if (!isEmailValid(accountStr)) {
//            etAccount.setError(getString(R.string.error_invalid_email));
            focusView = etAccount;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();
        } else {


        }
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    /** 登录（提交到服务器验证）
     *  account和password参数
     *  登录账号100127110112密码123456
     *  登录账号100053410512密码123456
     */
    private void doLogin(final String account,String passwd){

        showCustomDialog("参数初始化...");
        final String url = NitConfig.doLoginUrl;
        Log.e("doLogin请求地址：",url);
        //对参数密码值经过MD5加密再传值（加密方式：MD5.MD5Encode(密码+账号)）
//        final String accountStr = "1000145101";
//        final String passwdStr = "123456";
        final String MD5PasswdStr = MD5.MD5Encode(passwd+account);
        new Thread(){
            @Override
            public void run() {
                try {
                    // 拼装JSON数据，向服务端发起请求
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("account",account);
                    userJSON.put("password",MD5PasswdStr);
                    userJSON.put("type",Constants.DEVICE_TYPE);
                    userJSON.put("mCode",Constants.SERIAL_NUM);
                    String content = String.valueOf(userJSON);
                    Log.e("发起请求参数：", content);
                    String content_type = HttpURLConnectionUtil.CONTENT_TYPE_JSON;
                    String jsonStr = HttpURLConnectionUtil.doPos(url,content,content_type);

                    Log.e("返回字符串结果：", jsonStr);
                    int msg = NetworkUtils.MSG_WHAT_ONE;
                    String text = jsonStr;
                    sendMessage(msg,text);
                }catch (SocketTimeoutException e){

                }catch (IOException e){
                    e.printStackTrace();
                    sendMessage(NetworkUtils.REQUEST_IO_CODE,NetworkUtils.REQUEST_IO_TEXT);
                }catch (Exception e) {
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
                    String loginJson = (String) msg.obj;
                    loginResultJSON(loginJson);
                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_JSON_CODE:
                    errorJsonText = (String) msg.obj;
                    //清除本地账号信息：
                    sharedPreferencesUtil.clear();
                    initData();
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_IO_CODE:
                    errorJsonText = (String) msg.obj;
                    //清除本地账号信息：
                    sharedPreferencesUtil.clear();
                    initData();
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    break;
                case NetworkUtils.REQUEST_CODE:
                    errorJsonText = (String) msg.obj;
                    //清除本地账号信息：
                    sharedPreferencesUtil.clear();
                    initData();
                    ToastUtil.showText(activity,errorJsonText,1);
                    hideCustomDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void loginResultJSON(String json){

        //{"data":null,"message":"登陆失败！请输入正确账号和密码！","status":300}
        //{"data":{"isQueryCoupons":false,"merchant_no":"1001302","terminal_id":"12968","accessToken":"mq8wcqlv14zup57ag1178cleyr1l5kcn","mid":3047,"eid":2160,"ename":"青蛙款台0827"},"message":"查询成功","status":200}
        // "timestamp":"1539939271572"}
        try {
            JSONObject job = new JSONObject(json);
            String status = job.getString("status");
            String message = job.getString("message");
            if(status.equals("200")){
                String dataJson = job.getString("data");
                //保存用户名和密码
                sharedPreferencesUtil.put("account",accountStr);
                sharedPreferencesUtil.put("passwd",passwdStr);
                Gson gson = GsonUtils.getGson();
                UserBean userBean = gson.fromJson(dataJson,UserBean.class);

                try {
                    MySerialize.saveObject("user",this,MySerialize.serialize(userBean));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//                userBean.setMerchant_no("1001306");
//                userBean.setTerminal_id("12974");
//                userBean.setAccessToken("1crptiarhwk02ugsv16rafwuhsfltom2");
//                userBean.setMname("生产免充值优惠券测试 ");
//                userBean.setMid("3648");
//                userBean.setEid("2166");
//                userBean.setSid("5206");
//                userBean.setEname("");

//                userBean.setMerchant_no("1001273");
//                userBean.setTerminal_id("12949");
//                userBean.setAccessToken("zqbssmpyknap2jvq37j24zv47nzydh7i");
//                userBean.setMname(" ");
//                userBean.setMid("");
//                userBean.setEid("");
//                userBean.setSid("");
//                userBean.setEname("");


//                userBean.setMerchant_no("1001271");
//                userBean.setTerminal_id("12969");
//                userBean.setAccessToken("mae9sti8inuis3dhi724qb6tgl1b8yjc");
//                userBean.setMname(" ");
//                userBean.setMid("");
//                userBean.setEid("");
//                userBean.setSid("");
//                userBean.setEname("");

                //富友预授权生产商户
//                userBean.setMerchant_no("1001067");
//                userBean.setTerminal_id("12356");
//                userBean.setAccessToken("ub93h1do1rlkizdh9mbd19qp5x1jty2w");
//                userBean.setMname(" ");
//                userBean.setMid("");
//                userBean.setEid("");
//                userBean.setSid("");
//                userBean.setEname("");


//                userBean.setMerchant_no("1001308");
//                userBean.setTerminal_id("12998");
//                userBean.setAccessToken("zofz1izshkjjfmd207tim4g50jcpus29");
//                userBean.setMname(" ");
//                userBean.setMid("");
//                userBean.setEid("");
//                userBean.setSid("");
//                userBean.setEname("");

//                userBean.setMerchant_no("1001316");
//                userBean.setTerminal_id("12989");
//                userBean.setAccessToken("m9no5jotwuyj8az0v3450bbhltexiyoy");
//                userBean.setMname(" ");
//                userBean.setMid("");
//                userBean.setEid("");
//                userBean.setSid("");
//                userBean.setEname("");


                intoMainActivity(userBean);
            }else{
                if(Utils.isNotEmpty(message)){
                    ToastUtil.showText(activity,message,1);
                }else{
                    ToastUtil.showText(activity,"登录失败！",1);
                }
                //清除本地账号信息：
                sharedPreferencesUtil.clear();
                initData();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            snackError("登录异常！");
            //清除本地账号信息：
            sharedPreferencesUtil.clear();
            initData();
        }
    }


    private void showNetworkSettingDialog(){
        View view = LayoutInflater.from(activity).inflate(R.layout.network_settings_hint_dialog, null);
        TextView btok = (TextView) view.findViewById(R.id.network_settings_hint_dialog_tvOk);
        final Dialog myDialog = new Dialog(activity,R.style.dialog);
        Window dialogWindow = myDialog.getWindow();
        WindowManager.LayoutParams params = myDialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(params);
        myDialog.setContentView(view);
        btok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_SETTINGS);
                startActivityForResult(intent,SETTINGS_REQEST_CODE);



                myDialog.dismiss();

            }
        });

        myDialog.show();
        myDialog.setCancelable(false);
    };



    private void intoMainActivity(UserBean userBean){
        Intent in = new Intent();
        in.setClass(LoginActivity.this,BannerImageActivity.class);
        in.putExtra("userBean",userBean);
        in.putExtra("action",Constants.ACTION_LOGIN_TO_BANNER);
        startActivity(in);
        //跳转动画效果
        overridePendingTransition(R.anim.in_from, R.anim.to_out);
        this.finish();
        Log.e(TAG,"关闭登录界面!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_REQEST_CODE){
            if(!NetworkUtils.isNetworkAvailable(this)){
                showNetworkSettingDialog();
                return;
            }else{
                initData();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_activity_btSubmit://提交、登录
                if(Utils.isFastClick()){
                    return;
                }
                if(!NetworkUtils.isNetworkAvailable(this)){
                    //设置网络
                    showNetworkSettingDialog();
                    return;
                }
                attemptLogin();
                break;
        }
    }
}

