package cn.tx.xhc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.tx.xhc.utils.UIUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ForgotActivity extends Activity {
	
	
	
	
	@ViewInject(R.id.et_username)
	private EditText et_username;
	
	
	@ViewInject(R.id.et_password)
	private EditText et_password;

	private String mPhone;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgot);
		ViewUtils.inject(this);
		mPhone = getIntent().getStringExtra("username");
		et_username.setText(mPhone);
	}
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
	
	/**
	 * 忘记密码确认
	 * @param view
	 */
	@OnClick(R.id.btn_reset)
	public void forgot(View view){
		String pwd = et_password.getText().toString();
		if(TextUtils.isEmpty(pwd)){
			UIUtils.showToast("请输入新的密码");
			return;
		}
		//TODO
		UIUtils.showToast("重置密码....");
//		Intent it = new Intent(this,MainActivity.class);
//		startActivity(it);
//		finish();
		
	}
}
