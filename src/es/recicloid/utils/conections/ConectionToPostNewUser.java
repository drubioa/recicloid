package es.recicloid.utils.conections;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import es.recicloid.models.User;
import es.recicloid.utils.json.JSONConverter;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Clase para efectuar de manera asincrona conexion al servidor con la finalidad
 * de generar un nuevo usuario.
 * @author diegorubio
 *
 */
public class ConectionToPostNewUser extends AsyncTask<User,Void,Boolean>{
	private ConectorTask conector;
	public Exception exception;
	
	public ConectionToPostNewUser(Context context){
		conector = new ConectorTask(context);
	}
	
	@Override
	protected Boolean doInBackground(User... params) {
		for(User u : params){
			HttpPost postRestues = new
					HttpPost("/FurnitureCollectionService/resources/users");
			postRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
			JSONObject dato;			
			try {
				StringEntity entity;
				dato = JSONConverter.userToJson(u.getName(),u.getPhone());
				entity = new StringEntity(dato.toString());
				entity.setContentType(MediaType.APPLICATION_JSON);
				postRestues.setEntity(entity);
				HttpResponse resp = conector.getHttpClient()
						.execute(conector.getTarget(), postRestues);
				if (resp.getStatusLine().getStatusCode() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
					 + resp.getStatusLine().getStatusCode());
				}
				if( resp.getEntity() != null ) {
					resp.getEntity().consumeContent();
			    }
			} catch (Exception e) {
				Log.e("ConectionToPostNewUser",e.toString());
				exception = e;
				return false;
			}
			
		}
		return true;
	}

}
