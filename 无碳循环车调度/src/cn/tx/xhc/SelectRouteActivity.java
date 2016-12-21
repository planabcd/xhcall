package cn.tx.xhc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.tx.xhc.domain.RouteResult;
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

public class SelectRouteActivity extends Activity{
	
	@ViewInject(R.id.lv_route)
	private ListView lv_route;
	
	@ViewInject(R.id.et_route_search_from)
	private TextView et_route_search_from;
	
	@ViewInject(R.id.et_route_search_to)
	private TextView et_route_search_to;
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;
	private HashMap<String, ArrayList<String>> mAllRoute;
	private HashMap<String, Double> mAllFee;
	private HashMap<String, Double> mAllTime;
	
	private MyAdapter mMyAdapter;
	private ArrayList<String> mRouteIds;

	private String mFrom;

	private String mTo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route_select);
		ViewUtils.inject(this);
		Intent it = getIntent();
		mFrom = it.getStringExtra("from");
		mTo = it.getStringExtra("to");
		et_route_search_from.setText(mFrom);
		et_route_search_to.setText(mTo);
		if(!TextUtils.isEmpty(mFrom) && !TextUtils.isEmpty(mTo)){
			queryRouteById("",mFrom,mTo);
		}
	}
	
	@OnClick(R.id.iv_clear_to)
	public void clearTo(View view){
		et_route_search_to.setText("");
	}
	@OnClick(R.id.iv_clear_from)
	public void clearFrom(View view){
		et_route_search_from.setText("");
	}
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
		@OnClick(R.id.iv_search)
		public void search(View view){
		mFrom = et_route_search_from.getText().toString().trim();
		mTo = et_route_search_to.getText().toString().trim();
		if(TextUtils.isEmpty(mFrom) || TextUtils.isEmpty(mTo)){
			UIUtils.showToast("请输入完整的地址");
			return;
		}else{
			queryRouteById("",mFrom,mTo);
		}
	}
	
	public void queryRouteById(String id,String from,String to) {
		pb_route_query.setVisibility(View.VISIBLE);
//		from = "15号楼";
//		to = "东门";
		String queryUrl = Constant.ROUTE_QUERY_ALL + "?from="+from+"&to="+to;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_route_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				Gson gson = new Gson();
				RouteResult routeResult = gson.fromJson(result, RouteResult.class);
				boolean hasRoute = false;
				mAllRoute = routeResult.getAllRoute();
				mAllFee = routeResult.getAllFee();
				mAllTime = routeResult.getAllTime();
				if(mAllRoute==null){
					//没有数据
					UIUtils.showToast("请确认输入的地址是否正确,服务器暂无数据");
					return;
				}
				mRouteIds = new ArrayList<String>();
				for(Entry<String,ArrayList<String>> en : mAllRoute.entrySet()){
					String routeId = en.getKey();
					ArrayList<String> stations = en.getValue();
					if(stations==null || stations.isEmpty()){
						continue;
					}else{
						mRouteIds.add(routeId);
						hasRoute = true;
					}
				}
				if(hasRoute){
					if(mMyAdapter==null){
						mMyAdapter = new MyAdapter();
						lv_route.setAdapter(mMyAdapter);
					}else{
						mMyAdapter.notifyDataSetChanged();
					}
				}else{
					//没有数据
					UIUtils.showToast("请确认输入的地址是否正确,服务器暂无数据");
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
			return mRouteIds.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(SelectRouteActivity.this,R.layout.list_item_route, null);
			TextView tv_routename =(TextView) view.findViewById(R.id.tv_routename);
			RelativeLayout rl_item_route = (RelativeLayout) view.findViewById(R.id.rl_item_route);
			final String routeId = mRouteIds.get(position);
			final ArrayList<String> stations = mAllRoute.get(routeId);
			final String title = "路线-"+routeId+"("+stations.get(0)+"---"+stations.get(stations.size()-1)+")";
			tv_routename.setText(title);
			rl_item_route.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					StringBuilder str = new StringBuilder();
					for(int i=0; i!=stations.size(); i++){
						str.append(stations.get(i)+"\n");
					}
					final String strStations = str.toString();
					Double fee = null;
					Double time = null;
					if(mAllFee!=null && mAllTime!=null){
						fee = mAllFee.get(routeId);
						time = mAllTime.get(routeId);
					}
					final String feestr = fee==null?"0.0":fee+"";
					final String timestr = time==null?"0.0":time+"";
					/*new SweetAlertDialog(SelectRouteActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("路线-"+routeId)
                    .setContentText(str.toString())
                    .show();*/
					 new SweetAlertDialog(SelectRouteActivity.this, SweetAlertDialog.WARNING_TYPE)
                     .setTitleText("路线-"+routeId+"(￥"+feestr+","+timestr+"分钟)")
                     .setContentText(str.toString())
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
                             sDialog.setTitleText("已经选择路线-"+routeId)
                                     .setConfirmText("OK")
                                     .setCancelClickListener(null)
                                     .setConfirmClickListener(null)
                                     .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                             Intent intent = new Intent();
                             intent.putExtra("routeId", routeId);
                             intent.putExtra("fee", feestr);
                             intent.putExtra("time", timestr);
                             intent.putExtra("stations", strStations);
                             intent.putExtra("routeName", stations.get(0)+"---"+stations.get(stations.size()-1));
                             intent.putExtra("from", mFrom);
                             intent.putExtra("to", mTo);
                             sDialog.dismiss();
                             setResult(0,intent);
                             finish();
                         }
                     })
                     .show();
					
					
					
					
				}
			});
			return view;
		}
		
	}
}
