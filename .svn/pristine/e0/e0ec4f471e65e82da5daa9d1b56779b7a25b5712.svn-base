package com.wanding.face.payutil;

import android.util.Log;

import com.wanding.face.Constants;
import com.wanding.face.bean.AuthBaseRequest;
import com.wanding.face.bean.AuthConfirmReqDate;
import com.wanding.face.bean.FacePayAuthInfoReq;
import com.wanding.face.bean.FacePayAuthInfoRes;
import com.wanding.face.bean.FacePayUnifiedOrderReq;
import com.wanding.face.bean.OrderListReqData;
import com.wanding.face.bean.PosMemConsumeReqData;
import com.wanding.face.bean.PosPayQueryReqData;
import com.wanding.face.bean.UserBean;
import com.wanding.face.jiabo.device.bean.PosScanpayReqData;
import com.wanding.face.jiabo.device.bean.PosScanpayResData;
import com.wanding.face.utils.DateFormatUtils;
import com.wanding.face.utils.FastJsonUtil;
import com.wanding.face.utils.RandomStringGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付参数封装
 */
public class PayParamsReqUtil {

    private static final String TAG = "PayParamsReqUtil";

    private static final String pay_ver = "100";
    private static final String version = "1.0";
    private static final String charset = "UTF-8";


    private static final String  SIGN_TYPE = "MD5";


    /**
     * 获取authinfo请求实体
     */
    public static Map<String,String> getAuthinfoReq(UserBean userBean, String rawdata) throws Exception{

        FacePayAuthInfoReq authInfoReq = new FacePayAuthInfoReq();
        authInfoReq.setVersion(version);
        authInfoReq.setTimestamp(DateFormatUtils.ISO_DATETIME_SS.format(new Date()));
        authInfoReq.setCharset(charset);
        authInfoReq.setMch_no(userBean.getMerchant_no());
        authInfoReq.setTerm_no(userBean.getTerminal_id());
        //随机字符串
        String nonceStr = RandomStringGenerator.getSerialNumTwo(15);
        Log.e("随机字符串",nonceStr);
        authInfoReq.setNonce_str(nonceStr);
        authInfoReq.setSign_type(SIGN_TYPE);
        authInfoReq.setPay_type(Constants.PAY_TYPE_010WX);
        authInfoReq.setAttach("AAAABBBBCCCC");
        authInfoReq.setRawdata(rawdata);
        //参数加签
        Map<String, String> map = FastJsonUtil.object2Map(authInfoReq);
        String sign = FacePayUtils.generateSignature(map,userBean.getAccessToken(),SIGN_TYPE);
        authInfoReq.setSign(sign);

        return FastJsonUtil.object2Map(authInfoReq);

    }

    /**
     * 微信刷脸SDK参数
     */
    public static Map<String,String> getBrushFaceParams(String totalFee,String payMode,FacePayAuthInfoRes authinfo) throws Exception{
        Map<String,String> params = new HashMap();
        if(authinfo != null){

            //必填参数
            params.put(Constants.PARAMS_APPID, authinfo.getAppid());
            params.put(Constants.PARAMS_MCH_ID, authinfo.getMch_id());
            params.put(Constants.PARAMS_SUB_APPID, authinfo.getSub_appid());
            params.put(Constants.PARAMS_SUB_MCH_ID, authinfo.getSub_mch_id());
            params.put(Constants.PARAMS_STORE_ID, authinfo.getStore_id());
            params.put(Constants.PARAMS_FACE_AUTHTYPE, "FACEPAY");
            params.put(Constants.PARAMS_AUTHINFO, authinfo.getAuthinfo());
            params.put(Constants.PARAMS_ASK_FACE_PERMIT, "1");
            params.put(Constants.PARAMS_MCH_NO, authinfo.getMch_no());
            params.put(Constants.PARAMS_TERM_NO, authinfo.getTerm_no());
            //选填参数
            String phone = "";
            params.put(Constants.PARAMS_TELEPHONE, phone);
            //商户订单号
            String out_trade_no = RandomStringGenerator.getRandomNum();
            Log.e("商户订单号：",out_trade_no);
            params.put(Constants.PARAMS_OUT_TRADE_NO, out_trade_no);
            params.put(Constants.PARAMS_TOTAL_FEE, totalFee);
            params.put(Constants.PARAMS_FACE_CODE_TYPE, payMode);
            params.put(Constants.PARAMS_ASK_RET_PAGE, "0");
        }
        return params;
    }

