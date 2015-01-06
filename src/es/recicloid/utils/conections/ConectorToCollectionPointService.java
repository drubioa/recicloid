package es.recicloid.utils.conections;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import es.recicloid.models.CollectionPoint;
import es.recicloid.utils.json.JSONConverter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ConectorToCollectionPointService 
	extends AsyncTask<InfoToFindCollectionPoint,Void,CollectionPoint>{

	private ConectorTask conector;
	public Exception exception;
	
	public ConectorToCollectionPointService(Context context){
		conector = new ConectorTask(context);
	}

	@Override
	protected CollectionPoint doInBackground(
			InfoToFindCollectionPoint... params) {
		InfoToFindCollectionPoint info = params[0];
		CollectionPoint p = null;
		Log.i("ConectorToCollectionPointService",
				"Estableciendo conexion");
		HttpGet getRequest = 
				new HttpGet("/FurnitureCollectionService/resources/point"
								+"?lat="+info.point.getLatitude()
								+"&lng="+info.point.getLongitude()
								+"&isRuralArea="+info.isRuralZone);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse;
		try {
			httpResponse = conector.getHttpClient()
					.execute(conector.getTarget(), getRequest);
			if(httpResponse.getStatusLine().getStatusCode() != 200){
				Log.e("ConectorToCollectionPointService",
						"Failed : HTTP error code : "
								+ httpResponse.getStatusLine().getStatusCode());
				throw new RuntimeException("Failed : HTTP error code : "
				+ httpResponse.getStatusLine().getStatusCode());
			}
			Log.i("ConectorToCollectionPointService","Manejando fichero JSON");
			// handle response
			String respStr = EntityUtils.toString(httpResponse.getEntity());
			JSONObject respJSON = new JSONObject(respStr);
			p = JSONConverter.JSONToCollectionPoint(respJSON);
		} catch (Exception e) {
			Log.e("ConectorToCollectionPointService",
					e.toString());
			this.exception = e;
		}
		return p;
	}
	
	
}
