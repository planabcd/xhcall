package cn.tx.xhc;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.tx.xhc.domain.Car;
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

public class CarActivity extends Activity{
	
	
	@ViewInject(R.id.pb_car_query)
	private ProgressBar pb_car_query;
	
	@ViewInject(R.id.lv_car)
	private ListView lv_car;

	private String mRouteId;

	
	private String selectedCarId;


	private MyAdapter mMyAdapter;
	
	private ArrayList<Car> carList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_car);
		ViewUtils.inject(this);
		listCar();
	}
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
	
	@OnClick(R.id.iv_search)
	public void search(View view){
		addCar();
	}
	public void listCar(){
		pb_car_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.CAR_LIST_BY_RUNNINGTYPE+"?runningType=1"+"&token="+System.currentTimeMillis();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_car_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if(TextUtils.isEmpty(result)){
					UIUtils.showToast("服务器暂无数据");
					return;
				}else{
					Gson gson = new Gson();
					Type type = new TypeToken<ArrayList<Car>>(){}.getType();
					carList = gson.fromJson(result, type);
					if(carList!=null && carList.size()>0){
						if(mMyAdapter!=null){
							mMyAdapter.notifyDataSetChanged();
						}else{
							mMyAdapter = new MyAdapter();
							lv_car.setAdapter(mMyAdapter);
						}
					}else{
						UIUtils.showToast("服务器暂无数据,请稍后重试");
					}
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				pb_car_query.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
	}
	
	
	public void addCar() {
		pb_car_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.CAR_ADD;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_car_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if(!TextUtils.isEmpty(result)){
					new SweetAlertDialog(CarActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("添加车辆成功,车辆编号:"+result)
                    .show();
					listCar();
				}else{
					UIUtils.showToast("添加车辆失败,请稍后重试");
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				pb_car_query.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
		
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return carList.size();
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
			
			View view = View.inflate(CarActivity.this,R.layout.list_item_route, null);
			TextView tv_routename =(TextView) view.findViewById(R.id.tv_routename);
			RelativeLayout rl_item_route = (RelativeLayout) view.findViewById(R.id.rl_item_route);
			ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
			final Car car = carList.get(position);
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 new SweetAlertDialog(CarActivity.this, SweetAlertDialog.WARNING_TYPE)
                     .setTitleText("确定要删除车辆(编号-"+car.getId()+")?")
                     .setContentText("该过程不可恢复!")
                     .setCancelText("取消")
                     .setConfirmText("确认")
                     .showCancelButton(true)
                     .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                         @Override
                         public void onClick(SweetAlertDialog sDialog) {
                        	sDialog.dismiss();
                        	pb_car_query.setVisibility(View.VISIBLE);
         					String queryUrl = Constant.CAR_DELETE+"?carId="+car.getId();
         					HttpUtils httpUtils = new HttpUtils();
         					httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
         						@Override
         						public void onSuccess(ResponseInfo<String> responseInfo) {
         							pb_car_query.setVisibility(View.INVISIBLE);
         							String result = responseInfo.result;
         							if("true".equals(result)){
         								 new SweetAlertDialog(CarActivity.this, SweetAlertDialog.SUCCESS_TYPE)
         			                     .setTitleText("删除车辆(编号-"+car.getId()+")成功")
         			                     .show();
         								carList.remove(position);
         								mMyAdapter.notifyDataSetChanged();
         							}else{
         								UIUtils.showToast("删除车辆(编号-"+car.getId()+")失败,请稍后重试");
         							}
         						}
         						@Override
         						public void onFailure(HttpException error, String msg) {
         							pb_car_query.setVisibility(View.INVISIBLE);
         							UIUtils.showToast("网络错误,请检查网络");
         						}
         					});
         					
                         }
                     })
                     .show();
				}
			});
			
			tv_routename.setText("车辆编号:"+car.getId()+"\n运行线路:线路--"+car.getCurrent_id());
			rl_item_route.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectedCarId = ""+car.getId();
					Intent it = new Intent(CarActivity.this,SelectCarRouteActivity.class);
					startActivityForResult(it, 10);
				}
			});
			return view;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			mRouteId = data.getStringExtra("routeId");
			updateCar(mRouteId,selectedCarId);
		}
	}

	private void updateCar(String routeId, String selectedCarId) {
		
		if(TextUtils.isEmpty(selectedCarId)){
			return;
		}
		pb_car_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.CAR_DISTRIBUTE+"?circuitId="+routeId+"&carId="+selectedCarId;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_car_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if("true".equals(result)){
					new SweetAlertDialog(CarActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("更新车辆状态成功")
                    .show();
					listCar();
				}else{
					UIUtils.showToast("更新车辆状态失败,请稍后重试");
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				pb_car_query.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
	}
}
