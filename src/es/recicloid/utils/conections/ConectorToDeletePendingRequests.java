package es.recicloid.utils.conections;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;

import android.content.Context;
import android.os.AsyncTask;

public class ConectorToDeletePendingRequests 
	extends AsyncTask<String,Void,Boolean>{
	public Exception exception;
	private ConectorTask conector;
	
	public ConectorToDeletePendingRequests(Context context){
		conector = new ConectorTask(context);
	}
	
	/**
	 * Se cancelan todas las solicitudes de recogida  pendientes de realizar
	 * de los usuarios con el telefono indicado en los parametros.
	 */
	@Override
	protected Boolean doInBackground(String... params) {
		for(String telephone : params){
			HttpDelete deleteRestues = new
					HttpDelete("/FurnitureCollectionService/resources/appointment"
							+"?phone_number="+telephone);
			deleteRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
			HttpResponse resp;
			try {
				resp = conector.getHttpClient()
						.execute(conector.getTarget(), deleteRestues);
			} catch (Exception e) {
				exception = e;
				return false;
			}

			if (resp.getStatusLine().getStatusCode() == 500) {
				throw new RuntimeException("Failed : HTTP error code : "
				 + resp.getStatusLine().getStatusCode()+"\n"+resp.getParams());
			}
			if(resp.getEntity() != null) {
				try {
					resp.getEntity().consumeContent();
				} catch (IOException e) {
					exception = e;
					return false;
				}
		    }
		}
		return true;
	}

}
