package es.uca.recicloid.map;

import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng ;

public class Zone {
	private List<LatLng> mPuntos;
	/**
	 * Parse file xml and obtains de list with the zone´s points
	 * @param context
	 * @param idZone 1 for Urban zone and 2 for general zone.
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public Zone(List<LatLng> puntos){
		mPuntos = puntos;
	}
	
	/**
	 * Comprueba si el punto loc se encuentra en la zona
	 * @param loc 
	 * @return
	 */
	public boolean isInside(LatLng loc){
		return isPointInPolygon(loc,mPuntos);
	}
	
	/**
	 * Comprueba si el punto loc se encuentra en la zona
	 * @param loc un tipo Localization
	 * @return
	 */
	public boolean isInside(Location loc){
		return isPointInPolygon(new LatLng(loc.getLatitude(),loc.getLongitude()),mPuntos);
	}

	private boolean isPointInPolygon(LatLng tap, List<LatLng> vertices) {
	    int intersectCount = 0;
	    
	    for(int j=0; j<vertices.size()-1; j++) {
	        if( rayCastIntersect(tap, vertices.get(j), vertices.get(j+1)) ) {
	            intersectCount++;
	        }
	    }

	    return (intersectCount % 2) == 1; // odd = inside, even = outside;
	}

	private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

	    double aY = vertA.latitude;
	    double bY = vertB.latitude;
	    double aX = vertA.longitude;
	    double bX = vertB.longitude;
	    double pY = tap.latitude;
	    double pX = tap.longitude;

	    if ( (aY>pY && bY>pY) || (aY<pY && bY<pY) || (aX<pX && bX<pX) ) {
	        return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
	    }

	    double m = (aY-bY) / (aX-bX); // Rise over run
	    double bee = (-aX) * m + aY;  // y = mx + b
	    double x = (pY - bee) / m;    // algebra is neat!

	    return x > pX;
	}

	
	
	
	
}
