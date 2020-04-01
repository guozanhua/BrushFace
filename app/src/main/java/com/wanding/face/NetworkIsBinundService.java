package com.wanding.face;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Time: 2020/3/26
 * Author:Administrator
 * Description:判断网络是否已连接
 */
public class NetworkIsBinundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
