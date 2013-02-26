package android.lazarus4android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class BatteryActivity extends Activity {
	
	private CheckBox batteryCheck;
	private TextView chargingState;
	private TextView batteryLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);
		
		batteryCheck = (CheckBox) findViewById(R.id.batteryEnabled);
		chargingState = (TextView) findViewById(R.id.batteryState);
		batteryLevel = (TextView) findViewById(R.id.batteryLevel);
		
		final Memory mem = (Memory) getApplication();
		mem.setBatteryButtons(chargingState, batteryLevel);
		if (mem.getBatteryEnabled()) {
			batteryCheck.setChecked(true);
			batteryCheck.setText("Battery Info Enabled");
			mem.printBatteryData();
		}
		
		batteryCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	if (batteryCheck.isChecked()) {
            		batteryCheck.setText("Battery Info Enabled");
            		mem.setBatteryEnabled(true);
            		mem.printBatteryData();
            	} else {
            		batteryCheck.setText("Battery Info Disabled");
            		mem.setBatteryEnabled(false);
            		mem.destroyReceiver();
            	}
			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_battery, menu);
		return true;
	}
}