package cn.tx.xhc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.tx.xhc.domain.OrderInsertReslult;
import cn.tx.xhc.global.Constant;
import cn.tx.xhc.utils.UIUtils;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuwan.customdatepicker.widget.CustomDatePicker;

public class OrderActivity extends Activity {
	
	@ViewInject(R.id.et_order_time)
	private EditText et_order_time;
	
	@ViewInject(R.id.et_route_search_from)
	private TextView et_route_search_from;
	
	@ViewInject(R.id.et_route_search_to)
	private TextView et_route_search_to;
	
	@ViewInject(R.id.et_order_route)
	private TextView et_order_route;
	
	@ViewInject(R.id.pb_order_query)
	private ProgressBar pb_order_query;

	private String mNow;

	private String mEnd;

	private SimpleDateFormat mSdf;

	private Date mCurrentDate;

	private String mRouteId;

	private CustomDatePicker mCustomDatePicker2;

	private String mFee;

	private String mTime;

	private SimpleDateFormat mSdf2;

	private String mTimeStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order);
		ViewUtils.inject(this);
		mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		mCurrentDate = new Date();
		mNow = mSdf.format(mCurrentDate);
        long endtime = mCurrentDate.getTime()+2*24*60*60;
        mEnd = mSdf.format(new Date(endtime));
        et_order_time.setText(mNow);
        initDateTimePicker();
		
	}
	
	public void initDateTimePicker(){
		mCustomDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
            	try {
					Date selectDateTime = mSdf.parse(time);
					Calendar selectCalendar = Calendar.getInstance();
					Calendar nowCalendar = Calendar.getInstance();
					selectCalendar.setTime(selectDateTime);
					nowCalendar.setTime(mCurrentDate);
					int selectDayOfMonth = selectCalendar.get(Calendar.DAY_OF_MONTH);
					int nowDayOfMonth = nowCalendar.get(Calendar.DAY_OF_MONTH);
					int selecthourOfDay = selectCalendar.get(Calendar.HOUR_OF_DAY);
					
					if(selectDayOfMonth!=nowDayOfMonth || selecthourOfDay<7 || selecthourOfDay>22){
						UIUtils.showToast("只能选择早上7点到晚上22点之间的时间!");
						return;
					}
				} catch (ParseException e) {
					et_order_time.setText(mNow);
					UIUtils.showToast("请选择合理的日期");
					return;
				}
            	
            	et_order_time.setText(time);
            }
        }, mNow, mEnd);
        mCustomDatePicker2.showSpecificTime(true); // 显示时和分
        mCustomDatePicker2.setIsLoop(false); // 允许循环滚动
	}
	@OnClick(R.id.et_order_time)
	public void getTime(View view){
		/*TimePickerDialog timePickerDialog = new TimePickerDialog(OrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
	         @Override
	         public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	        	 if(hourOfDay<7||hourOfDay>22){
	        		 UIUtils.showToast("这个时间段没车呢");
	        		 return;
	        	 }
	        	 String hourStr = hourOfDay+"";
	        	 String minuteStr = minute+"";
	        	 if(hourOfDay<10){
	        		 hourStr = "0"+hourOfDay;
	        	 }
	        	 if(minute<10){
	        		 minuteStr = "0"+minute;
	        	 }
	             et_order_time.setText(hourStr+":"+minuteStr);
	         }
	     //0,0指的是时间，true表示是否为24小时，true为24小时制
	     },0,0,true);
		timePickerDialog.setTitle("请选择预约上车时间");
		timePickerDialog.show();*/
		mCustomDatePicker2.show(et_order_time.getText().toString());
	}
	
	@OnClick(R.id.et_order_route)
	public void getRoute(View view){
		String from = et_route_search_from.getText().toString().trim();
		String to = et_route_search_to.getText().toString().trim();
		if(TextUtils.isEmpty(from) || TextUtils.isEmpty(to)){
			UIUtils.showToast("请输入完整的地址");
			return;
		}
		Intent intent = new Intent(this,SelectRouteActivity.class);
		intent.putExtra("from", from);
		intent.putExtra("to", to);
		startActivityForResult(intent, 10);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			mRouteId = data.getStringExtra("routeId");
			//String routeStations = data.getStringExtra("stations");
			String routeName = data.getStringExtra("routeName");
			String from = data.getStringExtra("from");
			String to = data.getStringExtra("to");
			mFee = data.getStringExtra("fee");
			mTime = data.getStringExtra("time");
			et_route_search_from.setText(from);
			et_route_search_to.setText(to);
			et_order_route.setText("路线--"+mRouteId+"("+routeName+")");
		}
	}
	
	
	
	@OnClick(R.id.iv_order_add)
	public void add(View view){
		final String time = et_order_time.getText().toString().trim();
		final String from = et_route_search_from.getText().toString().trim();
		final String to = et_route_search_to.getText().toString().trim();
//		final String from = "东门";
//		final String to = "图书馆";
		
		mSdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		try {
			mTimeStr = mSdf2.parse(time).getTime()+"";
		} catch (ParseException e) {
			mTimeStr = new Date().getTime()+"";
			e.printStackTrace();
		}
		
		
		String routeName = et_order_route.getText().toString().trim();
		if(TextUtils.isEmpty(time)){
			UIUtils.showToast("请选择预约时间");
			return;
		}
		if(TextUtils.isEmpty(routeName)){
			UIUtils.showToast("请输入选择路线");
			return;
		}
		new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText(from+"--"+to)
        .setContentText(time+"\n"+routeName
        		+"\n￥ "+mFee+"\n"+mTime+"min"
        		)
        .setCancelText("取消")
        .setConfirmText("确认")
        .showCancelButton(true)
        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
           	 sDialog.dismiss();
            }
        })
        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
            	String queryUrl = Constant.ORDER_INSERT + "?from="+from+"&to="+to+"&currentLine="+mRouteId+"&startTime="+mTimeStr;
        		//TODO
            	System.out.println("insert Order queryUrl:"+queryUrl);
            	HttpUtils httpUtils = new HttpUtils();
        		pb_order_query.setVisibility(View.VISIBLE);
        		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
        			@Override
        			public void onSuccess(ResponseInfo<String> responseInfo) {
        				pb_order_query.setVisibility(View.INVISIBLE);
        				String result = responseInfo.result;
        				if(TextUtils.isEmpty(result)){
        					UIUtils.showToast("订单提交失败,请稍后重试");
        				}else{
        					Gson gson = new Gson();
        					OrderInsertReslult orderInsertResult = gson.fromJson(result, OrderInsertReslult.class);
        					if(orderInsertResult!=null && orderInsertResult.isFlag()){
        						 new SweetAlertDialog(OrderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
        	                     .setTitleText("预约成功,准备上车~")
        	                     .setContentText("车辆编号:"+orderInsertResult.getCarId())
        	                     .show();
        					}else{
        						UIUtils.showToast("订单提交失败,请稍后重试");
        					}
        				}
        			}
        			@Override
        			public void onFailure(HttpException error, String msg) {
        				pb_order_query.setVisibility(View.INVISIBLE);
        				UIUtils.showToast("网络错误,请检查网络");
        			}
        		});
            	sDialog.dismiss();
            }
        })
        .show();
		
		
		
	}
	
	@OnClick(R.id.iv_clear_to)
	public void clearTo(View view){
		et_route_search_to.setText("");
		et_order_route.setText("");
	}
	@OnClick(R.id.iv_clear_from)
	public void clearFrom(View view){
		et_route_search_from.setText("");
		et_order_route.setText("");
	}
	
	/*@OnClick(R.id.iv_clear_time)
	public void clearTime(View view){
		et_order_time.setText("");
	}
	*/
	@OnClick(R.id.iv_clear_route)
	public void clearRoute(View view){
		et_order_route.setText("");
	}
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
	
	
}
