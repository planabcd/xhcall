package cn.tx.xhc.utils;

import cn.tx.xhc.MyApplication;
import android.widget.Toast;

public class UIUtils {
	public static void showToast(String msg){
		Toast.makeText(MyApplication.getCtx(), msg, Toast.LENGTH_LONG).show();
	}
	
	// /////////////////dip和px转换//////////////////////////

		public static int dip2px(float dip) {
			float density = MyApplication.getCtx().getResources().getDisplayMetrics().density;
			return (int) (dip * density + 0.5f);
		}

		public static float px2dip(int px) {
			float density = MyApplication.getCtx().getResources().getDisplayMetrics().density;
			return px / density;
		}
}