    /**
     * 微信刷脸API参数
     */
    public static Map<String,String> getPlaceorderParams(UserBean userBean,String face_code,String openid,Map map) throws Exception{
        FacePayUnifiedOrderReq facePayUnifiedOrderReq = (FacePayUnifiedOrderReq) FastJsonUtil.map2Object(map,FacePayUnifiedOrderReq.class);
        //随机字符串
        String nonceStr = RandomStringGenerator.getSerialNumTwo(15);
        Log.e("随机字符串",nonceStr);
        facePayUnifiedOrderReq.setNonce_str(nonceStr);
        facePayUnifiedOrderReq.setSign_type(SIGN_TYPE);
        //终端流水号
        String term_transaction_sn = facePayUnifiedOrderReq.getOut_trade_no();
        Log.e("终端流水号：",term_transaction_sn);
        facePayUnifiedOrderReq.setTerm_transaction_sn(term_transaction_sn);
        facePayUnifiedOrderReq.setOpenid(openid);
        facePayUnifiedOrderReq.setFace_code(face_code);
        facePayUnifiedOrderReq.setPay_type(Constants.PAY_TYPE_010WX);
        facePayUnifiedOrderReq.setBody(userBean.getMname());
        facePayUnifiedOrderReq.setGoods_tag(userBean.getMname());
        facePayUnifiedOrderReq.setDetail(userBean.getMname());
        facePayUnifiedOrderReq.setAttach(userBean.getMname());
        facePayUnifiedOrderReq.setMachineType(Constants.DEVICE_TYPE);

        //参数加签
        Map<String, String> params = FastJsonUtil.object2Map(facePayUnifiedOrderReq);
        params.remove(Constants.PARAMS_OUT_TRADE_NO);
        String sign = FacePayUtils.generateSignature(params,userBean.getAccessToken(),SIGN_TYPE);
        facePayUnifiedOrderReq.setSign(sign);

        return FastJsonUtil.object2Map(facePayUnifiedOrderReq);
    }

    /**
     * API查询交易明细参数配置
     */
    public static OrderListReqData getOrderListReqData(UserBean userBean,int pageNum,int pageCount,String dataType){
        OrderListReqData reqData = new OrderListReqData();
        reqData.setPageNum(pageNum+"");
        reqData.setNumPerPage(pageCount+"");
        reqData.setMid(userBean.getMid());
        reqData.setEid(userBean.getEid());
        reqData.setDate_type(dataType);
        return reqData;

    }

    /**
     * pay_ver	版本号，当前版本“100”
     * pay_type	请求类型，“010”微信，“020”支付宝，“060”qq钱包
     * service_id	接口类型，当前类型“010”
     * merchant_no	商户号
     * terminal_id	终端号
     * terminal_trace	终端流水号（socket协议：长度为6位，Http协议：长度为32位）
     * terminal_time	终端交易时间，yyyyMMddHHmmss，全局统一时间格式
     * auth_no	授权码(二维码号)
     * total_fee	金额，单位分
     * order_body	订单描述
     * key_sign	签名检验串,拼装所有必传参数+令牌，32位md5加密转换
     */
    public static PosScanpayReqData payReq(UserBean userBean, String pay_type, String total_fee,String auth_no ){

        PosScanpayReqData posBean = new PosScanpayReqData();
        String pay_verStr = pay_ver;
        String pay_typeStr = pay_type;
        String service_idStr = "010";

        //merchant_no	商户号
        String merchant_noStr = userBean.getMerchant_no();
        //terminal_id	终端号
        String terminal_idStr = userBean.getTerminal_id();



        //terminal_trace	终端流水号（socket协议：长度为6位，Http协议：长度为32位）
        String terminal_traceStr = RandomStringGenerator.getAFRandomNum();

        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());
        String auth_noStr = auth_no;
        String total_feeStr = total_fee;
        String order_bodyStr = "";

        posBean.setPay_ver(pay_verStr);
        posBean.setPay_type(pay_typeStr);
        posBean.setService_id(service_idStr);
        posBean.setMerchant_no(merchant_noStr);
        posBean.setTerminal_id(terminal_idStr);
        posBean.setTerminal_trace(terminal_traceStr);
        posBean.setTerminal_time(terminal_timeStr);
        posBean.setAuth_no(auth_noStr);
        posBean.setTotal_fee(total_feeStr);
        posBean.setOrder_body(order_bodyStr);
        posBean.setMachineType(Constants.DEVICE_TYPE);

        posBean.setKey_sign(posBean.getSignStr(userBean.getAccessToken()));



