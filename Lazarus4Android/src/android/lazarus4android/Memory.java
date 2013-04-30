package android.lazarus4android;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
 
public class Memory extends Application {
	
	// SYNCHRONIZATION LOCKS //
	final Object gpsEnabledLock = new Object();
	final Object latitudeLock = new Object();
	final Object longitudeLock = new Object();
	final Object batteryEnabledLock = new Object();
	final Object batteryStatusLock = new Object();
	final Object batteryLevelLock = new Object();
	final Object sendDataLock = new Object();
	final Object modelLock = new Object();
	final Object sdkLock = new Object();
	final Object manufacturerLock = new Object();
	
	
	// GPS //
	LocationManager lm;
	LocationListener locationListener;
	
	private boolean gpsEnabled = false;
	private String gpsLatitude = "0.0";
	private String gpsLongitude = "0.0";
	
	private TextView longitudeTextview;
	private TextView latitudeTextview;
	
	
	// BATTERY //
	private boolean batteryEnabled = false;
	private String batteryLevel = "Disabled";
	private String batteryStatus = "Disabled";
	
	private TextView chargingStateTextview;
	private TextView batteryLevelTextview;
	
	
	// GENERAL INFO //
	private volatile boolean sendData = false;
	private String model = "Disabled";
	private String sdk = "Disabled";
	private String manufacturer = "Disabled";
	private String imei = "0000000";
	
	
	// WEB SERVICE PROPERTIES //
	private String NAMESPACE;
	private String METHOD_NAME;
	private String SOAP_ACTION;
	private String URL;
	private String TIME;
	private int time;
	
	@Override
        public void onCreate() {
            super.onCreate();
            getWebServiceProperties();
        
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        }
	
	
	
	//****************************//
	//     OPEN PROPERTY FILE     //
	//****************************//
	
