package com.wanding.face.jiabo.device.bean;

import java.io.Serializable;

/**  蓝牙历史匹配记录实体  */
public class BlueToothBean implements Serializable {

	private Integer id;
	private String name;
	private String address;
	private String checked;//默认设备标示 0，未默认  1：选择默认
	
	public BlueToothBean() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	

	
	
}
