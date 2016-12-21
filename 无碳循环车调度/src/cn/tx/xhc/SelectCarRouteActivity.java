package cn.tx.xhc;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
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

public class SelectCarRouteActivity extends Activity{
	
	@ViewInject(R.id.lv_route)
	private ListView lv_route;
	
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;
	private Set<String> mKeySet;
	
	private HashMap<String, ArrayList<String>> mAllRoute;
	
	private MyAdapter mMyAdapter;
	private ArrayList<String> mKeyList;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_car_route_select);
		ViewUtils.inject(this);
		queryAllRoute();
	}
	
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
	
	public void queryAllRoute() {
		pb_route_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.ROUTE_QUERY_ALL_STATION;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_route_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				Gson gson = new Gson();
				mAllRoute = new HashMap<String,ArrayList<String>>();
				Type type = new TypeToken<HashMap<String,ArrayList<String>>>(){}.getType();
				mAllRoute = gson.fromJson(result, type);
				System.out.println(mAllRoute.keySet().size());
				if(mAllRoute!=null && mAllRoute.keySet()!=null && mAllRoute.keySet().size()>0){
					mKeySet = mAllRoute.keySet();
					if(mKeyList!=null){
						mKeyList.clear();
					}else{
						mKeyList = new ArrayList<String>();
					}
					for(String key : mKeySet){
						mKeyList.add(key);
					}
					if(mMyAdapter!=null){
						mMyAdapter.notifyDataSetChanged();
					}else{
						mMyAdapter = new MyAdapter();
						lv_route.setAdapter(mMyAdapter);
					}
				}else{
					UIUtils.showToast("服务器暂无数据");
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
			return mKeyList.size();
		}

		@Override
		public ArrayList<String> getItem(int position) {
			return mAllRoute.get(mKeyList.get(position));
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(SelectCarRouteActivity.this,R.layout.list_item_route, null);
			TextView tv_routename =(TextView) view.findViewById(R.id.tv_routename);
			RelativeLayout rl_item_route = (RelativeLayout) view.findViewById(R.id.rl_item_route);
			final ArrayList<String> stations = getItem(position);
			final String routeId = mKeyList.get(position);
			final String title = "路线-"+routeId+"("+stations.get(0)+"---"+stations.get(stations.size()-1)+")";
			tv_routename.setText(title);
			rl_item_route.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					StringBuilder str = new StringBuilder();
					for(int i=0; i!=stations.size(); i++){
						str.append(stations.get(i)+"\n");
					}
					 new SweetAlertDialog(SelectCarRouteActivity.this, SweetAlertDialog.WARNING_TYPE)
                     .setTitleText("路线-"+routeId)
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
                             intent.putExtra("routeName", stations.get(0)+"---"+stations.get(stations.size()-1));
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
