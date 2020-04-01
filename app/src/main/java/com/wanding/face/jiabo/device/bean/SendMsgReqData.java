package com.wanding.face.jiabo.device.bean;

import android.util.Log;

import java.io.Serializable;

/**
 * 发送数据到云打印设备执行打印
 */
public class SendMsgReqData implements Serializable {

    /**
     * 本次 API 请求发生的时刻，值为当前 UNIX 时间戳。
     * 13 位数字，精确到毫秒
     */
    private String reqTime;

    /**
     * 安全检验码，用 API 密钥和规定的参数进行 MD5 运
     * 算的结果。
     * mode=2 和 和 mode=3  时参与 MD5  运算的参数和顺
     * 序是：
     * memberCode+deviceNo+msgNo（如果存在）
     * +reqTime+apiKey。
     * 所有字符串合并后进行 MD5 运算，即 MD5(合并后
     * 字符串)
     */
    private String securityCode;

    /**
     * 商户编码
     * 在“佳博网络云打印平台”注册帐号后按指引得到
     * API集成信息，里面包含有商户编码
     */
    private String memberCode;

    /**
     * 佳博网络云打印机的设备编码
     * 添加终端后，可以在“佳博网络云打印平台”看到
     * 终端的设备编号
     */
    private String deviceNo;

    /**
     * 打印信息的格式类型：
     * mode=2，自由格式打印） （推荐使用）；
     * mode=3，指令集打印
     */
    private String mode;

    /**
     * mode=2 格式标签请查阅下面说明
     * mode=3 格式：十六进制命令集或十六进制字符串打
     * 印（无空格，如十六进制 0x12 0xAB 0xCD 0xEF，
     * 发送内容 0x120xAB0xCD0xEF）
     */
    private String msgDetail;

    /**
     * 参数 mode=2 时，允许指定打印内容使用编码格式，
     * （不填写）默认 charset=1
     * 备注：票据打印机中文默认是 gb18030，标签打印
     * 机中文需要设置为 utf-8
     */
    private String charset;

    /**
     * 订单编号
     * 自定义的打印信息编号，可选，一般由用户生成，
     * 用于跟踪订单状态。如果没有设置，WEB API 将自
     * 动分配
     */
    private String msgNo;

    /**
     * 是否重新打印，1:是；0:否。默认 0，若是重新打印
     * （值为 1）则系统允许 msgNo 字段重复
     */
    private int reprint;

    /**
     * 打印联数，取值范围：[ 1 - 5 ]，默认 1
     */
    private int times;

    /**
     * 发送的语音内容。仅支持语音版打印机。
     */
    private String voice;

    /**
     * 是否多订单模式，1:订单模式，0:单个订单，默认 0
     */
    private int multi;

    public SendMsgReqData() {

    }

    /**
     * 请求发送内容
     */
    public String reqContent(){
        final StringBuilder sb = new StringBuilder("");
        sb.append("reqTime=").append(reqTime).append("&");
        sb.append("securityCode=").append(securityCode).append("&");
        sb.append("memberCode=").append(memberCode).append("&");
        sb.append("deviceNo=").append(deviceNo).append("&");
        sb.append("mode=").append(mode).append("&");
        sb.append("msgDetail=").append(msgDetail).append("&");
        sb.append("charset=").append(charset).append("&");
        sb.append("msgNo=").append(msgNo).append("&");
        sb.append("reprint=").append(reprint).append("&");
        sb.append("times=").append(times).append("&");
        sb.append("voice=").append(voice).append("&");
        sb.append("multi=").append(multi);
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

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMsgDetail() {
        return msgDetail;
    }

    public void setMsgDetail(String msgDetail) {
        this.msgDetail = msgDetail;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getMsgNo() {
        return msgNo;
    }

    public void setMsgNo(String msgNo) {
        this.msgNo = msgNo;
    }

    public int getReprint() {
        return reprint;
    }

    public void setReprint(int reprint) {
        this.reprint = reprint;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getMulti() {
        return multi;
    }

    public void setMulti(int multi) {
        this.multi = multi;
    }
}
