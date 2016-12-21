package cn.tx.xhc;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import cn.tx.xhc.global.Constant;
import cn.tx.xhc.utils.UIUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class RegisterActivity extends Activity {
	
	
	
	@ViewInject(R.id.et_username)
	private EditText et_username;
	
	
	@ViewInject(R.id.et_password)
	private EditText et_password;
	
	@ViewInject(R.id.et_password_confirm)
	private EditText et_password_confirm;
	
	@ViewInject(R.id.pb_user_reg)
	private ProgressBar pb_user_reg;

	
	@ViewInject(R.id.rg_usertype)
	private RadioGroup rg_usertype;


	private String mPhone;
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		ViewUtils.inject(this);
		mPhone = getIntent().getStringExtra("username");
		et_username.setText(mPhone);
	}
	
	
	
	
	/**
	 * 注册
	 * @param view
	 */
	@OnClick(R.id.btn_reg)
	public void register(View view){
		String username = et_username.getText().toString();
		String pwd = et_password.getText().toString();
		String pwd_confirm = et_password_confirm.getText().toString();
		if(TextUtils.isEmpty(username)){
			UIUtils.showToast("请输入用户名");
			return;
		}
		if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_confirm)){
			UIUtils.showToast("请输入密码");
			return;
		}
		if(!pwd.equals(pwd_confirm)){
			UIUtils.showToast("两次输入不一致,请确认");
			return;
		}
		
		
		pb_user_reg.setVisibility(View.VISIBLE);
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", username);
		params.addBodyParameter("password", pwd);
		
		int checkedRadioButtonId = rg_usertype.getCheckedRadioButtonId();
		if(R.id.rb_admin==checkedRadioButtonId){
			//管理员
			params.addBodyParameter("usertype", Constant.USERTYPE_ADMIN);
		}else{
			//普通用户
			params.addBodyParameter("usertype",Constant.USERTYPE_NOT_ADMIN);
		}
		
		httpUtils.send(HttpMethod.POST, Constant.URL_REGISTER_USER, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_user_reg.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if("yes".equals(result)){
					UIUtils.showToast("注册成功");
					finish();
				}else{
					UIUtils.showToast("该用户名已经注册过啦");
					et_username.setText("");
					et_password.setText("");
					et_password_confirm.setText("");
					et_username.setFocusable(true);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				pb_user_reg.setVisibility(View.INVISIBLE);
				UIUtils.showToast("注册失败,请检查网络");
			}
		});
		
	}
}
