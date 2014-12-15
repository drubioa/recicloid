package es.recicloid.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng ;

import es.recicloid.parser.ZoneParser;

public class Zone {
	private List<LatLng> mPuntos;
	private Context context;
	
	public Zone(String filename,Context context) 
			throws IOException, XmlPullParserException{
		this.context = context;
		parserZone(filename); 
	}
	
	/**
	 * Dado el nombre de un fichero xml genera una zona correspondiente a un
	 * area de la localidad
	 * @param xmlFileName
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private void parserZone(String xmlFileName) throws IOException,
		XmlPullParserException{
		ZoneParser parser = new ZoneParser();
		InputStream in = context.getAssets().open(xmlFileName);
		mPuntos = parser.parse(in);
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
		return isPointInPolygon(new LatLng(loc.getLatitude(),
				loc.getLongitude()),mPuntos);
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
