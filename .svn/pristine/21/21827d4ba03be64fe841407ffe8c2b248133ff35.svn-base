package com.wanding.face.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanding.face.App;
import com.wanding.face.BaseActivity;
import com.wanding.face.Constants;
import com.wanding.face.MainActivity;
import com.wanding.face.R;
import com.wanding.face.bean.UserBean;
import com.wanding.face.jiabo.device.bean.PosScanpayResData;
import com.wanding.face.utils.DecimalUtil;
import com.wanding.face.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_pay_state)
public class PayStateActivity extends BaseActivity {

    @ViewInject(R.id.pay_state_imgPayState)
    ImageView imgPayState;
    @ViewInject(R.id.pay_state_tvPayState)
    TextView tvPayState;
    @ViewInject(R.id.pay_state_tvPayTotalFeeTitle)
    TextView tvPayTotalFeeTitle;
    @ViewInject(R.id.pay_state_tvPayTotalFee)
    TextView tvPayTotalFee;
    @ViewInject(R.id.pay_state_tvMeName)
    TextView tvMeName;
    @ViewInject(R.id.pay_state_btComplete)
    Button btComplete;

    private static final int TIME_COUNT = 5000;
    private String busType = "";
    private String state;
    private String totalFee = "0.00";
    private UserBean userBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        busType = intent.getStringExtra("busType");
        state = intent.getStringExtra("state");
        totalFee = intent.getStringExtra("totalFee");
        userBean = (UserBean) intent.getSerializableExtra("userBean");
        if(Utils.isNotEmpty(state)){
            if(state.equals(Constants.PAY_STATE_SUCCESS)){
                tvPayState.setText(R.string.pay_state_success);
            }else if(state.equals(Constants.PAY_STATE_FAIL)){
                imgPayState.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.fail_160_icon));
                tvPayState.setText(R.string.pay_state_fail);
                tvPayState.setTextColor(ContextCompat.getColor(activity,R.color.red_FF1200));
                tvPayTotalFeeTitle.setTextColor(ContextCompat.getColor(activity,R.color.red_FF1200));
                tvPayTotalFee.setTextColor(ContextCompat.getColor(activity,R.color.red_FF1200));
                tvMeName.setTextColor(ContextCompat.getColor(activity,R.color.red_FF1200));
            }else {
                imgPayState.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.unknown_160_icon));
                tvPayState.setText(R.string.pay_state_unknown);
                tvPayState.setTextColor(ContextCompat.getColor(activity,R.color.grey_c2c2c2));
                tvPayTotalFeeTitle.setTextColor(ContextCompat.getColor(activity,R.color.grey_c2c2c2));
                tvPayTotalFee.setTextColor(ContextCompat.getColor(activity,R.color.grey_c2c2c2));
                tvMeName.setTextColor(ContextCompat.getColor(activity,R.color.grey_c2c2c2));

            }
        }

        tvPayTotalFee.setText(totalFee);
        if(userBean!=null){
            tvMeName.setText(userBean.getMname());
        }

        setButton();

        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setButton(){
        CountDownTimer countDownTimer = new CountDownTimer(TIME_COUNT, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTick(long millisUntilFinished) {
                int remainTime = (int) (millisUntilFinished / 1000L);

                btComplete.setText(getResources().getString(R.string.complete) + "(" + remainTime + "s)");
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        countDownTimer.start();
    }
}
