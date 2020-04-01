package com.wanding.face.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * APP更新http工具类
 */
public class HttpURLConUtil {

    public static final int UPDATE_HINT = 0x1001;
    public static final int TO_INSTALL = 0x1002;
    public static final int NO_UPDATE_HINT = 0x1003;
    /**
     * 检测版本失败
     */
    public static final int TEST_VERSION_FAIL = 0x404;
    public static final String TEST_VERSION_FAIL_MSG = "检测版本失败！";
    /**
     * 下载失败
     */
    public static final int DOWNLOAD_FAIL = 0x504;
    public static final String DOWNLOAD_FAIL_MSG = "下载失败！";



    public static UpdateInfo getUpdateInfo(String path){
        //包装成url的对象
        try {
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            InputStream is =conn.getInputStream();
            UpdateInfo info = XmlUtils.getUpdateInfo(is);
            return info;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
