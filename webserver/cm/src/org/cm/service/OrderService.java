package org.cm.service;

import java.util.HashMap;

import org.cm.entity.OrderResult;

public interface OrderService {
/**
 * Ϊ�˷����û���Ҫ�ٴεĴ���from��to��ʱ�������ٴζ�current��·�����ж�
 * @param from
 * @param to
 * @param currentLine
 */
	public OrderResult order(String from,String to,int currentLine,Long startTime);
	
	public int getOrdersOfPayByDate(String date);
	
	public int getPersonsPeriod(String startTime,String endTime);
	
	public double getIncomePeriod(String startTime,String endTime);
	
	public HashMap<String,Integer> getBusestOfThree(String startTime,String endTime);
}