	private void getWebServiceProperties() {
		Resources resources = this.getResources();
		AssetManager assetManager = resources.getAssets();
		InputStream inputStream = null;
		
		try {
		    inputStream = assetManager.open("androidPropertyFile.properties");
		    Properties properties = new Properties();
		    properties.load(inputStream);
		    Log.d("properties", "Properties file found and loaded");
		    
		    NAMESPACE = properties.getProperty("NAMESPACE");
		    METHOD_NAME = properties.getProperty("METHOD_NAME");
		    SOAP_ACTION = properties.getProperty("SOAP_ACTION");
		    URL = properties.getProperty("URL");
		    TIME = properties.getProperty("TIME");
		    
		    time = Integer.parseInt(TIME);
		} catch (IOException e) {
		    System.err.println("Failed to open microlog property file");
		    e.printStackTrace();
		} finally {
			try { inputStream.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}



	//*********************************************//
	// GPS LISTENERS, ATTRIBUTE SETTERS-GETTERS    //
	// AND PRINT DATA METHODS                      //
	//*********************************************//
	
	public void initializeGPSListener() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new GPSLocationListener(this);
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}
	public void destroyGpsListener() {
		lm = null;
		locationListener = null;
		printGPSData();
	}

	public boolean getGpsEnabled() { synchronized(gpsEnabledLock){ return gpsEnabled; }}
	public void setGpsEnabled(boolean gpsEnabled) {
		synchronized(gpsEnabledLock) {
			this.gpsEnabled = gpsEnabled;
			initializeGPSListener();
	}}
	public String getGpsLatitude() { synchronized(latitudeLock){ return gpsLatitude; }}
	public String getGpsLongitude() { synchronized(longitudeLock){ return gpsLongitude; }}
	public void setGpsLatitude(String gpsLatitude) { synchronized(latitudeLock){ this.gpsLatitude = gpsLatitude;}}
	public void setGpsLongitude(String gpsLongitude) { synchronized(longitudeLock){ this.gpsLongitude = gpsLongitude; }}

	public void printGPSData() {
		if (getGpsEnabled()) {
			longitudeTextview.setText("GPS Longtitude is: " + getGpsLongitude());
    		latitudeTextview.setText("GPS Latitude is: " + getGpsLatitude());
		} else {
			longitudeTextview.setText("GPS Longtitude is: Disabled");
    		latitudeTextview.setText("GPS Latitude is: Disabled");
		}
	}

	public void setGPSButtons(TextView longitude, TextView latitude) {
		this.latitudeTextview = latitude;
		this.longitudeTextview = longitude;
	}
	
	
	
	//*********************************************//
	// BATTERY LISTENERS, ATTRIBUTE SETTERS-GETTERS//
	// AND PRINT DATA METHODS                      //
	//*********************************************//
	
	public boolean getBatteryEnabled() { synchronized(batteryEnabledLock){ return batteryEnabled; }}
	public void setBatteryEnabled(boolean batteryEnabled) {
		synchronized(batteryEnabledLock) {
			this.batteryEnabled = batteryEnabled;
			createReceiver();
	}}

	public String getBatteryLevel() { synchronized(batteryLevelLock){ return batteryLevel; }}
	public String getBatteryStatus() { synchronized(batteryStatusLock){ return batteryStatus; }}
	public void setBatteryLevel(String batteryLevel) { synchronized(batteryLevelLock){ this.batteryLevel = batteryLevel; }}
	public void setBatteryStatus(String batteryStatus) { synchronized(batteryStatusLock){ this.batteryStatus = batteryStatus; }}

	public void setBatteryButtons(TextView chargingState, TextView batteryLevel) {
		this.chargingStateTextview = chargingState;
		this.batteryLevelTextview = batteryLevel;
	}
	
	public void createReceiver() {
		this.registerReceiver(this.batteryReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	public void destroyReceiver() {
		this.unregisterReceiver(this.batteryReceiver);
		printBatteryData();
	}
	
	public BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String statusString = "Unknown";
			int  level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			int status = intent.getIntExtra("status", 0);
			switch (status) {
		        case BatteryManager.BATTERY_STATUS_CHARGING:
		            statusString = "Charging";
		            break;
		        case BatteryManager.BATTERY_STATUS_DISCHARGING:
		            statusString = "Discharging";
		            break;
		        case BatteryManager.BATTERY_STATUS_FULL:
		            statusString = "Full";
		            break;
		        case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
		            statusString = "Not Charging";
		            break;
	        }
			setBatteryStatus(statusString);
			setBatteryLevel(String.valueOf(level)); 
			printBatteryData();
		}
	};
	
	public void printBatteryData() {
		if (getBatteryEnabled()) {
			chargingStateTextview.setText("Charging state is: " + getBatteryStatus());
			batteryLevelTextview.setText("Battery Level is: " + getBatteryLevel() + " %");
		} else {
			chargingStateTextview.setText("Charging state is: Disabled");
			batteryLevelTextview.setText("Battery Level is: Disabled");
		}
	}
	
	
	
	//*********************************************//
	// GENERAL INFO LISTENERS,ATTRIBUTE            //
	// SETTERS-GETTERS AND PRINT DATA METHODS      //
	//*********************************************//
	
	public boolean getSendData() { synchronized(sendDataLock){ return sendData; }}
	public void setSendData(boolean sendData) {
		synchronized(sendDataLock) {
			this.sendData = sendData;
			if (sendData)
				createWSThread();
	}}

	public String getModel() { synchronized(modelLock){ return model; }}
	public String getSdk() { synchronized(sdkLock){ return sdk; }}
	public String getManufacturer() { synchronized(manufacturerLock){ return manufacturer; }}
	public void setModel(String model) { synchronized(modelLock){ this.model = model; }}
	public void setSdk(String sdk) { synchronized(sdkLock){ this.sdk = sdk; }}
	public void setManufacturer(String manufacturer) { synchronized(manufacturerLock){ this.manufacturer = manufacturer; }}
	
	
	
	//*********************************************//
	// WEB SERVICE THREAD AND METHODS              //
	// (USING KSOAP 2)                             //
	//*********************************************//
	
	private void createWSThread() {
		Thread t = new Thread(new WSThread());
		t.start();
	}
	
	public class WSThread implements Runnable{

		@Override
		public void run() {
			while (getSendData()) {
				if (getBatteryEnabled() && getGpsEnabled()) {
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					TerminalData data = new TerminalData(getGpsLongitude(), getGpsLatitude(), getBatteryLevel(),
							getBatteryStatus(), getModel(), getSdk(), getManufacturer());
					
					PropertyInfo propInfo1 = new PropertyInfo();
					propInfo1.setName("data");
					propInfo1.setType(data.getClass());
					propInfo1.setValue(data);
					
					request.addProperty("device", imei);
					request.addProperty(propInfo1);
					
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					envelope.setOutputSoapObject(request);
					HttpTransportSE ht = new HttpTransportSE(URL);
					 
					try {
						ht.call(SOAP_ACTION, envelope);
					} catch (IOException e) { e.printStackTrace();
					} catch (XmlPullParserException e) { e.printStackTrace();
					} catch (Exception ex){ ex.printStackTrace(); }
				}
				try { Thread.sleep(time); }
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}			
	}
}
