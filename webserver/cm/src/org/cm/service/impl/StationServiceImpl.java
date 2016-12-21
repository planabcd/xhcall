package org.cm.service.impl;

import org.cm.dao.RouteDao;
import org.cm.entity.Route;
import org.cm.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class StationServiceImpl implements StationService {
	
	@Autowired
	private RouteDao routeDao;
	public boolean addStation(int circuitId, String stationName, String prev,
			String next) {
		boolean result = true;
		Route station = new Route();
		//����վ�Ƿ����
		if(routeDao.findByName(stationName).size()!=0)
			return false;
		station.setStation_name(stationName);
		if(routeDao.findByName(prev).size()==0)
			return false;
		//�����һվ�Ƿ����
		station.setPrev(prev);
		if(routeDao.findByName(next).size()==0)
			return false;
		//�����һվ�Ƿ����
		station.setNext(next);
		station.setDirection("����");//Ĭ��
		station.setCircuit_id(circuitId);

		
		//�޸���һվ����һվ
		Route preStation = new Route();
		preStation.setCircuit_id(circuitId);
		preStation.setStation_name(prev);
		preStation.setNext(stationName);
		
		if(routeDao.updateNext(preStation)==0)
			return false;
		//���Ӹ�վ
		if(routeDao.addStation(station)==0)
			return false;
		//�޸���һվ����һվ
		Route nextStation = new Route();
		nextStation.setCircuit_id(circuitId);
		nextStation.setStation_name(next);
		nextStation.setPrev(stationName);
		if(routeDao.updatePrev(nextStation)==0)
			return false;
		
		
		return result;//����޸�ʧ�ܷ���false,��������ʧ�ܣ�ԭ������Ǹ�վ�Ѿ����ڣ�������һվ������һվ������
	}
	@Override
	public boolean addRoute(String route) {
		int circuitId = routeDao.findMaxCircuitId()+1;//��·�ţ��Լ����ã�Ĭ�����ֵ+1
		
		String [] stations = route.split(",");
		if(stations.length<=1)//��վ��С��1��ʱ�����������
			return false;
		//������վ
		Route station = new Route();
		station.setStation_name(stations[0]);
	    station.setNext(stations[1]);
	    station.setDirection("����");
	    station.setCircuit_id(circuitId);
	    //�����м�վ
	    if(routeDao.addStation(station)==0){
	    	try {
				throw new Exception("���վ��ʧ��");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return false;
	    }
	    //�������һվ
		for(int i =1;i<stations.length-1;i++){
		    station = new Route();
			station.setStation_name(stations[i]);
			station.setPrev(stations[i-1]);
		    station.setNext(stations[i+1]);
		    station.setDirection("����");
		    station.setCircuit_id(circuitId);
		    if(routeDao.addStation(station)==0){
		    	try {
					throw new Exception("���վ��ʧ��");
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	return false;
		    }	
		}
		station = new Route();
		station.setStation_name(stations[stations.length-1]);
	    station.setPrev(stations[stations.length-2]);
	    station.setDirection("����");
	    station.setCircuit_id(circuitId);
	    if(routeDao.addStation(station)==0){
	    	try {
				throw new Exception("���վ��ʧ��");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return false;
	    }
		return true;
	}

	public static void main(String[] args) {
		
		
		String route = "����,ͼ���,����,����";
		String [] stations = route.split(",");
		for(String s:stations)
			System.out.println(s+";");
		
		
	}

}
