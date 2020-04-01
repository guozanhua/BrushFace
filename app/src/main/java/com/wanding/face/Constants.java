package com.wanding.face;

import android.os.Build;
import android.util.Log;

import com.wanding.face.utils.Utils;

/**
 *  常量帮助类
 */
public class Constants {

    /**
     * 设备类型:machineType
     *    YLY_PRINT("0","打印机"),
     * 	 FUYOU("1","扫呗终端"),
     * 	 LABA("2","云喇叭"),
     * 	 NEWLAND("3","新大陆终端"),
     * 	 YUBAO("4","御宝科技终端"),
     * 	 FUIOU("5","富友终端"),
     * 	 YINGTONG("6","银通终端"),
     * 	 FACE_WX("7","微信青蛙"),
     * 	 FACE_ALI("8","支付宝蜻蜓"),
     * 	QIANKEDUO_WX("9","钱客多");
     */
    public static final String DEVICE_TYPE = "7";
    /**
     * 设备序列号
     */
    public static final String SERIAL_NUM = Build.SERIAL;

    /**
     * 统一定义所有敏感操作需跳转密码校验界面的入口，根据不同入口进入下一步操作
     * 1：密码设置界面进入
     * 2：快速退款操作进入
     * 3：订单详情退款进入
     * 4：预授权撤销
     * 5：预授权完成撤销
     * 6：权限设置
     */
    public static final String ACTION_PASSWD_MODIFY_PASSWD = "1";
    public static final String ACTION_PASSWD_FAST_REFUND = "2";
    public static final String ACTION_PASSWD_DETAIL_REFUND = "3";
    public static final String ACTION_PASSWD_AUTH_CANCEL = "4";
    public static final String ACTION_PASSWD_AUTH_CONFIRM_CANCEL = "5";
    public static final String ACTION_PASSWD_PERMISSIONS = "6";

    /**
     * 统一定义进入广告设置BannerImageActivity界面的入口
     */
    public static final String ACTION_LOGIN_TO_BANNER = "1";
    public static final String ACTION_SETTING_TO_BANNER = "2";

    /**
     * 支付类型选择
     * 微信 = "010"，
     * 支付宝 = "020"，
     * 银联二维码 = "060"，
     * 刷卡 = "040"，
     * 翼支付 = "050"
     */
    public static final String PAY_TYPE_010WX = "010";
    public static final String PAY_TYPE_020ALI = "020";
    public static final String PAY_TYPE_040BANK = "040";
    public static final String PAY_TYPE_050BEST = "050";
    public static final String PAY_TYPE_060UNIONPAY = "060";
    public static final String PAY_TYPE_000CASH = "000";
    public static final String PAY_TYPE_800MEMBER = "800";
    /**
     * API公共参数值
     */
    public static final String PAY_TYPE_WX = "WX";
    public static final String PAY_TYPE_ALI = "ALI";
    public static final String PAY_TYPE_BEST = "BEST";
    /**
     *DEBIT= 借记卡
     */
    public static final String PAY_TYPE_DEBIT = "DEBIT";
    /**
     *CREDIT=贷记卡
     */
    public static final String PAY_TYPE_CREDIT = "CREDIT";
    public static final String PAY_TYPE_UNIONPAY = "UNIONPAY";
    public static final String PAY_TYPE_BANK = "BANK";

    /**
     * API公共参数
     */
    public static final String PARAMS_NONCE_STR = "nonce_str";
    public static final String PARAMS_SIGN = "sign";
    public static final String PARAMS_SIGN_TYPE = "sign_type";
    public static final String PARAMS_TERM_TRANSACTION_SN = "term_transaction_sn";
    public static final String PARAMS_OPENID = "openid";
    public static final String PARAMS_FACE_CODE = "face_code";
    public static final String PARAMS_PAY_TYPE = "pay_type";
    public static final String PARAMS_BODY = "body";
    public static final String PARAMS_GOODS_TAG = "goods_tag";
    public static final String PARAMS_DETAIL = "detail";
    public static final String PARAMS_ATTACH = "attach";

    /**
     * 业务类型
     */
    public static final String BUS_TYPE_PAY = "PAY";
    public static final String BUS_TYPE_AUTH = "AUTH";
    /**
     * 支付状态
     */
    public static final String PAY_STATE_SUCCESS = "SUCCESS";
    public static final String PAY_STATE_FAIL = "FAIL";
    public static final String PAY_STATE_UNKNOWN = "UNKNOWN";

