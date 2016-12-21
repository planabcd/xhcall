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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ToolsActivity extends Activity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools);
		ShareSDK.initSDK(this);
		ViewUtils.inject(this);
	}
	
	@OnClick(R.id.tv_share)
	public void share(View view){
		showShare();
	}
	
	@OnClick(R.id.tv_about)
	public void about(View view){
		new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
        .setTitleText("交通控制课程设计")
        .setContentText("廖    鑫:201324040211\n黄保宁:201324040112\n王景辉:201324040219")
        .show();
	}
	@OnClick(R.id.ib_tools_back)
	public void back(View view){
		finish();
	}
	@OnClick(R.id.tv_guishudi)
	public void phoneAddress(View view){
		Intent it = new Intent(this,PhoneAddressActivity.class);
		startActivity(it);
	}
	
	@OnClick(R.id.tv_youbian)
	public void phoneEmail(View view){
		Intent it = new Intent(this,EmailActivity.class);
		startActivity(it);
	}
	
	
	
	
	
	@OnClick(R.id.tv_exit)
	public void exit(View view){
		new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        .setTitleText("Are you sure?")
        .setContentText("Won't be able to recover this action")
        .setCancelText("No,cancel it!")
        .setConfirmText("Yes,exit now!")
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
            	sDialog.dismiss();
				/*Intent i = new Intent(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);*/
            	
            	Intent it = new Intent(ToolsActivity.this,LoginActivity.class);
				startActivity(it);
				finish();
            }
        })
        .show();
		
	}
	
	private void showShare() {
		 OnekeyShare oks = new OnekeyShare();
		 oks.setTheme(OnekeyShareTheme.CLASSIC);// 修改主题样式
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 
		 // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		 oks.setTitle("无碳循环车应用");
		 // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("无碳循环车应用,更多酷炫功能,等你来体验,下载地址 http://www.sshpro.cn/xhc.apk");
		 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		 //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("真的很好用!");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite("ShareSDK");
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }
}
