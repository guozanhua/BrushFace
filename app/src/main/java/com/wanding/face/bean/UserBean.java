package com.wanding.face.bean;

import java.io.Serializable;

/**
 * 登录用户实体（业务员）
 */
public class UserBean implements Serializable{



    private boolean isQueryCoupons;//: false,
	private String merchant_no;//: "1001271",
	private String mname;// "测试商户6.6官方",
	private String terminal_id;//: "12969",
	private String accessToken;//: "mae9sti8inuis3dhi724qb6tgl1b8yjc",
	private String mid;//: 1922,
	private String sid;//: 5186
	private String eid;//: 2161,
	private String ename;//: "青蛙款台0827-1"

	/**
	 * 判定退款时是否需输入验证码
	 */
	private boolean isRmvVerCodePri;


    public UserBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public boolean isQueryCoupons() {
		return isQueryCoupons;
	}

	public void setQueryCoupons(boolean queryCoupons) {
		isQueryCoupons = queryCoupons;
	}

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public boolean isRmvVerCodePri() {
		return isRmvVerCodePri;
	}

	public void setRmvVerCodePri(boolean rmvVerCodePri) {
		isRmvVerCodePri = rmvVerCodePri;
	}


}
