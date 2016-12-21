package org.cm.entity;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
	private int id;//订单的id
	private String from_station;
	private String to_station;
	private int carId;//车的id；
	private long startTime;//开始时间
	private long endTime;//结束时间
	private double fee;//乘坐本次公交所花费的钱
	private int currentId; //当前线路id
	private String payState;//支付的状态
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFrom_station() {
		return from_station;
	}
	public void setFrom_station(String from_station) {
		this.from_station = from_station;
	}
	public String getTo_station() {
		return to_station;
	}
	public void setTo_station(String to_station) {
		this.to_station = to_station;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public int getCurrentId() {
		return currentId;
	}
	public void setCurrentId(int currentId) {
		this.currentId = currentId;
	}
	public String getPayState() {
		return payState;
	}
	public void setPayState(String payState) {
		this.payState = payState;
	}

}
