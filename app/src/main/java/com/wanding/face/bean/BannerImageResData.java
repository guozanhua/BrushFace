package com.wanding.face.bean;

import java.io.Serializable;

/**
 * Time: 2020/1/7
 * Author:Administrator
 * Description: 首页轮播图请求返回
 */
public class BannerImageResData implements Serializable {

    /**
     * 设备类型 "7",
     */
    private String machine_type;
    /**
     * "",
     */
    private String img_path_5;
    /**
     * "",
     */
    private String img_path_4;
    /**
     * "",
     */
    private String img_path_3;
    /**
     * "https://devpay.wandingkeji.cn/image/adGroup/ceshi01/20191218d8bl9zrej6.jpg",
     */
    private String img_path_2;
    /**
     * "https://devpay.wandingkeji.cn/image/adGroup/ceshi01/20191218d8bl9zrej6.jpg",
     */
    private String img_path_1;
    /**
     * "2019-12-18 13:39:42",
     */
    private String create_time;
    /**
     * "ceshi01-gai-1",
     */
    private String group_name;
    /**
     * "ceshi01",
     */
    private String description;
    /**
     * "2019-12-18 13:58:39",
     */
    private String updata_time;
    /**
     * "c658368c-244b-43fc-9a76-f73fc1ddc7bc"
     */
    private String uuid;

    public BannerImageResData() {
    }

    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

    public String getImg_path_5() {
        return img_path_5;
    }

    public void setImg_path_5(String img_path_5) {
        this.img_path_5 = img_path_5;
    }

    public String getImg_path_4() {
        return img_path_4;
    }

    public void setImg_path_4(String img_path_4) {
        this.img_path_4 = img_path_4;
    }

    public String getImg_path_3() {
        return img_path_3;
    }

    public void setImg_path_3(String img_path_3) {
        this.img_path_3 = img_path_3;
    }

    public String getImg_path_2() {
        return img_path_2;
    }

    public void setImg_path_2(String img_path_2) {
        this.img_path_2 = img_path_2;
    }

    public String getImg_path_1() {
        return img_path_1;
    }

    public void setImg_path_1(String img_path_1) {
        this.img_path_1 = img_path_1;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdata_time() {
        return updata_time;
    }

    public void setUpdata_time(String updata_time) {
        this.updata_time = updata_time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
