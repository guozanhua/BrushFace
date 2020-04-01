package com.wanding.face;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.geekmaker.paykeyboard.PayKeyboard;
import com.geekmaker.paykeyboard.USBDetector;
import com.wanding.face.activity.AuthRecodeListActivity;
import com.wanding.face.activity.MemberManageActivity;
import com.wanding.face.activity.OrderListActivity;
import com.wanding.face.activity.SettingsActivity;
import com.wanding.face.activity.ShiftActivity;
import com.wanding.face.activity.SummaryActivity;
import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.usb.keyboar.USBKeyboard;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 *  功能菜单管理界面
 */
@ContentView(R.layout.activity_menu_manage)
public class MenuManageActivity extends BaseActivity implements View.OnClickListener,USBKeyboard.OnKeyboardValueListener {

    @ViewInject(R.id.transparent_title_imageView)
    ImageView imgBack;
    @ViewInject(R.id.transparent_title_layout)
    LinearLayout titleLayout;
    @ViewInject(R.id.transparent_title_tvTitle)
    TextView tvTitle;
    @ViewInject(R.id.transparent_title_imgTitleImg)
    ImageView imgTitleImg;
    @ViewInject(R.id.transparent_title_tvOption)
    TextView tvOption;

    @ViewInject(R.id.menu_manage_tvMname)
    TextView tvMname;
    @ViewInject(R.id.menu_manage_tvEname)
    TextView tvEname;

    @ViewInject(R.id.menu_manage_sign_layout)
    RelativeLayout signLayout;
    @ViewInject(R.id.menu_manage_orderList_layout)
    RelativeLayout orderListLayout;
    @ViewInject(R.id.menu_manage_shift_layout)
    RelativeLayout shiftLayout;
    @ViewInject(R.id.menu_manage_summary_layout)
    RelativeLayout summaryLayout;
    @ViewInject(R.id.menu_manage_authRecodeLayout)
    RelativeLayout authRecodeLayout;
    @ViewInject(R.id.menu_manage_member_layout)
    RelativeLayout memberLayout;
    @ViewInject(R.id.menu_manage_setting_layout)
    RelativeLayout systemLayout;



    private UserBean userBean;

    MySyntherizer synthesizer = MainActivity.synthesizer;
    USBKeyboard usbKeyboard = MainActivity.usbKeyboard;
    private boolean isKeyboardValidity = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("悦收银");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");
        initListener();

        if(userBean!=null){
            String mname = userBean.getMname();
            String ename = userBean.getEname();
            if(Utils.isNotEmpty(mname)){
                tvMname.setText(mname);
            }
            if(Utils.isNotEmpty(ename)){
                tvEname.setText(ename);
            }
        }

        speak("请点击屏幕开始设置");

    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if(usbKeyboard!=null){

            usbKeyboard.release();
            usbKeyboard = null;
        }
        Log.e(TAG,"onDestroy....");
    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        systemLayout.setOnClickListener(this);
        signLayout.setOnClickListener(this);
        orderListLayout.setOnClickListener(this);
        shiftLayout.setOnClickListener(this);
        summaryLayout.setOnClickListener(this);
        authRecodeLayout.setOnClickListener(this);
        memberLayout.setOnClickListener(this);

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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String errorJsonText = "";
            switch (msg.what){
                case NetworkUtils.MSG_WHAT_ONE:

                    break;
                case NetworkUtils.REQUEST_JSON_CODE:

                    break;
                case NetworkUtils.REQUEST_IO_CODE:

                    break;
                case NetworkUtils.REQUEST_CODE:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.transparent_title_imageView:
                finish();
                break;
            case R.id.menu_manage_sign_layout:
                intent.setClass(activity,LoginActivity.class);
                startActivity(intent);
                App.getInstance().exit();
                finish();
                break;
            case R.id.menu_manage_orderList_layout:
                intent.setClass(activity,OrderListActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
                break;
            case R.id.menu_manage_shift_layout:
                intent.setClass(activity,ShiftActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
                break;
            case R.id.menu_manage_summary_layout:
                intent.setClass(activity,SummaryActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
                break;
            case R.id.menu_manage_authRecodeLayout:
                intent.setClass(activity,AuthRecodeListActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
                break;
            case R.id.menu_manage_member_layout:
                intent.setClass(activity,MemberManageActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
                break;
            case R.id.menu_manage_setting_layout:
                intent.setClass(activity,SettingsActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    /*@Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            *//*0键*//*
            Log.e(TAG,"按下键盘回退/删除键");
            finish();
        }
        return true;
    }*/
}
