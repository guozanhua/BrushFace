package com.wanding.face.jiabo.device;

import android.util.Log;

import com.wanding.face.jiabo.device.bean.QueryDevListReqData;
import com.wanding.face.jiabo.device.bean.SendMsgReqData;
import com.wanding.face.print.JiaBoPrintTextUtil;
import com.wanding.face.utils.DateTimeUtil;
import com.wanding.face.utils.MD5;

/**
 * 请求参数帮助类
 */
public class RequestUtil {

    /**
     *  API密钥
     */
    private static final String apiKey = "YKAJT588";
    /**
     * 商户编号
     */
    private static final String memberCode = "0d9a4b5e2e5c42ef9e6a56b7f2bd22cd";
    /**
     * 终端编号
     */
    private static final String deviceNo = "00391282538428370";

    public static String queryDevListReq(){
        QueryDevListReqData data = new QueryDevListReqData();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        String time = simpleDateFormat.format(new Date(System.currentTimeMillis()));
//        Date date = simpleDateFormat.parse(time);
//        long ts = date.getTime();
//        String timeSsss = String.valueOf(ts);
//        Log.e("时间戳",timeSsss);
        String reqTimeStr = Long.toString(System.currentTimeMillis());
        Log.e("时间戳",reqTimeStr);
        data.setReqTime(reqTimeStr);
        data.setMemberCode(memberCode);
        data.setDeviceID("");

        String securityCodeStr = data.getMemberCode()+data.getReqTime()+apiKey;
        Log.e("参数拼接",securityCodeStr);
        String securityCodeMD5Str = MD5.MD5Encode(securityCodeStr);
        data.setSecurityCode(securityCodeMD5Str);



        return data.reqContent();
    }

    public static String sendMsgReq(){
        SendMsgReqData data = new SendMsgReqData();

        String reqTimeStr = Long.toString(System.currentTimeMillis());
        Log.e("时间戳",reqTimeStr);
        data.setReqTime(reqTimeStr);
        data.setMemberCode(memberCode);
        data.setDeviceNo(deviceNo);
        data.setMode("2");
        data.setMsgDetail(JiaBoPrintTextUtil.wifiSendMsgDetail());
        data.setCharset("1");
        data.setMsgNo(DateTimeUtil.getSystemTime());
        data.setReprint(1);
        data.setTimes(1);
        data.setVoice("");
        data.setMulti(0);

        String securityCodeStr = data.getMemberCode()+data.getDeviceNo()+data.getMsgNo()+data.getReqTime()+apiKey;
        Log.e("参数拼接",securityCodeStr);
        String securityCodeMD5Str = MD5.MD5Encode(securityCodeStr);
        data.setSecurityCode(securityCodeMD5Str);


        return data.reqContent();
    }
}
