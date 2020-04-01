package com.wanding.face.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 交易明细返回实体
 */
public class OrderListResData implements Serializable {
	
	private String message;//": "查询成功",
	private String status;//": 200
	
	private Integer countRow;//": 7,
	private Integer numPerPage;//": 1,
	private Integer totalCount;//": 7,
	private Integer pageNum;//": 1
	/**
	 * isHistory N：实时订单，Y：历史订单
	 */
	private String isHistory;

	private List<OrderDetailData> orderList;
	
	public OrderListResData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCountRow() {
		return countRow;
	}

	public void setCountRow(Integer countRow) {
		this.countRow = countRow;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public String getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}


	public List<OrderDetailData> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<OrderDetailData> orderList) {
		this.orderList = orderList;
	}

	

	
}
