package es.uca.recicloid.map;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

public class AddressTracker {
	Context mContext ;
	
	public AddressTracker(Context context){
		mContext = context;
	}
	
	public LatLng getLocationBy(String strAddress) throws IOException{
		Geocoder coder = new Geocoder(mContext);
		List<Address> address = coder.getFromLocationName(strAddress,1);
		if(address == null){
			return null;
		}
		Address location = address.get(0);
	    location.getLatitude();
	    location.getLongitude();
	    LatLng p1 = new LatLng(location.getLatitude(),location.getLongitude());

	    return p1;
	}
}
