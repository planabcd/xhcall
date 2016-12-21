package org.cm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cm.dao.RouteDao;
import org.cm.entity.Route;
import org.cm.service.QueryRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class QueryRouteServiceImpl implements QueryRouteService{
	@Autowired
	private RouteDao routeDao;
	/**
	 * ��ѯ����·�ߵ�վ��
	 */
	@Override
	public HashMap<Integer,String[]> queryRoute(String from, String to) {
		/*��ѯ������ʼվ�㵽��ֹվ�����е�·�߲������ַ����������ʽ��������
		�˴����õ���ʽΪ������ѯÿһ��·�ߣ�Ȼ��ֱ��ÿ��վ�����Ϣ��������
		*/
		//��ѯ���ݿ������е�·��idȻ�����·��id����ѯÿһ��·�ߵĽ���������ڼ��ϵ���
		List<Route> list=routeDao.findAllId();
		List<Integer> allId=new ArrayList<Integer>();
		if(list.size()>0){
		for(Route route:list){
			if(!allId.contains(route.getCircuit_id())){
				allId.add(route.getCircuit_id());
			}
		}
		}
		//����·��id�ҳ����е�·��
		Map<Integer,String[]> result=new HashMap<Integer,String[]>();
		for(int i=0;i<allId.size();i++){
			String[] str=getStartAndEndStationById(from, to, allId.get(i));
			
		result.put(allId.get(i), str);
	       
		}
		
		return (HashMap) result;
	}
	
	//��ѯ��һ����·�ϵ����� վ��
	public String[] queryRoute(String from,String to ,int id){
		if(from==null||to==null){
			return null;
		}
		if("".equals(from)||"".equals(to)){
			return null;
		}
		List<Route> routeList=routeDao.findById(id);
		System.out.println("--------·�ߴ�ӡ------------");
		//���贫���վ��������·��ͼ�о�����
		List<String> list=new ArrayList<String>();
		repeatFind(from, to, routeList, list);
		String[] s=null;
		//���������ҵ�from ��to�����е�վ�㱣�浽�ַ���������
		List<String> resultList=new ArrayList<String>();
		//����������а�����ʼվ�����ֹվ���򷵻ز�ѯ����·��ͼ
		if(list.contains(from)&&list.contains(to)){
			System.out.println("������ʼ");
			s = list.toArray(new String[]{});   
			System.out.println("s:"+s);
			for(int i=0;i<s.length;i++){//���������õ���ʼ·�ߺ���ֹ·�ߵ�����
				resultList.add(s[i]);//��������ϸ�ֵ��
				if(s[i].equals(to)){
					break;
				}
				
			}
			return resultList.toArray(new String[]{});
		}
		System.out.println("----------�෴�����ӡ·��");
		//�õ���վ����෴��������е�վ��
		List<String> list2=new ArrayList<String>();
		repeatFindDown(from, to, routeList, list2);
		if(list2.contains(from)&&list2.contains(to)){
			System.out.println("������ʼ2");
			s = list2.toArray(new String[]{});   
			System.out.println("s:"+s);
			int j=0;
			for(int i=0;i<s.length;i++){//���������õ���ʼ·�ߺ���ֹ·�ߵ�����
				resultList.add(s[i]);//��������ϸ�ֵ��
				if(s[i].equals(to)){
					break;
				}
				
			}
		}
		
		return resultList.toArray(new String[]{});
	}
	/**
	 * ����һ���ݹ�Ĳ�����������ÿһ����·�ϵ����е�վ��
	 * @param from ��ʼվ��
	 * @param to ����վ��
	 * @param routeList �����ݿ��в�ѯ�õ������е�ÿһ��id����Ӧ��վ�㼯��
	 * @param str ��������һ��վ����ַ�����
	 */
	public static void  repeatFind(String from,String to,List<Route> routeList,List<String> 
		list	){
		for(Route route:routeList){
			//�ȱ���������·
			if(from.equals(route.getStation_name())){
				list.add(route.getStation_name());
				if(route.getNext()!=null)
				repeatFind(route.getNext(),to,routeList,list);
			}
		}
		
	}
	/**
	 * ���õݹ���õķ�ʽ���Ҵ���ʼվ�㵽��ֹ�յ��·��
	 */
	public static void  repeatFindDown(String from,String to,List<Route> routeList,List<String> 
	list	){
			for(Route route:routeList){
				//�ȱ���������·
				if(from.equals(route.getStation_name())){
					list.add(route.getStation_name());
					if(route.getPrev()!=null)
						repeatFindDown(route.getPrev(),to,routeList,list);
				}
			}
			
		}
	
	/**
	 * ����id��ѯ������·����ʼվ�����ֹվ��
	 * @param id�����id�����ݿ���һ������
	 */
	public String[]  getStartAndEndStationById(String from,String to,int id){
		System.out.println("����÷���");
		String[] s =null;
		//����id��ѯ��������·�����е�վ��ļ���
		List<Route> routeList=routeDao.findById(id);
		//��ѯ������·�й��Ƿ����from��to����վ��������������صĽ��Ϊnull
		List<String> listRoute=new ArrayList<String>();
		for(Route route:routeList){
			listRoute.add(route.getStation_name());
		}
		if(!(listRoute.contains(from)&&listRoute.contains(to))){
			System.out.println("�ѵ������������");
			return null;
		}
	
		//�����id����·�ϰ������������վ�㣬����from��to��λ���ҵ�������·����ʼվ�����ֹ
		//վ��
		
		List<String> list=new ArrayList<String>();
		repeatFind(from, to, routeList, list);
		//�õ���վ����෴��������е�վ��
		List<String> list2=new ArrayList<String>();
		repeatFindDown(from, to, routeList, list2);
//		System.out.println("-============================");
//		for(String s2:list2){
//			System.out.println(s2);
//		}
//		System.out.println("-============================");
		//list1�����˴���ʼվ�㵽�����յ������·�ߣ�list2�����˴�����վ�㵽���е�·��
		if(list.contains(from)&&list.contains(to)){//�ҵ���
			//s = list.toArray(new String[]{});   //�õ�����ʼվ�㵽��·�յ��·��ͼ
			Collections.reverse(list2);
			if(list.size()>1){
				for(int i=1;i<list.size();i++){
					list2.add(list.get(i));
				}
			}
//			System.out.println("��ӡlist2");
//			for(String s1:list2){
//				System.out.println(s1);
//			}
//			System.out.println("��ӡlist2");
			return list2.toArray(new String[]{});
		}
		if(list2.contains(from)&&list2.contains(to)){//�ҵ���
			Collections.reverse(list);
			if(list2.size()>1){
				for(int i=1;i<list2.size();i++){
					list.add(list2.get(i));
				}
			}
			return list.toArray(new String[]{});
//			System.out.println("��ӡlist");
//			for(String s1:list){
//				System.out.println(s1);
//			}
//			System.out.println("��ӡlist");
		}
		
	
		
	
		
		return null;
		
		}

	@Override
	public HashMap<Integer, String[]> queryRouteFromTo(String from, String to) {
		/*��ѯ������ʼվ�㵽��ֹվ�����е�·�߲������ַ����������ʽ��������
		�˴����õ���ʽΪ������ѯÿһ��·�ߣ�Ȼ��ֱ��ÿ��վ�����Ϣ��������
		*/
		//��ѯ���ݿ������е�·��idȻ�����·��id����ѯÿһ��·�ߵĽ���������ڼ��ϵ���
		List<Route> list=routeDao.findAllId();
		List<Integer> allId=new ArrayList<Integer>();
		if(list.size()>0){
		for(Route route:list){
			if(!allId.contains(route.getCircuit_id())){
				allId.add(route.getCircuit_id());
			}
		}
		}
		//����·��id�ҳ����е�·��
		Map<Integer,String[]> result=new HashMap<Integer,String[]>();
		for(int i=0;i<allId.size();i++){
			String[] str=queryRoute(from, to, allId.get(i));
			
		result.put(allId.get(i), str);
	       
		}
		
		return (HashMap) result;
	}

		
}
