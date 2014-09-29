package es.recicloid.logic.conections;

import java.io.IOException;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

import es.recicloid.clases.CollectionPoint;

/**
 * Clase que represente objetos conectores con el servicio encargado de 
 * localizar los puntos de recogida mas cercanos.
 * @author Diego Rubio Abujas
 *
 */
public class ConectorToServices{
	HttpHost target;
	DefaultHttpClient httpclient;
	JSONConverter jsonConverter;
	// Provisionalmente en modo local
	final String HOST = "192.168.1.13";
	final int PORT = 8080;
	
	public ConectorToServices(){
		httpclient = new DefaultHttpClient();
		target = new HttpHost(HOST, PORT,"http");	
		jsonConverter = new JSONConverter();
	}
	
	/**
	 * Se localiza el punto de recogida mas cercano a un punto dado.
	 * @param point ubicacion geografica de un punto.
	 * @return punto correspondiente a un punto de recogido mas cercano al parametro.
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public CollectionPoint getNearestCollectionPoint(Location point,
			boolean isRuralZone) 
			throws JSONException, ParseException, IOException{
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
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		JSONObject respJSON = new JSONObject(respStr);
		CollectionPoint p = jsonConverter.JSONToCollectionPoint(respJSON);
		return p;
	}
	
	/**
	 * Se crea un nuevo usuario que solicita recogida de muebles y enseres.
	 * @param userName
	 * @param phoneNumber
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public HttpResponse postNewUser(String userName,String phoneNumber) 
			throws ClientProtocolException, IOException, JSONException{
		HttpPost postRestues = new
				HttpPost("/FurnitureCollectionService/resources/users");
		postRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
		JSONObject dato = jsonConverter.userToJson(userName,phoneNumber);
		StringEntity entity = new StringEntity(dato.toString());
		entity.setContentType(MediaType.APPLICATION_JSON);
		postRestues.setEntity(entity);
		HttpResponse resp = httpclient.execute(target, postRestues);
		if (resp.getStatusLine().getStatusCode() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		return resp;	
	}

}
