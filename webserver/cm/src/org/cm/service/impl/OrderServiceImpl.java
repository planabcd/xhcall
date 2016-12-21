package org.cm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.cm.dao.OrderDao;
import org.cm.dao.RouteDao;
import org.cm.entity.Car;
import org.cm.entity.CountsOrder;
import org.cm.entity.Order;
import org.cm.entity.OrderResult;
import org.cm.entity.Route;
import org.cm.service.OrderService;
import org.cm.service.QueryRouteService;
import org.cm.service.QueryStationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private QueryStationInfoService queryService;
	@Autowired
	private QueryRouteService queryRouteService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private RouteDao routeDao;
	@Override
	public OrderResult order(String from, String to,int currentLine,Long startTime ) {
			//Ϊ��ȷ����ȫ���ٴζ��û���������в�ѯ
			OrderResult orderResult=new OrderResult();
		String[] route=queryRouteService.queryRoute(from, to, currentLine);
		if(route==null){//����from��to,currentLine��ѯʧ��
			orderResult.setFlag(false);
			System.out.println("������");
			return orderResult;//û�в�ѯ��ֱ�ӷ���
		}
		//�ܹ���ѯ�����������·��id��car���н��г��Ĳ���
		List<Car> carList=orderDao.findByCurrentId(currentLine);
		//��ų���id�ͳ���ǰλ�õ��ϳ��ص�ľ���
		HashMap<Integer,Integer> carIdMap=new HashMap<Integer,Integer>();
		//����һ�������������漴�������carId
		System.out.println("��ӡcarList");
		for(Car car:carList){
			System.out.println(car.getCurrent_location()+","+car.getDirection());
			//����·������վ��
			List<Route> routeList=routeDao.findById(currentLine);
			//����car��λ�ú����з�����з��䳵   
			if("����".equals(car.getDirection())){
//				System.out.println("���д�ӡ��");
		//�����Ƕ��ŵ����ַ�����.netx--->next--->next
				//���贫���վ��������·��ͼ�о�����
				List<String> list=new ArrayList<String>();
				
				//���ҳ��ӵ�ǰλ�õ�from��·��
				QueryRouteServiceImpl.repeatFind(car.getCurrent_location(), from, routeList, list);
				//����������а�����ʼվ�����ֹվ���򷵻ز�ѯ����·��ͼ
//				System.out.println("list��ӡ���Կ�ʼ");
//				for(String se:list){
//					System.out.println("se:"+se);
				if(list.contains(car.getCurrent_location())&&list.contains(from)){
					int count=0;
					for(;!list.get(count).equals(from);count++){
						;
					}
					//���ڼ����д�ŵ��Ǵӳ��ĵ�ǰλ�õ��յ�ĳ�
					carIdMap.put(car.getId(),count);
					System.out.println("����count:"+count);
			
				}
				
		
				
				
			}else{//���������ֵ����ŷ��� ����.prev----��prev-----��prev
				
				//���贫���վ��������·��ͼ�о�����
				List<String> list1=new ArrayList<String>();
				//���ҳ��ӵ�ǰλ�õ�from��·��
				QueryRouteServiceImpl.repeatFindDown(car.getCurrent_location(), from, routeList, list1);
				//����������а�����ʼվ�����ֹվ���򷵻ز�ѯ����·��ͼ
//				System.out.println("list��ӡ���Կ�ʼ");
//				for(String se:list){
//					System.out.println("se:"+se);
//				System.out.println("��ӡ���п�ʼ");
//				for(String s:list1){
//					System.out.println("list1:"+list1);
//				}
//				System.out.println("��ӡ���н���");
				if(list1.contains(car.getCurrent_location())&&list1.contains(from)){
					int count=0;
					for(;!list1.get(count).equals(from);count++){
						;
					}
					//���ڼ����д�ŵ��Ǵӳ��ĵ�ǰλ�õ��յ�ĳ�
					carIdMap.put(car.getId(),count);
					System.out.println("����count:"+count);
				}
				
		
//				System.out.println("��ӡ������");
				
				
			}
	
		}
//		System.out.println("��ӡcarList����");
		//��ʵ���У�����û�ж�order��id�ֶν���ֵ�����ã���Ϊ���������ݿ��в�����
		//�������ķ�ʽ������oder��id
		//���ݹ�����ʼվ�����ֹվ�㣬����Ҫ��Orderʵ�����һ����װ
		Order order=new Order();
		order.setFrom_station(from);
		order.setTo_station(to);
		order.setPayState("δ����");
		order.setCurrentId(currentLine);
		//���ÿ�ʼʱ��
		
//		order.setStartTime(date);
		//������ת��Ϊ��1970:0:0�ĺ���ֵ
		long start=startTime;
		//��ѯ����ʼվ�㵽��ֹվ���ʱ��
		int costTime=queryService.queryRunTime(from, to);
		long end=start+costTime*60*1000;
	
//		order.setEndTime(endTime);//���ý���ʱ��
//		order.setPay_state("δ����");
		//����õ�carId?
		//�����û�����������·id���в�ѯҪ��û�о���from��to�����еĳ���id,���
		//����������ͬ����ô���ѡ��һ������id�������
		
		
//		order.setCarId(carId);
		//����õ���ǰ��·
//		order.setCurrentLine(currentLine);
		//���������ʼվ�㵽��ֹվ��ķ���
		double fee=queryService.queryFee(from, to);
		order.setFee(fee);
		//��װʵ����ϣ�����װ�õ�ʵ�����ݣ�����service�����洢�����ݿ��С�
		return null;
		
	}
	
	public int getOrdersOfPayByDate(String date) {
		int result = 0;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date aimDate = sdf.parse(date);
//			long lDate = aimDate.getTime();//��ȡ��ѯʱ�䣬��ʱ��涨Ϊ0��0��0��
//			result = orderDao.getAllOrders(lDate);			
//		} catch (ParseException e) {
//			
//			e.printStackTrace();
//		}
		result = orderDao.getAllOrders(Long.parseLong(date));
		return result;
	}
//��ȡĳ��ʱ���ڵĳ˳�������
	@Override
	public int getPersonsPeriod(String startTime, String endTime) {
		long start = Long.valueOf(startTime);
		long end = Long.valueOf(endTime);
		Order order = new Order();
		order.setStartTime(start);
		order.setEndTime(end);
		return orderDao.getPersonsPeriod(order);
	}

	@Override
	public double getIncomePeriod(String startTime, String endTime) {
		long start = Long.valueOf(startTime);
		long end = Long.valueOf(endTime);
		Order order = new Order();
		order.setStartTime(start);
		order.setEndTime(end);
		List<Order> result = orderDao.getIncomePeriod(order);
		double sum = 0;
		for(Order o:result){
			sum += o.getFee();
		}
		return sum;
	}

	@Override
	public HashMap<String,Integer> getBusestOfThree(String startTime, String endTime) {
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		long start = Long.valueOf(startTime);
		long end = Long.valueOf(endTime);
		Order order = new Order();
		order.setStartTime(start);
		order.setEndTime(end);
		order.setId(3);//����id����ѯ��ҳ�Ĵ�С,������Ĭ��Ҫ��ǰ����
		List<CountsOrder> orders = orderDao.getBusestOfThree(order);
		System.out.println(orders.size());
		for(CountsOrder c:orders){
			result.put(c.getFrom_station(), c.getCounts());
		}
		return result;
	}

}
