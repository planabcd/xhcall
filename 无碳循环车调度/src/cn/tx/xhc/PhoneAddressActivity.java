package cn.tx.xhc;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.tx.xhc.domain.PhoneAddressResult;
import cn.tx.xhc.domain.PhoneAddressResult.PhoneAddressResultData;
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

public class PhoneAddressActivity extends Activity{
	
	
	@ViewInject(R.id.et_route_search_to)
	private TextView et_route_search_to;
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phoneaddress);
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
			UIUtils.showToast("请输入完整的电话号码");
			return;
		}else{
			queryPhoneAddress(to);
		}
	}
	
	public void queryPhoneAddress(final String phone) {
		pb_route_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.PHONE_QUERY_ADDRESS + "?key="+Constant.APPKEY_PHONE_QUERY_ADDRESS+"&phone="+phone;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_route_query.setVisibility(View.INVISIBLE);
				
				
				String result = responseInfo.result;
				Gson gson = new Gson();
				PhoneAddressResult phoneAddressResult = gson.fromJson(result, PhoneAddressResult.class);
				if("success".equals(phoneAddressResult.getMsg())){
					PhoneAddressResultData resultData = phoneAddressResult.getResult();
					new SweetAlertDialog(PhoneAddressActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
		            .setTitleText(phone)
		            .setContentText(resultData.getProvince()+"-"+resultData.getCity()+"-"+resultData.getOperator())
		            .show();
				}else{
					//没有数据
					UIUtils.showToast("请确认输入的号码是否正确,服务器暂无数据");
				}
				
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				pb_route_query.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
		
	}
}
