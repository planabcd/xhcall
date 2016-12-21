package org.cm.dao;

import java.util.List;

import org.cm.entity.Route;

public interface RouteDao {
	//ͨ���Թ����ķ�ʽ����ѯ���Լ����е�·��
	public List<Route> findById(int id);
	public List<Route> findAllId();
	public List<Route> findByName(String stationName);
	//վ���curd
	public int addStation(Route station);
	public int updatePrev(Route station);
	public int updateNext(Route station);
	//
	public int findMaxCircuitId();

}
