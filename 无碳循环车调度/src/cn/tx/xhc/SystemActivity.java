package cn.tx.xhc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.tx.xhc.utils.PrefUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SystemActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_system);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.tv_sysytem_car)
	public void carManager(View view){
		Intent it = new Intent(this,CarActivity.class);
		startActivity(it);
	}
	@OnClick(R.id.tv_system_route)
	public void routeManager(View view){
		Intent it = new Intent(this,AddStationActivity.class);
		startActivity(it);
	}
	
	@OnClick(R.id.tv_system_order)
	public void orderList(View view){
		Intent intent = new Intent(this,OrderListActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.tv_pri)
	public void about(View view){
		String usertype = PrefUtils.getString(this, "usertype", "Customer");
		String username = PrefUtils.getString(this, "username", "Default");
	    String str;
		if("Customer".equals(usertype)){
	    	str = "登录注册\n预约上车\n站点查询\n路线查询\n高级工具\n订单管理";
	    }else{
	    	str = "登录注册\n统计数据\n车辆管理\n路线管理\n站点管理\n高级工具";
	    }
		new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
        .setTitleText("权限("+username+")")
        .setContentText(str)
        .show();
	}
	@OnClick(R.id.ib_tools_back)
	public void back(View view){
		finish();
	}
	
	
	
}
