package es.recicloid.logic.map;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

public class AddressTracker {
	Context mContext ;
	
	public AddressTracker(Context context){
		mContext = context;
	}
	
	/**
	 * Obtiene la latitud y longitud de una cadena de texto correspondiente
	 * a una direcci??n
	 * @param strAddress
	 * @return
	 * @throws IOException
	 */
	public LatLng getLocationBy(String strAddress) throws IOException{
		Geocoder coder = new Geocoder(mContext);
		List<Address> address = coder.getFromLocationName(strAddress,1);
		if(address == null){
			Log.w("AddressTracker","address is null");
			return null;
		}
		Address location = address.get(0);
	    location.getLatitude();
	    location.getLongitude();
	    LatLng p1 = new LatLng(location.getLatitude(),location.getLongitude());
	    Log.i("AddressTracker","returning lng and lat");
	    return p1;
	}
}
