package com.wanding.face.bean;

/**
 * Time: 2019/11/27
 * Author:Administrator
 * Description: 校验操作员密码和修改操作员密码请求参数
 */
public class CheckPasswdReqData {

    /**
     * 校验密码
     */
    private String password;

    /**
     * 设备号
     */
    private String mCode;

    /**
     * 设备类型
     */
    private String type;

    public CheckPasswdReqData() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
