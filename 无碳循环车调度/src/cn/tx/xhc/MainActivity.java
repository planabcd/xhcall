package cn.tx.xhc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tx.xhc.global.Constant;
import cn.tx.xhc.utils.PrefUtils;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MainActivity extends Activity {
	

	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	
	@ViewInject(R.id.gv_home_gridview)
	private GridView gv_home_gridview;

	private String usertype = Constant.USERTYPE_ADMIN;
	private String username = "username";
	
	@OnClick(R.id.ib_route_back)
	public void back(View view){
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		Intent it = getIntent();
		usertype = it.getStringExtra("usertype");
		username = it.getStringExtra("username");
		PrefUtils.setString(this, "usertype", usertype);
		PrefUtils.setString(this, "username", username);
		
		tv_title.setText("您好,"+username+"("+usertype+")。欢迎访问长安大学无碳循环车调度系统!!!,更多酷炫功能,等你来体验~");
		gv_home_gridview.setAdapter(new Myadapter());
		gv_home_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				Intent it;
				switch (pos) {
					case 0:
						//进入登陆注册界面
						it = new Intent(MainActivity.this,LoginActivity.class);
						startActivity(it);
						finish();
						break;
					case 1:
						//进入查询路线界面
						it = new Intent(MainActivity.this,RouteActivity.class);
						startActivity(it);
						break;
					case 2:
						//进入查询费用界面
						it = new Intent(MainActivity.this,StationActivity.class);
						startActivity(it);
						break;
					case 3:
						//进入查询费用界面
						it = new Intent(MainActivity.this,FeeActivity.class);
						startActivity(it);
						break;
					case 4:
						//进入查询费用界面
						it = new Intent(MainActivity.this,TimeActivity.class);
						startActivity(it);
						break;
					case 5:
						//进入查询路线界面
						it = new Intent(MainActivity.this,OrderActivity.class);
						startActivity(it);
						break;
					case 6:
						//统计数据
						it = new Intent(MainActivity.this,CountDataActivity.class);
						startActivity(it);
						break;
					case 7:
						//高级工具
						it = new Intent(MainActivity.this,ToolsActivity.class);
						startActivity(it);
						break;
					case 8:
						//系统管理
						it = new Intent(MainActivity.this,SystemActivity.class);
						startActivity(it);
						break;
					default:break;
					
					
				}
				
			}
		});
		
		
	}
	
	class Myadapter extends BaseAdapter{

		int[] imageId = { R.drawable.home_item_load, R.drawable.home_item_route, R.drawable.home_item_station,
				R.drawable.home_item_coin, R.drawable.home_item_time, R.drawable.home_item_order,
				R.drawable.home_item_data, R.drawable.home_item_tools, R.drawable.home_item_setting };
		
		
		String[] names = { "登录注册", "查询路线", "站点详情", "费用咨询", "运行时间", "我要约车", "统计数据",
				"高级工具", "系统管理" };
		
		@Override
		public int getCount() {
			return 9;
		}

		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.list_item_main,null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_itemhome_icon);
			TextView tv = (TextView) view.findViewById(R.id.tv_itemhome_text);
			iv.setImageResource(imageId[position]);
			tv.setText(names[position]);
			return view;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}


}
