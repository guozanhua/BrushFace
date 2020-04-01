package com.wanding.face.jiabo.device.bean;

import android.util.Log;

import com.wanding.face.utils.MD5;

/**
 *  查询云打印设备列表（获取打印机状态）
 */
public class QueryDevListReqData {

    /**
     * 本次 API 请求发生的时刻，值为当前 UNIX 时间戳。
     * 13 位数字，精确到毫秒。
     */
    private String reqTime;

    /**
     * 安全检验码，用 API 密钥和规定的参数进行 MD5 运
     * 算的结果
     * 参与 MD5 运算的参数和顺序是：
     * memberCode+reqTime+apiKey
     */
    private String securityCode;

    /**
     * 商户编码
     * 在“佳博网络云打印平台”注册帐号后按指引得到
     * API集成信息，里面包含有商户编码
     */
    private String memberCode;

    /**
     * 设备 ID，不提交该字段查询用户所有设备
     */
    private String deviceID;

    public QueryDevListReqData() {

    }



    /**
     * 请求发送内容
     */
    public String reqContent(){
        final StringBuilder sb = new StringBuilder("");
        sb.append("reqTime=").append(reqTime).append("&");
        sb.append("securityCode=").append(securityCode).append("&");
        sb.append("memberCode=").append(memberCode).append("&");
        sb.append("deviceID=").append(deviceID);
        String sbStr = sb.toString();
        Log.e("参数拼接",sbStr);
        return sbStr;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
