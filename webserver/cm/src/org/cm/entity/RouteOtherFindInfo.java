package org.cm.entity;

import java.io.Serializable;
/**
 *���ʵ����Ҫ��������װ��ѯվ������Ӧ�Ľ��
 * @author sony
 *
 */
public class RouteOtherFindInfo implements Serializable{
	private String stationName;
	private Integer[] routeInIds;
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Integer[] getRouteInIds() {
		return routeInIds;
	}
	public void setRouteInIds(Integer[] routeInIds) {
		this.routeInIds = routeInIds;
	}
	

}