        return posBean;
    }




    /**
     * 会员消费实体参数值
     */
    public static PosMemConsumeReqData consumeReq(String pay_type, String auth_no, String total_fee,
                                                  UserBean userBean, int cardId){

        PosMemConsumeReqData posBean = new PosMemConsumeReqData();
        String pay_verStr = pay_ver;
        String pay_typeStr = pay_type;
        String service_idStr = "010";

        //merchant_no	商户号
        String merchant_noStr = userBean.getMerchant_no();
//        String merchant_noStr = "1001369";
        //terminal_id	终端号
        String terminal_idStr = userBean.getTerminal_id();
//        String terminal_idStr = "13210 ";

        //终端流水号
        String terminal_traceStr = RandomStringGenerator.getAFRandomNum();
        //终端交易时间
        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());
        String auth_noStr = auth_no;
        String total_feeStr = total_fee;
        String memberCodeStr = "";
        String order_bodyStr = String.valueOf(cardId);

        posBean.setPay_ver(pay_verStr);
        posBean.setPay_type(pay_typeStr);
        posBean.setService_id(service_idStr);
        posBean.setMerchant_no(merchant_noStr);
        posBean.setTerminal_id(terminal_idStr);
        posBean.setTerminal_trace(terminal_traceStr);
        posBean.setTerminal_time(terminal_timeStr);
        posBean.setAuth_no(auth_noStr);
        posBean.setTotal_fee(total_feeStr);
        posBean.setMemberCode(memberCodeStr);
        posBean.setOrder_body(order_bodyStr);
        posBean.setMember_type("Y");//区分刷脸会员消费还是POS会员消费
//        posBean.setMachineType(Constants.DEVICE_TYPE);

        posBean.setKey_sign(posBean.getSignStr(userBean.getAccessToken()));
