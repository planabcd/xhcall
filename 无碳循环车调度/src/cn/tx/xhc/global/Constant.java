package cn.tx.xhc.global;

public class Constant {
	public static String URL_SERVER = "http://123.206.21.81:8080";
	public static String URL_REGISTER_USER = URL_SERVER+"/cm/regist.do";
	public static String URL_LOGIN_USER = URL_SERVER+"/cm/login.do";
	
	public static String USERTYPE_ADMIN = "Admin";
	public static String USERTYPE_NOT_ADMIN = "Customer";
	
	public static String ROUTE_QUERY_ALL = URL_SERVER+"/cm/queryRoute.do";
	public static String ROUTE_QUERY_ALL_STATION = URL_SERVER+"/cm/queryAllStation.do";
	
	public static String FEE_QUERY_ALL = URL_SERVER+"/cm/queryFee.do";
	public static String TIME_QUERY_ALL = URL_SERVER+"/cm/queryTime.do";
	public static String ORDER_INSERT = URL_SERVER+"/cm/order.do";
	public static String ORDER_LIST = URL_SERVER+"/cm/findAllOrder.do";
	
	public static String COUNT_DATA_PERSONS_NUM = URL_SERVER+"/cm/getPersonsPeriod.do";
	public static String COUNT_DATA_TOTAL_MONEY = URL_SERVER+"/cm/getIncomePeriod.do";
	public static String COUNT_DATA_STATIONS_RANK = URL_SERVER+"/cm/getBusestOfThree.do";
	
	public static String ROUTE_ADD = URL_SERVER+"/cm/addRoute.do";
	
	
	public static String CAR_ADD = URL_SERVER+"/cm/addCar.do";
	public static String CAR_DELETE = URL_SERVER+"/cm/removeCar.do";
	public static String CAR_DISTRIBUTE = URL_SERVER+"/cm/distributeRoteToCar.do";
	public static String CAR_LIST_BY_RUNNINGTYPE = URL_SERVER+"/cm/getCarsInfo.do";
	
	public static String PHONE_QUERY_ADDRESS = "http://apicloud.mob.com/v1/mobile/address/query";
	public static String LUCKY_QUERY = "http://apicloud.mob.com/appstore/lucky/mobile/query";
	
	public static String APPKEY_PHONE_QUERY_ADDRESS = "1a0657cba0088";
	

}
