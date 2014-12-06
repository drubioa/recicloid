package es.recicloid.logic.conections;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import es.recicloid.clases.CollectionPoint;

import android.content.Context;
import android.location.Location;

public class ConectorToCollectionPointServiceImp extends ConectorToServices
	implements ConectorToCollectionPointService{

	public ConectorToCollectionPointServiceImp(Context context) throws IOException {
		super(context);
	}

	public CollectionPoint getNearestCollectionPoint(Location point,
			boolean isRuralZone) throws ClientProtocolException, IOException, JSONException{
			// Create connection
			HttpGet getRequest = 
					new HttpGet("/FurnitureCollectionService/resources/point"
							+"?lat="+point.getLatitude()
							+"&lng="+point.getLongitude()
							+"&isRuralArea="+isRuralZone);
			getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
			HttpResponse httpResponse = 
					httpclient.execute(target, getRequest);
			if(httpResponse.getStatusLine().getStatusCode() != 200){
				throw new RuntimeException("Failed : HTTP error code : "
						 + httpResponse.getStatusLine().getStatusCode());
			}
			// handle response
			String respStr = EntityUtils.toString(httpResponse.getEntity());
			JSONObject respJSON = new JSONObject(respStr);
			CollectionPoint p = jsonConverter.JSONToCollectionPoint(respJSON);
			return p;
		
	}
	
	
}
