package android.lazarus4android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class IntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		Thread timer = new Thread() {
			public void run() {
				try { sleep(3000);
				} catch(InterruptedException e) { e.printStackTrace();
				} finally {
					Intent openStartingPoint = new Intent(IntroActivity.this, MainActivity.class);
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_intro, menu);
		return true;
	}
	
	

}
