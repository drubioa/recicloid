package es.recicloid.utils.conections;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.location.Location;
import es.recicloid.models.CollectionPoint;

public interface ConectorToCollectionPointService {

	/**
	 * Se localiza el punto de recogida mas cercano a un punto dado.
	 * @param point ubicacion geografica de un punto.
	 * @return punto correspondiente a un punto de recogido mas cercano al parametro.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JSONException 
	 * @throws Exception
	 */
	public CollectionPoint getNearestCollectionPoint(Location point,
			boolean isRuralZone) throws ClientProtocolException, IOException, JSONException;
	
}
