package com.wanding.face.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geekmaker.paykeyboard.DefaultKeyboardListener;
import com.geekmaker.paykeyboard.ICheckListener;
import com.geekmaker.paykeyboard.IPayRequest;
import com.geekmaker.paykeyboard.PayKeyboard;
import com.geekmaker.paykeyboard.USBDetector;
import com.google.gson.Gson;
import com.wanding.face.BaseActivity;
import com.wanding.face.MainActivity;
import com.wanding.face.NitConfig;
import com.wanding.face.R;
import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.bean.OrderDetailData;
import com.wanding.face.bean.RefundResData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.httputil.HttpURLConnectionUtil;
import com.wanding.face.httputil.NetworkUtils;
import com.wanding.face.print.USBPrintTextUtil;
import com.wanding.face.usb.keyboar.USBKeyboard;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.EditTextUtils;
import com.wanding.face.utils.GsonUtils;
import com.wanding.face.utils.TimeCountUtil;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

/**
 * 退款界面
 */
@ContentView(R.layout.activity_refund)
public class RefundActivity extends BaseActivity implements View.OnClickListener,USBKeyboard.OnKeyboardValueListener {

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

    @ViewInject(R.id.refund_activity_etPrice)
    EditText etMoney;

    @ViewInject(R.id.refund_activity_layoutGetVerCode)
    RelativeLayout layoutGetVerCode;
    @ViewInject(R.id.refund_activity_etVerCode)
    EditText etVerCode;


    @ViewInject(R.id.refund_activity_btGetVerCode)
    Button btGetVerCode;//获取验证码
    @ViewInject(R.id.refund_activity_btOk)
    Button btSubmit;//退款按钮

    private UserBean userBean;
    private OrderDetailData order;//订单对象
    private TimeCountUtil timeCountUtil;

    String etVerCodeStr;
    String etPriceStr;

