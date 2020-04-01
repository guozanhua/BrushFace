package com.wanding.face;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.wanding.face.baidu.tts.MySyntherizer;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.FlashHelper;
import com.wanding.face.utils.ToastUtil;
import com.wanding.face.utils.USBkeyboardUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 *  输入金额界面
 */
@ContentView(R.layout.activity_input_amount)
public class InputAmountActivity extends MainBaseActivity implements View.OnClickListener {

    @ViewInject(R.id.input_amount_btConfirm)
    Button btConfirm;

    MySyntherizer synthesizer = MainActivity.synthesizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //清空StringBuilder，EditText恢复初始值
        //清空EditText
        pending.delete( 0, pending.length() );
        if(pending.length()<=0){
            etSumMoney.setText("￥0.00");
        }
        Log.e(TAG,"onResume()方法被调用....");

    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止闪烁
        FlashHelper.getInstance().stopFlick(btConfirm);
    }

    private void initListener(){
        btConfirm.setOnClickListener(this);
    }




    /**
     * 发起支付第一步
     */
    private void payMethodOne(String totalFee){
        //开启闪烁
        FlashHelper.getInstance().startFlick(btConfirm);
        String totalFeeStr = "";
        try {
            Log.e("输入框金额值：", totalFee);
            if(Utils.isEmpty(totalFee)){
                ToastUtil.showText(activity,"请输入有效金额！",1);
                //停止闪烁
                FlashHelper.getInstance().stopFlick(btConfirm);
                speak("请输入正确的支付金额！");
                return;
            }
            totalFeeStr =  DecimalUtil.StringToPrice(totalFee);
            Log.e("金额值转换后：", totalFeeStr);
            //金额是否合法
            int isCorrect = DecimalUtil.isEqual(totalFeeStr);
            if(isCorrect != 1){
                ToastUtil.showText(activity,"请输入有效金额！",1);
                //停止闪烁
                FlashHelper.getInstance().stopFlick(btConfirm);
                speak("请输入正确的支付金额！");
                return;
            }
//            speak("支付"+totalFeeStr+"元，请点击刷脸支付！");
            payMethodTwo(totalFeeStr);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    private void payMethodTwo(String totalFee){
        Intent intent = new Intent();
        intent.setClass(activity,BrushFaceActivity.class);
        intent.putExtra("totalFee",totalFee);
        startActivity(intent);
        finish();
        //清空pending的值

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

    private void checkResult(int result, String method) {
        if (result != 0) {
//            toPrint("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
            Log.e("error code :", result+" method:" + method );
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.input_amount_btConfirm:
                if(Utils.isFastClick()){
                    return;
                }
                String totalFee = pending.toString();
                payMethodOne(totalFee);
                break;
                default:
                    break;
        }
    }



    /**
     * 监听外接设备（扫码枪，键盘）
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.e("event的值", "event= "+event);
        String value = USBkeyboardUtil.getKeyValue(event,pending);

        Log.e("value的值", value);
        if(Utils.isEmpty(value)){
            etSumMoney.setText("￥0.00");
        }else{
            etSumMoney.setText("￥"+value);
        }

        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER){
            /*回车键*/
            String valueStr = pending.toString();
            Log.e(TAG,"输入金额"+valueStr);
            payMethodOne(valueStr);

        }
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL){
            /*0键*/
            Log.e(TAG,"按下键盘回退/删除键");
            if (pending.length() != 0) {
                pending = pending.delete(pending.length() - 1, pending.length());
                etSumMoney.setText("￥"+pending);
                if("0".equals(pending.toString())){
                    //清空pending
                    pending.delete( 0, pending.length() );
                }
                if(pending.length()<=0){
                    etSumMoney.setText("￥0.00");
                }
            }
            finish();


        }
        if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            /*0键*/
            Log.e(TAG,"按下back键");
            finish();

        }

        /**
         * 这里直接return true，防止事件向上传递，改变最终输入结果
         */
//        return super.dispatchKeyEvent(event);
        return true;

    }

}
