package es.recicloid.utils.conections;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

import es.recicloid.models.CollectionRequest;
import es.recicloid.utils.json.JSONConverter;

public class ConectorToGetAppointments 
	extends AsyncTask<String,Void,List<CollectionRequest>>{
	public Exception exception;
	private ConectorTask conector;
	
	public ConectorToGetAppointments(Context context){
		conector = new ConectorTask(context);
	}
	
	/**
	 * Conecta de forma asincrona, obtiene todas las peticiones de un usuarios.
	 * Devuelve nulo en caso de que no se pueda realizar la operacion.
	 */
	public List<CollectionRequest> doInBackground(String... phones){
		String phone = phones[0];
		List<CollectionRequest> list = null;
		if(phone == null){
			throw new NullPointerException("Phone argument is null in getPendingCollectionRequest method");
		}
		HttpGet getRequest =  
				new HttpGet("/FurnitureCollectionService/resources/collectionrequests"
						+"?phone_number="+phone);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse;
		try {
			httpResponse = conector.getHttpClient()
					.execute(conector.getTarget(), getRequest);
			if(httpResponse == null){
				throw new NullPointerException("httpResponse is null");
			}
			// Si se produce un error lanza excepcion en tiempo de ejcucion.
			if(httpResponse.getStatusLine().getStatusCode() != 200){
				throw new RuntimeException("Failed : HTTP error code : "
						 + httpResponse.getStatusLine().getStatusCode());	
			}
			String respStr = EntityUtils.toString(httpResponse.getEntity());
			JSONObject respJSON = new JSONObject(respStr);
			if( httpResponse.getEntity() != null ) {
				httpResponse.getEntity().consumeContent();
		    }
			list = JSONConverter.JSONtoCollectionRequest(respJSON);
			// Si se ha producido alguna excepcion al conectar se almacena.
			if(conector.exception != null){
				exception = conector.exception;
			}
		} catch (Exception e) {
			exception = e;
		}
		return list;
	}
}