    /**
     * 百度语音合成
     */
    MySyntherizer synthesizer = MainActivity.synthesizer;
    USBKeyboard usbKeyboard = MainActivity.usbKeyboard;
    private boolean isKeyboardValidity = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgBack.setVisibility(View.VISIBLE);
        imgBack.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.back_icon));
        tvTitle.setText("退款");
        imgTitleImg.setVisibility(View.GONE);
        tvOption.setVisibility(View.GONE);
        tvOption.setText("");

        Intent intent = getIntent();
        userBean = (UserBean) intent.getSerializableExtra("userBean");
        order = (OrderDetailData) intent.getSerializableExtra("order");


        initView();
        initListener();
    }


    @Override
    protected void onStart() {
        super.onStart();

        speak("请输入退款金额！");
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

    private void initView(){
        EditTextUtils.setPricePoint(etMoney);
        if(userBean.isRmvVerCodePri()){
            layoutGetVerCode.setVisibility(View.GONE);
        }else{
            layoutGetVerCode.setVisibility(View.VISIBLE);
        }
    }

    private void initListener(){
        imgBack.setOnClickListener(this);
        btGetVerCode.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
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
     * 定制键盘
     */
    @Override
    public void getKeyboardValue(int keyCode, String keyName, double money, String text) {
        Log.e(TAG,"接收的金额："+String.valueOf(money));
        Log.e(TAG,"屏幕更新的值："+text);
        if(isKeyboardValidity) {
            if (keyCode == -1 && "ESC".equals(keyName)) {
                //初始化显示
                etMoney.setText("");
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
                if("pay---".equals(text)||"success".equals(text)||"0".equals(text)){
                    etMoney.setText("");
                }else{
                    etMoney.setText(text);
                    etMoney.setSelection(etMoney.getText().toString().length());
                }
                etMoney.setSelection(etMoney.getText().toString().length());
            } else if(USBKeyboard.KEY_CODE_PAY==keyCode&&USBKeyboard.KEY_NAME_PAY.equals(keyName)){
                //点击支付键
                if(Utils.isFastClick()){
                    return;
                }
                try{
                    etMoney.setText(String.valueOf(money));
                    refundOne();
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                if("pay---".equals(text)||"success".equals(text)){
                    etMoney.setText("");
                }else{
                    etMoney.setText(text);
                    etMoney.setSelection(etMoney.getText().toString().length());
                }

            }
        }
    }

    /**
     * 获取验证码
     */
    private void getVerCode(){
        showCustomDialog();
        final String url = NitConfig.getVerCodeUrl;
        new Thread(){
            @Override
            public void run() {
                try {
                    // 拼装JSON数据，向服务端发起请求
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("orderId", order.getOrderId());
                    userJSON.put("sid", userBean.getSid());
                    userJSON.put("mid", userBean.getMid());
                    String content = String.valueOf(userJSON);
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

    /**
     * 发起退款
     */
    private void refundTwo(){
        showCustomDialog();
        final String url = NitConfig.refundRequestUrl;
        new Thread(){
            @Override
            public void run() {
                try {
                    // 拼装JSON数据，向服务端发起请求
                    JSONObject userJSON = new JSONObject();
                    userJSON.put("mid", userBean.getMid());
                    userJSON.put("orderId", order.getOrderId());
                    userJSON.put("amount", etPriceStr);
                    userJSON.put("verCode", etVerCodeStr);
                    userJSON.put("isRmvVerCodePri", userBean.isRmvVerCodePri());
                    userJSON.put("desc", "");
                    String content = String.valueOf(userJSON);
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
                    String getVerCodeJson=(String) msg.obj;
                    getVerCodeJson(getVerCodeJson);
                    hideCustomDialog();
                    break;
                case NetworkUtils.MSG_WHAT_TWO:
                    String refundResJson = (String) msg.obj;
                    refundResJson(refundResJson);
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

    private void getVerCodeJson(String jsonStr){
        //{"data":null,"message":"发送成功！","status":200}
        try {
            JSONObject job = new JSONObject(jsonStr);
            String status = job.getString("status");
            String message = job.getString("message");
            if(status.equals("200")){
                ToastUtil.showText(activity,"验证码发送成功！",1);

            }else{
                timeCountUtil.cancel();
                timeCountUtil.onFinish();
                ToastUtil.showText(activity,message,1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refundResJson(String jsonStr){
        String msg = "";
        try {
            JSONObject job = new JSONObject(jsonStr);
            String status = job.getString("status");
            String message = job.getString("message");
            String dataJsonStr = job.getString("data");
            if(status.equals("200")){
                Gson gjson  =  GsonUtils.getGson();
                RefundResData refundResData = gjson.fromJson(dataJsonStr, RefundResData.class);
                startPrint(refundResData);
                msg = "退款成功"+DecimalUtil.branchToElement(refundResData.getRefund_fee())+"元";
                finish();

            }else{
                if(Utils.isNotEmpty(message)){
                    msg = message;
                    ToastUtil.showText(activity,message,1);
                }else{
                    msg = "退款失败！";
                    ToastUtil.showText(activity,"退款失败！",1);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
            msg = "退款失败！";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "退款失败！";
        }
        speak(msg);
    }

    private void refundOne(){
        etVerCodeStr = etVerCode.getText().toString().trim();
        etPriceStr = etMoney.getText().toString().trim();

        if(Utils.isEmpty(etPriceStr)){
//            speak("请输入退款金额");
//            etMoney.requestFocus();
            ToastUtil.showText(activity,"请输入退款金额！",1);
            return;
        }

        if(userBean.isRmvVerCodePri()){

            etVerCodeStr = "";
        }else{
            if(Utils.isEmpty(etVerCodeStr)){
//                etVerCode.requestFocus();
                speak("请输入退款验证码");
                ToastUtil.showText(activity,"请输入退款验证码！",1);
                return;
            }
        }



//        if(Utils.isNotEmpty(etPriceStr)){
//            etPrice = DecimalUtil.StringToPrice(etPriceStr);
//            double dou_etPrice = Double.parseDouble(etPrice);
//
//            String ordrPriceStr = order.getGoodsPrice();
//            double dou_price = Double.parseDouble(ordrPriceStr);
//            if(dou_etPrice>dou_price){
//                ToastUtil.showText(activity,"退款金额不能大于支付金额！",1);
//                return;
//            }
//        }
        if(timeCountUtil!=null){
            timeCountUtil.cancel();
            timeCountUtil.onFinish();
        }
        refundTwo();
        stop();
    }

    private void startPrint(final RefundResData refundResData){
        //打印
//        USBPrinter.initPrinter(this);
        USBPrintTextUtil.refundText(userBean,refundResData);

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
                    JiaBoPrintTextUtil.refundText(id,userBean,refundResData);
                } else {
                    mHandler.obtainMessage( PRINTER_COMMAND_ERROR ).sendToTarget();
                }
            }
        } );*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_title_imageView:
                finish();
                break;
            case R.id.refund_activity_btGetVerCode:
                //防止连续点击
                if(Utils.isFastClick()){
                    return;
                }
                timeCountUtil = new TimeCountUtil(60000, 1000, RefundActivity.this, btGetVerCode);
                timeCountUtil.start();
                getVerCode();
                break;
            case R.id.refund_activity_btOk:
                if(Utils.isFastClick()){
                    return;
                }
                refundOne();
                break;
                default:
                    break;
        }
    }



}
