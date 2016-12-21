package org.cm.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cm.dao.RouteDao;
import org.cm.entity.Route;
import org.cm.entity.RouteOtherFindInfo;
import org.cm.service.QueryRouteService;
import org.cm.service.QueryStationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class QueryStationInfoServiceImpl implements QueryStationInfoService{
	//����ÿһվ��Ʊ��Ϊ1Ԫ
	@Value("#{fee.fee}")  
	private    double FEE;
	@Value("#{fee.cost}")
	private int costTime;//����ÿһ��վ���ʱ��Ϊ5����
	
	@Autowired
	private RouteDao routeDao;
	@Autowired
	private QueryRouteService queryRouteService;
	@Override
	public RouteOtherFindInfo queryStation(String stationName) {
		//��ѯվ�������Լ����ڵ�·��
		List<Route> routeList=routeDao.findByName(stationName);
		//��routeList����ȡ��id
		List<Integer> ids=new ArrayList<Integer>();
	if(routeList!=null){
		for(int i=0;i<routeList.size();i++){
			ids.add(routeList.get(i).getCircuit_id());
		}
	}
		
		RouteOtherFindInfo info=new RouteOtherFindInfo();
		info.setStationName(stationName);
		
		info.setRouteInIds(ids.toArray( new Integer[]{}));
		
		return info;
	}

	@Override
	public int queryStationNum(String from, String to) {
	Map<Integer,String[]> routeMap=	queryRouteService.queryRouteFromTo(from, to);
	System.out.println("daozhele1");
	if(routeMap==null){//��ʾ�����·�ߴ���
		System.out.println("map is null");
	return 0;
	}
	//���õ���������map�ҳ����е�map�и���̵�һ��·��
	Iterator iter = routeMap.entrySet().iterator();
	int leastStationNum=0;
	int i=0;
	int count=0;//������֤��һ�θ�ֵ����0������վ��
	while(iter.hasNext()){//�������е�·��
		System.out.println("i:"+(++i));
	 Entry  entry=(Entry) iter.next();
	 String[] result=(String[]) entry.getValue();
	 System.out.println("luxaindesize:"+result.length);
	 
	 if(result.length>0&&count==0){
		 //��֤�õ������е�
		 leastStationNum=result.length;
		 count++;
	 }
	 if(result.length>0&&count>0){
		 if(leastStationNum>result.length){
			 leastStationNum=result.length;
		 }
		 
	 }
	 
	
	}
	 System.out.println("leastStationNum"+leastStationNum);
		return leastStationNum;
	}

	@Override
	public double queryFee(String from, String to) {
		int distance=queryStationNum( from,  to);
		if(distance>=1){
		double resultFee=(distance-1)*FEE;
		return resultFee;
		}else{
			return 0;
		}
		
		
	}
	@Override
	public int queryRunTime(String from, String to) {
		int distance=queryStationNum( from,  to);
		if(distance>=1){
		int allCostTime=(distance-1)*costTime;
		return allCostTime;
		}else{
			return 0;
		}
		
		
	}
	
	
	

	public double getFEE() {
		return FEE;
	}

	public void setFEE(double fEE) {
		FEE = fEE;
	}

	public int getCostTime() {
		return costTime;
	}

	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}





}
