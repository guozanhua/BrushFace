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
public class CofyRefundActivity extends BaseActivity implements View.OnClickListener,ICheckListener {

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
    protected void onResume() {
        super.onResume();
        //初始化定制键盘
        etMoney.setText("");
        onCreateIndex = 0;
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
                                if(onCreateIndex > 0&&!"0".equals(text)){
                                    if(!"pay---".equals(text)&&!"success".equals(text)){

                                        etMoney.setText(text);
                                    }else{
                                        if(onCreateIndex > 0&&!"0".equals(text)&&"pay---".equals(text)){
                                            speak("请重新输入金额！");
                                        }
//                                        keyboard.reset();
                                    }
                                    keyboardIndex = 0;

                                }else{
                                    etMoney.setText("");
                                }
                                etMoney.setSelection(etMoney.getText().toString().length());
                                Log.e(TAG,String.format("lastupdate  : %s \n ",text));
                            }
                        });
                        Log.i(TAG,"KeyboardUI:"+String.format("display update %s",text));
                    }

                    @Override
                    public void onAvailable() {
                        super.onAvailable();
                        if(keyboard==null){
//                            openKeyboard();
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
                                //键盘支付结果回调
                                request.setResult(true);
                                if(Utils.isFastClick()){
                                    return;
                                }
                                refundOne();
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
            Log.e("KeyboardUI","keyboard exists!!!");
        }

        speak("请输入退款金额！");
    }

    private void toActivity(int keyCode,String keyName){
        if(Utils.isFastClick()){
            return;
        }
        if(keyCode == 21 &&keyName.equals("ESC")){
            speak("取消");
            activity.finish();
        }else if(keyCode == 23 &&keyName.equals("PAY")){
            if(keyboard!=null){

                keyboard.reset();
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
                msg = "退款成功！";
                Gson gjson  =  GsonUtils.getGson();
                RefundResData refundResData = gjson.fromJson(dataJsonStr, RefundResData.class);
                startPrint(refundResData);
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

        if(keyboard!=null){

            keyboard.reset();
        }
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
                timeCountUtil = new TimeCountUtil(60000, 1000, CofyRefundActivity.this, btGetVerCode);
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

    @Override
    public void onAttach() {
        openKeyboard();
    }
}
