package cn.tx.xhc;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
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

public class FeeActivity extends Activity{
	
	@ViewInject(R.id.et_route_search_from)
	private TextView et_route_search_from;
	
	@ViewInject(R.id.et_route_search_to)
	private TextView et_route_search_to;
	
	@ViewInject(R.id.pb_route_query)
	private ProgressBar pb_route_query;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fee);
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
			queryFee(from,to);
//			queryFee("15号楼","东门");
		}
	}
	
	public void queryFee(final String from,final String to) {
		pb_route_query.setVisibility(View.VISIBLE);
		String queryUrl = Constant.FEE_QUERY_ALL + "?from="+from+"&to="+to;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, queryUrl,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_route_query.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if(TextUtils.isEmpty(result)){
					UIUtils.showToast("请确认输入的地址是否正确,服务器暂无数据");
				}else{
					
					 new SweetAlertDialog(FeeActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                     .setTitleText(from+"---"+to)
                     .setContentText("￥ "+result)
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

}
