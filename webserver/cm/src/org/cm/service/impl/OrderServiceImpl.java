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
			//为了确保安全，再次对用户的输入进行查询
			OrderResult orderResult=new OrderResult();
		String[] route=queryRouteService.queryRoute(from, to, currentLine);
		if(route==null){//根据from，to,currentLine查询失败
			orderResult.setFlag(false);
			System.out.println("返回了");
			return orderResult;//没有查询到直接返回
		}
		//能够查询到，则根据线路的id在car表中进行车的查找
		List<Car> carList=orderDao.findByCurrentId(currentLine);
		//存放车的id和车当前位置到上车地点的距离
		HashMap<Integer,Integer> carIdMap=new HashMap<Integer,Integer>();
		//定义一个集合用来保存即将分配的carId
		System.out.println("打印carList");
		for(Car car:carList){
			System.out.println(car.getCurrent_location()+","+car.getDirection());
			//该线路的所有站点
			List<Route> routeList=routeDao.findById(currentLine);
			//根据car的位置和运行方向进行分配车   
			if("上行".equals(car.getDirection())){
//				System.out.println("上行打印了");
		//上行是东门到西街方向东门.netx--->next--->next
				//假设传入的站点在整个路线图中均存在
				List<String> list=new ArrayList<String>();
				
				//查找车从当前位置到from的路线
				QueryRouteServiceImpl.repeatFind(car.getCurrent_location(), from, routeList, list);
				//如果上行线中包含起始站点和终止站点则返回查询到的路线图
//				System.out.println("list打印测试开始");
//				for(String se:list){
//					System.out.println("se:"+se);
				if(list.contains(car.getCurrent_location())&&list.contains(from)){
					int count=0;
					for(;!list.get(count).equals(from);count++){
						;
					}
					//现在集合中存放的是从车的当前位置到终点的车
					carIdMap.put(car.getId(),count);
					System.out.println("上行count:"+count);
			
				}
				
		
				
				
			}else{//下行是西街到东门方向 西街.prev----》prev-----》prev
				
				//假设传入的站点在整个路线图中均存在
				List<String> list1=new ArrayList<String>();
				//查找车从当前位置到from的路线
				QueryRouteServiceImpl.repeatFindDown(car.getCurrent_location(), from, routeList, list1);
				//如果上行线中包含起始站点和终止站点则返回查询到的路线图
//				System.out.println("list打印测试开始");
//				for(String se:list){
//					System.out.println("se:"+se);
//				System.out.println("打印下行开始");
//				for(String s:list1){
//					System.out.println("list1:"+list1);
//				}
//				System.out.println("打印下行结束");
				if(list1.contains(car.getCurrent_location())&&list1.contains(from)){
					int count=0;
					for(;!list1.get(count).equals(from);count++){
						;
					}
					//现在集合中存放的是从车的当前位置到终点的车
					carIdMap.put(car.getId(),count);
					System.out.println("下行count:"+count);
				}
				
		
//				System.out.println("打印下行了");
				
				
			}
	
		}
//		System.out.println("打印carList结束");
		//在实体中，我们没有对order的id字段进行值的设置，因为我们在数据库中采用了
		//自增长的方式来设置oder的id
		//传递过来起始站点和终止站点，我们要对Order实体进行一个封装
		Order order=new Order();
		order.setFrom_station(from);
		order.setTo_station(to);
		order.setPayState("未付款");
		order.setCurrentId(currentLine);
		//设置开始时间
		
//		order.setStartTime(date);
		//将日期转换为到1970:0:0的毫秒值
		long start=startTime;
		//查询从起始站点到终止站点的时间
		int costTime=queryService.queryRunTime(from, to);
		long end=start+costTime*60*1000;
	
//		order.setEndTime(endTime);//设置结束时间
//		order.setPay_state("未付款");
		//如何拿到carId?
		//根据用户传过来的线路id进行查询要还没有经过from到to的所有的车的id,如果
		//两个距离相同，那么随机选择一个车的id进行添加
		
		
//		order.setCarId(carId);
		//如何拿到当前线路
//		order.setCurrentLine(currentLine);
		//计算出从起始站点到终止站点的费用
		double fee=queryService.queryFee(from, to);
		order.setFee(fee);
		//封装实体完毕，将封装好的实体内容，调用service方法存储到数据库中。
		return null;
		
	}
	
	public int getOrdersOfPayByDate(String date) {
		int result = 0;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Date aimDate = sdf.parse(date);
//			long lDate = aimDate.getTime();//获取查询时间，将时间规定为0点0分0秒
//			result = orderDao.getAllOrders(lDate);			
//		} catch (ParseException e) {
//			
//			e.printStackTrace();
//		}
		result = orderDao.getAllOrders(Long.parseLong(date));
		return result;
	}
//获取某段时间内的乘车人人数
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
		order.setId(3);//利用id来查询分页的大小,本函数默认要求前三条
		List<CountsOrder> orders = orderDao.getBusestOfThree(order);
		System.out.println(orders.size());
		for(CountsOrder c:orders){
			result.put(c.getFrom_station(), c.getCounts());
		}
		return result;
	}

}
