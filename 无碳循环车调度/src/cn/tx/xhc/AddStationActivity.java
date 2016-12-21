package cn.tx.xhc;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.tx.xhc.global.Constant;
import cn.tx.xhc.utils.UIUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
//TODO
public class AddStationActivity extends Activity{
	
	
	@ViewInject(R.id.et_route_search_to)
	private TextView et_route_search_to;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_station_add);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.iv_clear_to)
	public void clearTo(View view){
		et_route_search_to.setText("");
	}
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
	
	@OnClick(R.id.iv_search)
	public void search(View view){
	String to = et_route_search_to.getText().toString().trim();
	if(TextUtils.isEmpty(to)){
		UIUtils.showToast("请输入站点,按照';'分割");
		return;
	}else{
		addStation(to);
	}
	}
	
	public void addStation(String to) {
		String replace = to.replace(";", ",");
		String queryUrl = Constant.ROUTE_ADD+"?route="+replace;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if("true".equals(responseInfo.result)){
					new SweetAlertDialog(AddStationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
	                .setTitleText("添加路线成功")
	                .show();
				}else{
					UIUtils.showToast("添加路线失败");
				}
				
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
		
	}

}
