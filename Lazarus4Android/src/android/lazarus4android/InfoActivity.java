package android.lazarus4android;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class InfoActivity extends Activity {
	
	private CheckBox infoCheck;
	private TextView model;
	private TextView sdk;
	private TextView manufacturer;
	
	private String modelString;
	private String sdkString;
	private String manufacturerString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		infoCheck = (CheckBox) findViewById(R.id.sendDataEnabled);
		model = (TextView) findViewById(R.id.model);
		sdk = (TextView) findViewById(R.id.sdk);
		manufacturer = (TextView) findViewById(R.id.manufacturer);
		
		modelString = Build.MODEL;
		sdkString = Build.VERSION.RELEASE;
		manufacturerString = Build.MANUFACTURER;
		
		final Memory mem = (Memory) getApplication();
		if (mem.getSendData()) {
			infoCheck.setChecked(true);
			infoCheck.setText("Send Data Enabled");
			model.setText("Adroid Model is: " + modelString);
    		sdk.setText("SDK is: " + sdkString);
    		manufacturer.setText("Manufacturer is: " + manufacturerString);
		}
		
        infoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            	if (infoCheck.isChecked()) {
            		infoCheck.setText("Send Data Enabled");
            		model.setText("Adroid Model is: " + modelString);
            		sdk.setText("SDK is: " + sdkString);
            		manufacturer.setText("Manufacturer is: " + manufacturerString);
            		
            		mem.setModel(modelString);
            		mem.setSdk(sdkString);
            		mem.setManufacturer(manufacturerString);
            		mem.setSendData(true);	
            	} else {
            		infoCheck.setText("Send Data Enabled");
            		model.setText("Adroid Model is: Disabled");
            		sdk.setText("SDK is: Disabled");
            		manufacturer.setText("Manufacturer is: Disabled");
            		
            		mem.setModel(modelString);
            		mem.setSdk(sdkString);
            		mem.setManufacturer(manufacturerString);
            		mem.setSendData(false);
            	}
			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info, menu);
		return true;
	}

}
