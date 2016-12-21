package org.cm.controller;

import java.util.HashMap;

import org.cm.entity.RouteOtherFindInfo;
import org.cm.entity.RouteResult;
import org.cm.service.QueryRouteService;
import org.cm.service.QueryStationInfoService;
import org.cm.service.impl.QueryStationInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class RouteController {
	@Autowired
	private QueryRouteService queryRouteService;
	
	@Autowired
	private  QueryStationInfoService queryStationInfoService;
	
	/**
	 * ��ѯ������ʼվ������ֹվ������е�·����json���ݵ���ʽ����
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(value = "/queryRouteById.do",produces = "application/json;charset=utf-8")  
	@ResponseBody()
	public  RouteResult queryRouteById(String from ,String to,int id){
		RouteResult result=new RouteResult();
		result.setId(id);
		String [] s=(String[])queryRouteService.getStartAndEndStationById(from, to, id);
		result.setRouteFindById(s);
		
		return result;
	}
	/**
	 * ��ѯ ���е�·�߲����ؽ��
	 * @param from
	 * @param to
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryRoute.do",produces = "application/json;charset=utf-8")  
	@ResponseBody()
	public RouteResult query(String from ,String to){
		
		RouteResult result=new RouteResult();
		result.setAllRoute(queryRouteService.queryRoute(from,to)); 
		return result;
	}

	/**
 * ��ѯ��ǰվ�㣬���ص�ǰվ������ڵ�·��
 * @param stationName
 */
	@RequestMapping(value = "/queryStation.do",produces = "application/json;charset=utf-8")  
	@ResponseBody()
	public RouteOtherFindInfo queryStation(String stationName){
		RouteOtherFindInfo info=queryStationInfoService.queryStation(stationName);
		return info;
	
	}
	/**
	 * ��ѯ���ã�����վ��ĸ���������Ӧ�ļ۸��������������·���ܹ�����õص��������ѵķ���
	 * @param stationName
	 * @return
	 */
	@RequestMapping(value = "/queryFee.do",produces = "application/json;charset=utf-8")  
	@ResponseBody()
	public double  queryFee(String from,String to){
		double fee=queryStationInfoService.queryFee( from, to);
		return fee;
	
	}
	@RequestMapping(value = "/queryTime.do",produces = "application/json;charset=utf-8")  
	@ResponseBody()
	public int  queryTime(String from,String to){
		int costTime=queryStationInfoService.queryRunTime(from, to);
		return costTime;
	
	}
	
	@RequestMapping(value = "/queryStartAndEndStationById.do",produces = "application/json;charset=utf-8")  
	@ResponseBody()
	public String[]    getStartAndEndStationById(String from,String to,int id){
	String[] s=	queryRouteService.getStartAndEndStationById(from, to,id);
		return s;
	
	}
	
	
	

}
