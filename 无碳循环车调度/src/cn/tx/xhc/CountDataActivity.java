package cn.tx.xhc;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.tx.xhc.global.Constant;
import cn.tx.xhc.utils.UIUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuwan.customdatepicker.widget.CustomDatePicker;
public class CountDataActivity extends Activity {
	
	@ViewInject(R.id.et_route_search_from)
	private EditText et_route_search_from;
	
	@ViewInject(R.id.et_route_search_to)
	private EditText et_route_search_to;
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;

	private SimpleDateFormat mSdf;

	private Date mCurrentDate;

	private String mNow;

	private CustomDatePicker mCustomDatePicker;

	private CustomDatePicker mCustomDatePicker2;

	private SimpleDateFormat mSdf2;
	
	private final static int ACTION_PERSONS_NUM = 1;
	private final static int ACTION_TOTAL_MONEY = 2;
	private final static int ACTION_STATIONS_RANK = 3;

	private String mContentTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_countdata);
		ViewUtils.inject(this);
		mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		mCurrentDate = new Date();
		mNow = mSdf.format(mCurrentDate);
		et_route_search_to.setText(mNow.split(" ")[0]);
		et_route_search_from.setText(mNow.split(" ")[0]);
		initDatePicker();
		
	}
	public void initDatePicker(){
		mCurrentDate = new Date();
		mNow = mSdf.format(mCurrentDate);
		mCustomDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
            	et_route_search_from.setText(time.split(" ")[0]);
            }
        }, "2016-01-01 00:00", mNow);
        mCustomDatePicker.showSpecificTime(false); // 显示时和分
        mCustomDatePicker.setIsLoop(false); // 允许循环滚动
        
        mCustomDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
        	@Override
        	public void handle(String time) { // 回调接口，获得选中的时间
        		et_route_search_to.setText(time.split(" ")[0]);
        	}
        }, "2016-01-01 00:00", mNow);
        mCustomDatePicker2.showSpecificTime(false); // 显示时和分
        mCustomDatePicker2.setIsLoop(false); // 允许循环滚动
	}
	
	
	
	public void getCountData(final int actionType){
		String from = et_route_search_from.getText().toString().trim();
		String to = et_route_search_to.getText().toString().trim();
		mSdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		String fromStr;
		String toStr;
		try {
			fromStr = mSdf2.parse(from).getTime()+"";
			toStr = mSdf2.parse(to).getTime()+"";
		} catch (ParseException e) {
			fromStr = "1";
			toStr = new Date().getTime()+"";
			e.printStackTrace();
		}
		String queryUrl = Constant.COUNT_DATA_PERSONS_NUM + "?startTime="+fromStr+"&endTime="+toStr;
		mContentTitle = "当前乘车人次";
		switch(actionType){
			case ACTION_PERSONS_NUM:
				System.out.println("personnum");
				queryUrl = Constant.COUNT_DATA_PERSONS_NUM + "?startTime="+fromStr+"&endTime="+toStr;
				mContentTitle = "当前乘车人次";
				break;
			case ACTION_TOTAL_MONEY:
				System.out.println("money");
				queryUrl = Constant.COUNT_DATA_TOTAL_MONEY + "?startTime="+fromStr+"&endTime="+toStr;
				mContentTitle = "当前营业收入";
				break;
			case ACTION_STATIONS_RANK:
				System.out.println("rank");
				queryUrl = Constant.COUNT_DATA_STATIONS_RANK + "?startTime="+fromStr+"&endTime="+toStr;
				mContentTitle = "热门站点 TOP3";
				break;
			default:
			break;
		}
		HttpUtils httpUtils = new HttpUtils();
		pb_route_query.setVisibility(View.VISIBLE);
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_route_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if(TextUtils.isEmpty(result)){
					UIUtils.showToast("请确认输入的地址是否正确,服务器暂无数据");
				}else{
					if(actionType==ACTION_STATIONS_RANK){
						Gson gson = new Gson();
						StringBuilder str = new StringBuilder();
						Type type = new TypeToken<HashMap<String,Integer>>(){}.getType();
						HashMap<String,Integer> resultMap = gson.fromJson(result, type);
						for(Entry<String,Integer> en : resultMap.entrySet()){
							String key = en.getKey();
							Integer value = en.getValue();
							str.append("\n"+key+"-"+value);
						}
						result = str.toString();
					}
					new SweetAlertDialog(CountDataActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(mContentTitle)
                    .setContentText(result)
                    .show();
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				pb_route_query.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
	}
	
	@OnClick(R.id.tv_countdata_person_num)
	public void personNum(View view){
		getCountData(ACTION_PERSONS_NUM);
	}
	
	@OnClick(R.id.tv_countdata_money_total)
	public void totalMoney(View view){
		getCountData(ACTION_TOTAL_MONEY);
	}
	@OnClick(R.id.tv_countdata_station)
	public void getStations(View view){
		getCountData(ACTION_STATIONS_RANK);
		
	}
	
	
	@OnClick(R.id.iv_clear_to)
	public void clearTo(View view){
		et_route_search_to.setText("");
	}
	@OnClick(R.id.iv_clear_from)
	public void clearFrom(View view){
		et_route_search_from.setText("");
	}
	
	
	@OnClick(R.id.ib_tools_back)
	public void back(View view){
		finish();
	}
	
	
	@OnClick(R.id.et_route_search_from)
	public void getTimeFrom(View view){
        mCustomDatePicker.show(et_route_search_from.getText().toString());
	}
	
	@OnClick(R.id.et_route_search_to)
	public void getTimeTo(View view){
		mCustomDatePicker2.show(et_route_search_from.getText().toString());
	}
	
}
