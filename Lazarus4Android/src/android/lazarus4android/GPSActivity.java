package android.lazarus4android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class GPSActivity extends Activity {
	
	private CheckBox gpsCheck;
	private TextView longitude;
	private TextView latitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		
		gpsCheck = (CheckBox) findViewById(R.id.gpsEnabled);
		longitude = (TextView) findViewById(R.id.gpsLongitude);
		latitude = (TextView) findViewById(R.id.gpsLatitude);
		
		final Memory mem = (Memory) getApplication();
		mem.setGPSButtons(longitude, latitude);
		
		if (mem.getGpsEnabled()) {
			gpsCheck.setChecked(true);
			gpsCheck.setText("Gps Enabled");
			mem.printGPSData();
		}
		
        gpsCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	if (gpsCheck.isChecked()) {
            		gpsCheck.setText("Gps Enabled");
            		mem.setGpsEnabled(true);
            		mem.printGPSData();
            	} else {
            		gpsCheck.setText("Gps Disabled");
            		mem.setGpsEnabled(false);
            		mem.destroyGpsListener();
            	}
			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_gps, menu);
		return true;
	}
}
