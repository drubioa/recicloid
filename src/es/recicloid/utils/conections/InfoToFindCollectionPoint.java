package es.recicloid.utils.conections;

import android.location.Location;

/**
 * Clase empleada para la tarea asincrona de Conector To Collection Point Service.
 * @author diegorubio
 *
 */
public class InfoToFindCollectionPoint {
	public Location point;
	public boolean isRuralZone;
	
	public InfoToFindCollectionPoint(Location valid_location, boolean b) {
		point = valid_location;
		isRuralZone = b;
	}
}
