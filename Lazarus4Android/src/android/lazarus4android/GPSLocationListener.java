package android.lazarus4android;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSLocationListener implements LocationListener {
	
	Memory mem;
	
	public GPSLocationListener(Memory mem) {
		this.mem = mem;
	}

	@Override
	public void onLocationChanged(Location location) {
		String latitude = String.valueOf(location.getLatitude());
		String longitude = String.valueOf(location.getLongitude());
		mem.setGpsLatitude(latitude);
		mem.setGpsLongitude(longitude);
		mem.printGPSData();
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