//        posBean.setKey_sign(posBean.getSignStr("r2cxr989mft8ymupmoscy5uj67ia983c"));



        return posBean;
    }

    /**
     * 支付中状态时轮询获取订单状态
     */
    public static PosPayQueryReqData paymentStateQueryReq(String pay_type, PosScanpayResData resData, UserBean userBean){

        PosPayQueryReqData posBean = new PosPayQueryReqData();

        posBean.setPay_ver(pay_ver);
        posBean.setPay_type(pay_type);
        posBean.setService_id("020");
        posBean.setMerchant_no(userBean.getMerchant_no());
        posBean.setTerminal_id(userBean.getTerminal_id());

        String terminal_traceStr = RandomStringGenerator.getAFRandomNum();
        posBean.setTerminal_trace(terminal_traceStr);

        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());
        posBean.setTerminal_time(terminal_timeStr);
        posBean.setOut_trade_no(resData.getOut_trade_no());
        posBean.setPay_trace("");
        posBean.setPay_time("");
        posBean.setOperator_id("");
        posBean.setKey_sign(posBean.getSignStr(userBean.getAccessToken()));
        return posBean;
    }

    /**
     *  预授权
     */
    public static AuthBaseRequest authReq(boolean isBrushFace, UserBean userBean, String pay_type, String total_fee, String auth_no,String openid,String term_transaction_sn){
        AuthBaseRequest request = new AuthBaseRequest();
        String pay_verStr = pay_ver;
        String pay_typeStr = pay_type;
        String service_idStr = "011";

        //merchant_no	商户号
        String merchant_noStr = userBean.getMerchant_no();
        //terminal_id	终端号
        String terminal_idStr = userBean.getTerminal_id();


        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());
        String auth_noStr = auth_no;
        String operator_idStr = null;
        String total_feeStr = total_fee;
        request.setPay_ver(pay_verStr);
        request.setPay_type(pay_typeStr);
        request.setService_id(service_idStr);
        request.setMerchant_no(merchant_noStr);
        request.setTerminal_id(terminal_idStr);

        request.setTerminal_time(terminal_timeStr);
        //terminal_trace	终端流水号（socket协议：长度为6位，Http协议：长度为32位）
        if(isBrushFace){
            request.setFace_code(auth_noStr);
            request.setOpenid(openid);
            request.setTerminal_trace(term_transaction_sn);
            request.setOrder_body("");
        }else{
            request.setTerminal_trace(RandomStringGenerator.getAFRandomNum());
            request.setAuth_code(auth_noStr);
            request.setOrder_body(null);
        }

        request.setOperator_id(operator_idStr);
        request.setTotal_fee(total_feeStr);
        request.setMachineType(Constants.DEVICE_TYPE);


        //参数加签
        Log.e("参数:",FastJsonUtil.toJSONString(request));
        Map<String, Object> map = request.toMap();
        Log.e("toMap参数:",map.toString());
        String mapStr = FacePayUtils.getSign(map,userBean.getAccessToken());
        request.setKey_sign(mapStr);
        return request;
    }


    /**
     *  预授权撤销
     */
    public static AuthConfirmReqDate authCancelReq(UserBean userBean, String auth_no){
        AuthConfirmReqDate request = new AuthConfirmReqDate();
        String pay_verStr = pay_ver;
        String service_idStr = "012";
        String merchant_nameStr = userBean.getMname();
        //merchant_no	商户号
        String merchant_noStr = userBean.getMerchant_no();
        //terminal_id	终端号
        String terminal_idStr = userBean.getTerminal_id();
        //terminal_trace	终端流水号（socket协议：长度为6位，Http协议：长度为32位）
        String terminal_traceStr = RandomStringGenerator.getAFRandomNum();

        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());

        request.setPay_ver(pay_verStr);
        request.setService_id(service_idStr);
        request.setMerchant_name(merchant_nameStr);
        request.setMerchant_no(merchant_noStr);
        request.setTerminal_id(terminal_idStr);
        request.setTerminal_trace(terminal_traceStr);
        request.setTerminal_time(terminal_timeStr);



        request.setOut_trade_no(auth_no);
        request.setMachineType(Constants.DEVICE_TYPE);

        //参数加签
        Log.e("参数:",FastJsonUtil.toJSONString(request));
        Map<String, Object> map = request.toMap();
        Log.e("toMap参数:",map.toString());
        String mapStr = FacePayUtils.getSign(map,userBean.getAccessToken());
        request.setKey_sign(mapStr);
        return request;
    }

    /**
     *  预授权完成
     */
    public static AuthConfirmReqDate authConfirmReq(UserBean userBean, String auth_no, String total_fee){
        AuthConfirmReqDate request = new AuthConfirmReqDate();
        String pay_verStr = pay_ver;
        String service_idStr = "013";
        String merchant_nameStr = userBean.getMname();
        //merchant_no	商户号
        String merchant_noStr = userBean.getMerchant_no();
        //terminal_id	终端号
        String terminal_idStr = userBean.getTerminal_id();
        //terminal_trace	终端流水号（socket协议：长度为6位，Http协议：长度为32位）
        String terminal_traceStr = RandomStringGenerator.getAFRandomNum();

        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());
        String total_feeStr = total_fee;

        request.setPay_ver(pay_verStr);
        request.setService_id(service_idStr);
        request.setMerchant_name(merchant_nameStr);
        request.setMerchant_no(merchant_noStr);
        request.setTerminal_id(terminal_idStr);
        request.setTerminal_trace(terminal_traceStr);
        request.setTerminal_time(terminal_timeStr);

        request.setConsume_amount(total_feeStr);


        request.setOut_trade_no(auth_no);
        request.setMachineType(Constants.DEVICE_TYPE);

        //参数加签
        Log.e("参数:",FastJsonUtil.toJSONString(request));
        Map<String, Object> map = request.toMap();
        Log.e("toMap参数:",map.toString());
        String mapStr = FacePayUtils.getSign(map,userBean.getAccessToken());
        request.setKey_sign(mapStr);
        return request;
    }

    /**
     *  预授权完成撤销
     */
    public static AuthConfirmReqDate authConfirmCancelReq(UserBean userBean, String auth_no, String total_fee){
        AuthConfirmReqDate request = new AuthConfirmReqDate();
        String pay_verStr = pay_ver;
        String service_idStr = "013";
        String merchant_nameStr = userBean.getMname();
        //merchant_no	商户号
        String merchant_noStr = userBean.getMerchant_no();
        //terminal_id	终端号
        String terminal_idStr = userBean.getTerminal_id();
        //terminal_trace	终端流水号（socket协议：长度为6位，Http协议：长度为32位）
        String terminal_traceStr = RandomStringGenerator.getAFRandomNum();

        String terminal_timeStr = DateFormatUtils.ISO_DATETIME_SS.format(new Date());
        String total_feeStr = total_fee;

        request.setPay_ver(pay_verStr);
        request.setService_id(service_idStr);
        request.setMerchant_name(merchant_nameStr);
        request.setMerchant_no(merchant_noStr);
        request.setTerminal_id(terminal_idStr);
        request.setTerminal_trace(terminal_traceStr);
        request.setTerminal_time(terminal_timeStr);

        request.setRefund_fee(total_feeStr);


        request.setOut_trade_no(auth_no);
        request.setMachineType(Constants.DEVICE_TYPE);

        //参数加签
        Log.e("参数:",FastJsonUtil.toJSONString(request));
        Map<String, Object> map = request.toMap();
        Log.e("toMap参数:",map.toString());
        String mapStr = FacePayUtils.getSign(map,userBean.getAccessToken());
        request.setKey_sign(mapStr);
        return request;
    }


}
