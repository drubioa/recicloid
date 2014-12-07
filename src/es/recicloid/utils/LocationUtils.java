package es.recicloid.utils;

import java.util.Date;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public abstract class LocationUtils {
	
	/**
	 * Transforms a LatLng to Location
	 * @param point
	 * @return Location correspondents to point dispatched
	 */
	public static Location convertLagIntToLocation(LatLng point){
		  Location location = new Location("PuntoRecogida");
		  location.setLatitude(point.latitude);
		  location.setLongitude(point.longitude);
		  location.setTime(new Date().getTime());
		  return location;
	}
	
	
	
}
