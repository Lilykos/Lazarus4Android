package android.lazarus4android;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class TerminalData implements KvmSerializable {
    private String longitude;
    private String latitude;
    private String batteryLevel;
    private String batteryStatus;
    private String model;
    private String sdk;
    private String manufacturer;

    public TerminalData(String gpsLongitude, String gpsLatitude,
			String batteryLevel, String batteryStatus, String model,
			String sdk, String manufacturer) {
		
    	this.longitude = gpsLongitude;
    	this.latitude = gpsLatitude;
    	this.batteryLevel = batteryLevel;
    	this.batteryStatus = batteryStatus;
    	this.model = model;
    	this.sdk = sdk;
    	this.manufacturer = manufacturer;
	}

	@Override
	public Object getProperty(int arg0) {
		if (arg0 == 0)
			return longitude;	
		if (arg0 == 1) 
			return latitude;
		if (arg0 == 2) 
			return batteryLevel;
		if (arg0 == 3) 
			return batteryStatus;
		if (arg0 == 4) 
			return model;
		if (arg0 == 5) 
			return sdk;
		if (arg0 == 6) 
			return manufacturer;
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 7;
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		if (arg0 == 0) {
			arg2.name = "longitude";
			arg2.type = PropertyInfo.STRING_CLASS;			
		}
		if (arg0 == 1) {
			arg2.name = "latitude";
			arg2.type = PropertyInfo.STRING_CLASS;
		}
		if (arg0 == 2) {
			arg2.name = "batteryLevel";
			arg2.type = PropertyInfo.STRING_CLASS;
		}
		if (arg0 == 3) {
			arg2.name = "batteryStatus";
			arg2.type = PropertyInfo.STRING_CLASS;
		}
		if (arg0 == 4) {
			arg2.name = "model";
			arg2.type = PropertyInfo.STRING_CLASS;
		}
		if (arg0 == 5) {
			arg2.name = "sdk";
			arg2.type = PropertyInfo.STRING_CLASS;
		}
		if (arg0 == 6) {
			arg2.name = "manufacturer";
			arg2.type = PropertyInfo.STRING_CLASS;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		if (arg0 == 0)
			longitude = arg1.toString();
		if (arg0 == 1) 
			latitude = arg1.toString();
		if (arg0 == 2) 
			batteryLevel = arg1.toString();
		if (arg0 == 3) 
			batteryStatus = arg1.toString();
		if (arg0 == 4) 
			model = arg1.toString();
		if (arg0 == 5) 
			sdk = arg1.toString();
		if (arg0 == 6) 
			manufacturer = arg1.toString();
	}

}
