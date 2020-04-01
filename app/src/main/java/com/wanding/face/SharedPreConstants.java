package com.wanding.face;

/**
 * Time: 2019/12/11
 * Author:Administrator
 * Description: 本地保存配置文件常量管理
 */
public class SharedPreConstants {

    /**
     * 权限设置
     */
    public static final String PERMISSIONS_FILE_NAME = "permissions";
    public static final String PERMISSIONS_REFUND_KEY_NAME = "refundPermissions";
    public static final String PERMISSIONS_AUTH_KEY_NAME = "authPermissions";

    /**
     * 刷脸参数face_code_type设置
     * FACE_CODE_KEY_NAME = "0":刷脸请求返回人脸码进行支付
     * PAYMENT_CODE_KEY_NAME = "1"：刷脸请求返回支付码进行支付
     */
    public static final String FACE_CODE_TYPE_FILE_NAME = "facecodetype";
    public static final String FACE_CODE_TYPE_KEY_NAME = "isFaceCode";
}
