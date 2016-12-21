package cn.tx.xhc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import cn.tx.xhc.utils.Typefaces;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

public class SplashActivity extends ActionBarActivity {
	private Titanic mTitanic;
	private Handler mHandler;
	private boolean isCancel = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        mTitanic = new Titanic();
        mTitanic.start(tv);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!isCancel){
					mTitanic.cancel();
	        		Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
	        		startActivity(intent);
	        		finish();
				}
			}
		}, 10000);
	}
	
	public void enterLogin(View v){
		mTitanic.cancel();
		isCancel = true;
		Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
