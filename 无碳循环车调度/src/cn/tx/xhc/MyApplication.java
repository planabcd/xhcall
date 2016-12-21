package cn.tx.xhc;

import android.app.Application;
import android.content.Context;
import cn.smssdk.SMSSDK;

public class MyApplication extends Application{

	public static Context ctx;
	@Override
	public void onCreate() {
		super.onCreate();
		ctx = this;
		SMSSDK.initSDK(this, "1a02de80f0ec4", "f2a14e7d2c214b97d43a8def7d94a6df");
	}
	
	public static Context getCtx(){
		return ctx;
	}
}