    /**
     * 微信人脸SDK公共参数
     */
    public static final String RETURN_CODE = "return_code";
    public static final String RETURN_SUCCESS = "SUCCESS";
    public static final String RETURN_FAILE = "SUCCESS";
    public static final String RETURN_MSG = "return_msg";

    public static final String PARAMS_FACE_AUTHTYPE = "face_authtype";
    public static final String PARAMS_FACE_CODE_TYPE = "face_code_type";
    public static final String PARAMS_APPID = "appid";
    public static final String PARAMS_SUB_APPID = "sub_appid";
    public static final String PARAMS_MCH_ID = "mch_id";
    public static final String PARAMS_MCH_NO = "mch_no";
    public static final String PARAMS_MCH_NAME = "mch_name";
    public static final String PARAMS_TERM_NO = "term_no";
    public static final String PARAMS_SUB_MCH_ID = "sub_mch_id";
    public static final String PARAMS_STORE_ID = "store_id";
    public static final String PARAMS_AUTHINFO = "authinfo";
    public static final String PARAMS_OUT_TRADE_NO = "out_trade_no";
    public static final String PARAMS_TOTAL_FEE = "total_fee";
    public static final String PARAMS_TELEPHONE = "telephone";
    public static final String PARAMS_ASK_FACE_PERMIT = "ask_face_permit";
    public static final String PARAMS_ASK_RET_PAGE = "ask_ret_page";

    public static final String PARAMS_REPORT_ITEM = "item";
    public static final String PARAMS_REPORT_ITEMVALUE = "item_value";

    public static final String PARAMS_REPORT_MCH_ID = "mch_id";
    public static final String PARAMS_REPORT_SUT_MCH_ID = "sub_mch_id";
    public static final String PARAMS_REPORT_OUT_TRADE_NO = "out_trade_no";
    public static final String PARAMS_BANNER_STATE = "banner_state";

    /**
     * 佳博打印机参数
     */
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    public static final int MESSAGE_UPDATE_PARAMETER = 0x009;
    /**
     * 打印机连接实时连接状态
     * DISCONNET:未连接 CONNING:连接中  CONNETED:已连接
     */
    public static String DEVICE_FILE_NAME = "deviceFileName";
    public static String DEVICE_STATE_KEY = "state";
    public static String DEVICE_TYPE_KEY = "type";
    public static String DEVICE_STATE_DISCONNET = "disconnet";
    public static String DEVICE_STATE_CONNING = "conning";
    public static String DEVICE_STATE_CONNETED = "conneted";
    public static String DEVICE_STATE_CONN_FAIL = "conn_fail";


    public static String getPayWay(String payWayStr){
        String payWay = "";
        if(Utils.isNotEmpty(payWayStr)){
            if(PAY_TYPE_040BANK.equals(payWayStr)){
                return "BANK";
            }else if(PAY_TYPE_010WX.equals(payWayStr)){
                return "WX";
            }else if(PAY_TYPE_020ALI.equals(payWayStr)){
                return "ALI";
            }else if(PAY_TYPE_060UNIONPAY.equals(payWayStr)){
                return "UNIONPAY";
            }else if(PAY_TYPE_050BEST.equals(payWayStr)){
                return "BEST";
            }else if(PAY_TYPE_060UNIONPAY.equals(payWayStr)){
                return "UNIONPAY";
            }
        }
        return payWay;
    }

