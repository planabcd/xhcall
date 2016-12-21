package cn.tx.xhc;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.tx.xhc.domain.Order;
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

public class OrderListActivity extends Activity {
	
	@ViewInject(R.id.lv_order_list)
	private ListView lv_order_list;
	
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;
	
	private MyAdapter mMyAdapter;
	private ArrayList<Order> mOrderList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_order_list);
		ViewUtils.inject(this);
		queryOrder();
	}
	@OnClick(R.id.ib_tools_back)
	public void back(View view){
		finish();
	}
	
	
	public void queryOrder() {
		pb_route_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.ORDER_LIST ;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			
			
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_route_query.setVisibility(View.INVISIBLE);
				mOrderList = new ArrayList<Order>();
				Type type = new TypeToken<ArrayList<Order>>() {}.getType();  
				mOrderList = new Gson().fromJson(responseInfo.result,type);
				/*try {
					JSONArray jsonArray = new JSONArray(responseInfo.result);
				} catch (JSONException e) {
					e.printStackTrace();
				}*/
				if(mOrderList!=null){
					if(mMyAdapter!=null){
						mMyAdapter.notifyDataSetChanged();
					}else{
						mMyAdapter = new MyAdapter();
						lv_order_list.setAdapter(mMyAdapter);
					}
				}else{
					UIUtils.showToast("网络错误,请检查网络");
				}
				
				
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				pb_route_query.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
		
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mOrderList.size();
		}

		@Override
		public Object getItem(int position) {
			return mOrderList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(OrderListActivity.this,R.layout.list_item_order_list, null);
			TextView tv_routename =(TextView) view.findViewById(R.id.tv_routename);
			RelativeLayout rl_item_route = (RelativeLayout) view.findViewById(R.id.rl_item_route);
			Order order = mOrderList.get(position);
			new Date(order.getStartTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String formatDateStr = sdf.format(new Date(order.getStartTime()));
			tv_routename.setText("路线: "+order.getFrom_station()+"-"+order.getTo_station()+"\n时间: "+formatDateStr);
			rl_item_route.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			return view;
		}
		
	}
	
	
	
	
	
}
