package com.wanding.face.bean;

import java.io.Serializable;

/**
 * 支付参数实体
 */
public class PayParamsReqData implements Serializable {

    /**
     * 商户号（API）
     */
    private String mch_no;

    /**
     * 终端号（API）
     */
    private String term_no;
    /**
     * 微信刷脸SDK支付参数
     */
    private String appid;//公众号
    private String mch_id;//商户号
    private String sub_appid;//子商户公众账号ID(非服务商模式不填),须与调用支付接口时字段一致
    private String sub_mch_id;//子商户号(非服务商模式不填),须与调用支付接口时字段一致
    private String store_id;//门店编号
    private String telephone;//用户手机号,非必填
    private String out_trade_no;//商户订单号，须与调用支付接口时字段一致，该字段在在face_code_type为"1"时可不填，为"0"时必填
    private String total_fee;//订单金额(数字), 单位分，该字段在在face_code_type为"1"时可不填，为"0"时必填
    private String face_code_type;//目标face_code类型，可选值："0"，人脸付款码：数字字母混合，通过「刷脸支付」接口完成支付；"1"，刷卡付款码：18位数字，通过「付款码支付/被扫支付」接口完成支付。如果不填写则默认为"0"
    private String ignore_update_pay_result;
    private String face_authtype;//可选值：FACEPAY: 人脸凭证，常用于刷脸支付 FACEPAY_DELAY: 延迟支付(需联系微信支付开通权限)
    private String authinfo;// 	调用凭证
    private String ask_face_permit;//支付成功页是否需要展示人脸识别授权项。展示：1。不展示：0。人脸识别授权项：用户授权后用于1:N识别，可返回用户信息openid，建议商户有自己会员系统时，填1
    private String ask_ret_page;//是否展示微信支付成功页，可选值："0"，不展示；"1"，展示

    public PayParamsReqData() {

    }

    public String getMch_no() {
        return mch_no;
    }

    public void setMch_no(String mch_no) {
        this.mch_no = mch_no;
    }

    public String getTerm_no() {
        return term_no;
    }

    public void setTerm_no(String term_no) {
        this.term_no = term_no;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_appid() {
        return sub_appid;
    }

    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getFace_code_type() {
        return face_code_type;
    }

    public void setFace_code_type(String face_code_type) {
        this.face_code_type = face_code_type;
    }

    public String getIgnore_update_pay_result() {
        return ignore_update_pay_result;
    }

    public void setIgnore_update_pay_result(String ignore_update_pay_result) {
        this.ignore_update_pay_result = ignore_update_pay_result;
    }

    public String getFace_authtype() {
        return face_authtype;
    }

    public void setFace_authtype(String face_authtype) {
        this.face_authtype = face_authtype;
    }

    public String getAuthinfo() {
        return authinfo;
    }

    public void setAuthinfo(String authinfo) {
        this.authinfo = authinfo;
    }

    public String getAsk_face_permit() {
        return ask_face_permit;
    }

    public void setAsk_face_permit(String ask_face_permit) {
        this.ask_face_permit = ask_face_permit;
    }

    public String getAsk_ret_page() {
        return ask_ret_page;
    }

    public void setAsk_ret_page(String ask_ret_page) {
        this.ask_ret_page = ask_ret_page;
    }
}