    /**
     * 获取支付类型
     * orderTypeStr:0正向 ,1退款
     * payWay：支付方式
     * isPrint：是否打印小票方式
     */
    public static String getPayWay(String orderTypeStr,String payWay,boolean isPrint){
        String payWayStr = "未知";
        if("0".equals(orderTypeStr)){
            if(Utils.isNotEmpty(payWay)){
                if(PAY_TYPE_WX.equals(payWay)||PAY_TYPE_010WX.equals(payWay)){
                    if(isPrint){
                        payWayStr = "微信支付/WEIXIN PAY";
                    }else
                    {
                        payWayStr = "微信";
                    }
                }else if(PAY_TYPE_ALI.equals(payWay)||PAY_TYPE_020ALI.equals(payWay)){
                    if(isPrint)
                    {
                        payWayStr = "支付宝支付/ALI PAY";
                    }else
                    {
                        payWayStr = "支付宝";
                    }
                }else if(PAY_TYPE_BEST.equals(payWay)||PAY_TYPE_050BEST.equals(payWay)){
                    if(isPrint)
                    {
                        payWayStr = "翼支付/BEST PAY";
                    }else
                    {
                        payWayStr = "翼支付";
                    }
                }else if(PAY_TYPE_DEBIT.equals(payWay)||PAY_TYPE_040BANK.equals(payWay)){
                    //DEBIT= 借记卡       CREDIT=贷记卡
                    if(isPrint)
                    {
                        payWayStr = "刷卡支付/BANK PAY";
                    }else
                    {
                        payWayStr = "银行卡(借记卡)";
                    }
                }else if(PAY_TYPE_CREDIT.equals(payWay)||PAY_TYPE_040BANK.equals(payWay)){
                    //DEBIT= 借记卡       CREDIT=贷记卡
                    if(isPrint)
                    {
                        payWayStr = "刷卡支付/BANK PAY";
                    }else
                    {
                        payWayStr = "银行卡(贷记卡)";
                    }
                }else if(PAY_TYPE_UNIONPAY.equals(payWay)||PAY_TYPE_060UNIONPAY.equals(payWay)){
                    //UNIONPAY = 银联二维码
                    if(isPrint)
                    {
                        payWayStr = "银联二维码支付/UNIONPAY PAY";
                    }else
                    {
                        payWayStr = "银联二维码";
                    }
                }else if(PAY_TYPE_BANK.equals(payWay)||PAY_TYPE_040BANK.equals(payWay)){
                    //BANK = 银行卡
                    if(isPrint)
                    {
                        payWayStr = "刷卡支付/BANK PAY";
                    }else
                    {
                        payWayStr = "银行卡";
                    }
                }
            }
        }else if("1".equals(orderTypeStr)){

            if(PAY_TYPE_WX.equals(payWay)||PAY_TYPE_010WX.equals(payWay)){
                return "微信退款";
            }else if(PAY_TYPE_ALI.equals(payWay)||PAY_TYPE_020ALI.equals(payWay)){
                return "支付宝退款";
            }else if(PAY_TYPE_BEST.equals(payWay)||PAY_TYPE_050BEST.equals(payWay)){
                return "翼支付退款";
            }else if(PAY_TYPE_UNIONPAY.equals(payWay)||PAY_TYPE_060UNIONPAY.equals(payWay)){
                return "银联二维码退款";
            }else if(PAY_TYPE_CREDIT.equals(payWay) || PAY_TYPE_DEBIT.equals(payWay)){
                //DEBIT= 借记卡       CREDIT=贷记卡
                return "消费撤销";
            }else if(PAY_TYPE_BANK.equals(payWay)||PAY_TYPE_040BANK.equals(payWay)){
                // BANK = 银行卡
                return "消费撤销";
            }
        }

        return payWayStr;
    }

    /**
     * 获取支付状态
     * orderType:交易类型 0正向（支付交易） ,1反向（退款交易）
     * status：交易状态 	0准备支付1支付完成2支付失败3.包括退款4.全部退款5.支付未知
     */
    public static String getOrderStatus(String orderType,String status){
        String orderStatus = "未知";
        if(Utils.isNotEmpty(orderType)){
            if(Utils.isNotEmpty(status)){
                //先判断是支付交易还是退款交易 0正向 ,1退款
                if("0".equals(orderType)){
                    //判断交易状态状态
                    if("1".equals(status)){
                        orderStatus = "支付成功";
                    }else if("3".equals(status)){
                        orderStatus = "包含退款";
                    }else if("4".equals(status)){
                        orderStatus = "全部退款";
                    }else if("5".equals(status)){
                        orderStatus = "支付未知";
                    }else{
                        orderStatus = "支付失败";
                    }
                }else if("1".equals(orderType)){
                    //判断退款状态
                    if("1".equals(status)){
                        orderStatus = "退款成功";
                    }else{
                        orderStatus = "退款失败";
                    }
                }
            }

        }
        return orderStatus;
    }


    /**
     * 扫码预授权，预授权撤销，预授权完成
     * payType支付方式：010：微信，020：支付宝
     * auth_type支付类型：1:预授权，2：预授权撤销，3：预授权完成,4：预授权完成撤销
     *
     */

    public static String getAuthPayTypeStr(String auth_type){

            if("1".equals(auth_type)){
                return "预授权";
            }else if("2".equals(auth_type)){
                return "预授权撤销";
            }else if("3".equals(auth_type)){
                return "预授权完成";
            }else if("4".equals(auth_type)){
                return "预授权完成撤销";
            }

        return "交易未知";
    }



}
