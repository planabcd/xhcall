package cn.tx.xhc.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RouteResult {
	private int id;
	private String[] routeFindById;
	HashMap<String,ArrayList<String>> allRoute;
	HashMap<String,Double> allTime;
	HashMap<String,Double> allFee;
	double fee;
	double time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String[] getRouteFindById() {
		return routeFindById;
	}
	public void setRouteFindById(String[] routeFindById) {
		this.routeFindById = routeFindById;
	}
	public HashMap<String, ArrayList<String>> getAllRoute() {
		return allRoute;
	}
	public void setAllRoute(HashMap<String, ArrayList<String>> allRoute) {
		this.allRoute = allRoute;
	}
	public HashMap<String, Double> getAllTime() {
		return allTime;
	}
	public void setAllTime(HashMap<String, Double> allTime) {
		this.allTime = allTime;
	}
	public HashMap<String, Double> getAllFee() {
		return allFee;
	}
	public void setAllFee(HashMap<String, Double> allFee) {
		this.allFee = allFee;
	}
	public double getFee() {
		return fee;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "RouteResult [id=" + id + ", routeFindById="
				+ Arrays.toString(routeFindById) + ", allRoute=" + allRoute
				+ ", allTime=" + allTime + ", allFee=" + allFee + ", fee="
				+ fee + ", time=" + time + "]";
	}
	
	
}
