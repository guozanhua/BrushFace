package com.wanding.face.jiabo.device.bean;

import java.util.List;

/**
 *  查询云打印设备列表（获取打印机状态）返回实体
 */
public class QueryDevListResData {

   /* {
        "code": 1,
        "msg": "查询成功",
        "statusList": [{
        "deviceID": "00391282538428370",
                "online": 1,
                "status": 1,
                "outtime": "",
                "printnum": 0
                      }]
    }*/

    /**
     * 返回代码
     */
    private int code;

    /**
     * 安全检验码，用 API 密钥和规定的参数进行 MD5 运
     * 算的结果
     * 参与 MD5 运算的参数和顺序是：
     * memberCode+reqTime+apiKey
     */
    private String msg;

    /**
     * 状态列表
     */
    private List<DeviceStateData> statusList;



    public QueryDevListResData() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DeviceStateData> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<DeviceStateData> statusList) {
        this.statusList = statusList;
    }
}
