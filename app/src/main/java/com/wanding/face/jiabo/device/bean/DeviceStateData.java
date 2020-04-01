package com.wanding.face.jiabo.device.bean;
/**
 * 设备状态
 */
public class DeviceStateData {

    /**
     * 设备 ID
     */
    private String deviceID;

    /**
     * Int  在线状态（0 离线， 1 在线）
     */
    private int online;

    /**
     * 打印机状态:
     * 1 正常
     * 2 缺纸
     * 3 其他异常
     * 4 过热
     * 5 开盖
     * 8 暂停
     * 9 打印中
     */
    private int status;

    /**
     * 最后一次打印时间
     */
    private String outtime;


    /**
     * 累计打印次数
     */
    private int printnum;

    public DeviceStateData() {

    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getOnlineState(int online){
        if(online == 1){
            return "在线";
        }
        return "离线";
    }

    public String getTerminalState(int status){
        if(status == 1){
            return "正常";
        }else if(status == 2){
            return "缺纸";
        }
        else if(status == 3){
            return "其他异常";
        }
        else if(status == 4){
            return "过热";
        }
        else if(status == 5){
            return "开盖";
        }
        else if(status == 8){
            return "暂停";
        }
        return "打印中";

    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public int getPrintnum() {
        return printnum;
    }

    public void setPrintnum(int printnum) {
        this.printnum = printnum;
    }
}
