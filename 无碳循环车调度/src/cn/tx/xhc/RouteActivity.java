package cn.tx.xhc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.app.Activity;
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

public class RouteActivity extends Activity{
	
	@ViewInject(R.id.lv_route)
	private ListView lv_route;
	
	@ViewInject(R.id.et_route_search_from)
	private TextView et_route_search_from;
	
	@ViewInject(R.id.et_route_search_to)
	private TextView et_route_search_to;
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;
	private HashMap<String, ArrayList<String>> mAllRoute;
	private MyAdapter mMyAdapter;
	private ArrayList<String> mRouteIds;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_route);
		ViewUtils.inject(this);
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
		String from = et_route_search_from.getText().toString().trim();
		String to = et_route_search_to.getText().toString().trim();
		if(TextUtils.isEmpty(from) || TextUtils.isEmpty(to)){
			UIUtils.showToast("请输入完整的地址");
			return;
		}else{
			queryRouteById("",from,to);
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
			
			View view = View.inflate(RouteActivity.this,R.layout.list_item_route, null);
			TextView tv_routename =(TextView) view.findViewById(R.id.tv_routename);
			RelativeLayout rl_item_route = (RelativeLayout) view.findViewById(R.id.rl_item_route);
			final String routeId = mRouteIds.get(position);
			final ArrayList<String> stations = mAllRoute.get(routeId);
			final String title = "路线-"+routeId+"("+stations.get(0)+"---"+stations.get(stations.size()-1)+")";
			tv_routename.setText(title);
			rl_item_route.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*AlertDialog.Builder builder = new AlertDialog.Builder(RouteActivity.this);
					builder.setTitle(title);
					String[] items = new String[stations.size()];
					for(int i=0; i!=stations.size(); i++){
						items[i] = stations.get(i);
					}
					builder.setSingleChoiceItems(items, 0,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});

					builder.setNegativeButton("退出", null);
					builder.show();*/
					StringBuilder str = new StringBuilder();
					for(int i=0; i!=stations.size(); i++){
						str.append(stations.get(i)+"\n");
					}
					new SweetAlertDialog(RouteActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    .setTitleText("路线-"+routeId)
                    .setContentText(str.toString())
                    .show();
				}
			});
			return view;
		}
		
	}
}
