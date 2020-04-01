package com.wanding.face;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wanding.face.utils.SharedPreferencesUtil;

/**
 * 欢迎界面
 */
public class WelComeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(activity, Constants.DEVICE_FILE_NAME);
                sharedPreferencesUtil.put(Constants.DEVICE_STATE_KEY,Constants.DEVICE_STATE_DISCONNET);
                sharedPreferencesUtil.put(Constants.DEVICE_TYPE_KEY,"");
                startActivity(new Intent(WelComeActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
