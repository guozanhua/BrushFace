package com.wanding.face;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wanding.face.utils.SharedPreferencesUtil;

/**
 * 开机自启动广播
 */
public class MyReceiver extends BroadcastReceiver {


    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent i = new Intent(context, WelComeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }




}
