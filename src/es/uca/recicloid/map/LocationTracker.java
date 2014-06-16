package es.uca.recicloid.map;

import es.uca.recicloid.R;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Clase que permite empleando 3G, Internet o GPS obtener la ubicación
 * donde se encuentra un usuario.
 * @author Diego Rubio Abujas
 *
 */
public class LocationTracker extends Service implements LocationListener{
	private final Context mContext;
	// flags
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
 
    Location location; 
    double latitude; 
    double longitude; 
    
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 15; // 15 metros
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 min
    
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
    			Log.i("LocationTracker","Getting location by network");
    			locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    		}
    		else if(isGPSEnabled){
    			Log.i("LocationTracker","Getting location by GPS");
    			locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    		}
    		else{
    			Log.w("LocationTracker", getResources().getString(
        		        		R.string.dialog_err_location_gpsint3d_disabled));
    			Toast msjError = Toast.makeText(getApplicationContext(),
    					getResources().getString(
        		        		R.string.dialog_err_location_gpsint3d_disabled), 
    					Toast.LENGTH_SHORT);
    			msjError.show();
    		}
        	if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                // El usuario ha registrado su ubicación anteriormente
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                else{
                	Log.w("LocationTracker","location is null in LocationTracker");
                }
    		}
        	else{
        		Log.e("LocationTracker","locationManager is null in LocationTracker");
        	}
    	}catch(Exception e){
    		Log.e("LocationTracker",e.toString());
    		Toast msjError = Toast.makeText(getApplicationContext(),
    				getResources().getString(
    		        		R.string.dialog_err_location_exception), 
					Toast.LENGTH_SHORT);
			msjError.show();
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
