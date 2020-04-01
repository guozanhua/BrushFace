package com.wanding.face.bean;
/**
 * 公共返回参数
 */
public class PublicResData {

    private String return_code;//": "SUCCESS",
    private String return_msg;//": "请求成功",

    public PublicResData() {
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }
}
