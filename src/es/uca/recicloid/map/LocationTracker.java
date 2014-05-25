package es.uca.recicloid.map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;


public class LocationTracker extends Service implements LocationListener{
	private final Context mContext;
	// flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    boolean canGetLocation = false;
 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 15; // 15 metros
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minuto
    
    protected LocationManager locationManager;

    public LocationTracker(Context context){
    	super();
    	mContext = context;
    }
    
    public Location getLocation() throws Exception {
    	try{
    		locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
    		isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
    		isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    		if(isNetworkEnabled){
    			locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    		}
    		else if(isGPSEnabled){
    			locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    		}
    		else{
    			Toast msjError = Toast.makeText(getApplicationContext(),"GPY y acceso a internet deshabilitado", Toast.LENGTH_SHORT);
    			msjError.show();
    		}
        	if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
    		}
    	}catch(Exception e){
    		throw new Exception(e);
    	}
    	return location;
    }
    
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
