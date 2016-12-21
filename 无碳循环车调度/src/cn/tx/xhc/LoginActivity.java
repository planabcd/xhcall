package cn.tx.xhc;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
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

public class LoginActivity extends Activity {
	
	@ViewInject(R.id.et_username)
	private EditText et_username;
	
	@ViewInject(R.id.et_password)
	private EditText et_password;
	
	@ViewInject(R.id.rg_usertype)
	private RadioGroup rg_usertype;
	
	@ViewInject(R.id.pb_user_login)
	private ProgressBar pb_user_login;
	
	private String userType;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}
	
	/**
	 * 登录
	 * @param view
	 */
	@OnClick(R.id.btn_load)
	public void login(View view){
		final String username = et_username.getText().toString();
		final String pwd = et_password.getText().toString();
		userType = Constant.USERTYPE_ADMIN; //默认Admin
		if(TextUtils.isEmpty(username)){
			UIUtils.showToast("请输入用户名");
			return;
		}
		if(TextUtils.isEmpty(pwd)){
			UIUtils.showToast("请输入密码");
			return;
		}
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("username", username);
		params.addBodyParameter("password", pwd);
		int checkedRadioButtonId = rg_usertype.getCheckedRadioButtonId();
		if(R.id.rb_admin==checkedRadioButtonId){
			//管理员界面
			params.addBodyParameter("usertype", Constant.USERTYPE_ADMIN);
			userType = Constant.USERTYPE_ADMIN;
		}else{
			//普通用户界面
			params.addBodyParameter("usertype",Constant.USERTYPE_NOT_ADMIN);
			userType = Constant.USERTYPE_NOT_ADMIN;
		}
		
		pb_user_login.setVisibility(View.VISIBLE);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, Constant.URL_LOGIN_USER,params,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pb_user_login.setVisibility(View.INVISIBLE);
				String result = responseInfo.result;
				if("true".equals(result)){
					Intent it = new Intent(LoginActivity.this,MainActivity.class);
					it.putExtra("usertype", userType);
					it.putExtra("username", username);
					startActivity(it);
					finish();
				}else{
					UIUtils.showToast("用户名,角色或者密码错误,请重新输入");
					et_username.setText("");
					et_password.setText("");
					et_username.setFocusable(true);
					return;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				pb_user_login.setVisibility(View.INVISIBLE);
				UIUtils.showToast("网络错误,请检查网络");
			}
		});
		
		/*Intent it = new Intent(LoginActivity.this,MainActivity.class);
		startActivity(it);
		finish();*/
		
	}
	/**
	 * 前往注册页面
	 * @param view
	 */
	@OnClick(R.id.btn_toreg)
	public void toRegister(View view){
		//打开注册页面
		RegisterPage registerPage = new RegisterPage();
		registerPage.setRegisterCallback(new EventHandler() {
		public void afterEvent(int event, int result, Object data) {
		// 解析注册结果
		if (result == SMSSDK.RESULT_COMPLETE) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
			String phone = (String) phoneMap.get("phone"); 
			Intent it = new Intent(LoginActivity.this,RegisterActivity.class);
			it.putExtra("username", phone);
			startActivity(it);
				}
			}
		});
		registerPage.show(this);
	}
	
	/**
	 * 忘记密码
	 * @param view
	 */
	@OnClick(R.id.tv_forgot_password)
	public void forgotPwd(View view){
		RegisterPage registerPage = new RegisterPage();
		registerPage.setRegisterCallback(new EventHandler() {
		public void afterEvent(int event, int result, Object data) {
		// 解析注册结果
		if (result == SMSSDK.RESULT_COMPLETE) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
			String phone = (String) phoneMap.get("phone"); 
			Intent it = new Intent(LoginActivity.this,ForgotActivity.class);
			it.putExtra("username", phone);
			startActivity(it);
				}
			}
		});
		registerPage.show(this);
	}
}
