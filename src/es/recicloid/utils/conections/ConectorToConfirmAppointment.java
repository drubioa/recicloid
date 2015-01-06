package es.recicloid.utils.conections;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import es.recicloid.models.CollectionRequest;
import es.recicloid.utils.json.JSONConverter;
import android.content.Context;
import android.os.AsyncTask;

public class ConectorToConfirmAppointment 
	extends AsyncTask<CollectionRequest,Void,Boolean>{
	public Exception exception;
	private ConectorTask conector;
	
	public ConectorToConfirmAppointment(Context context){
		conector = new ConectorTask (context);
	}
	
	
	@Override
	protected Boolean doInBackground(CollectionRequest... params) {
		for(CollectionRequest c : params){
			try{
				HttpPost post  = new
						HttpPost("/FurnitureCollectionService/resources/appointment");
				post.setHeader("content-type", MediaType.APPLICATION_JSON);
				if(!(c.getFch_collection()).isAfter(c.getFch_request())){
					throw new RuntimeException("Bad dates of appointment");
				}
				JSONObject dato;
				try {
					dato = JSONConverter.CollectionRequestToJSON(c);
				} catch (JSONException e) {
					e.printStackTrace();
					throw e;
				}
				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);
				HttpResponse resp = conector.getHttpClient()
						.execute(conector.getTarget(), post);
				if (resp.getStatusLine().getStatusCode() != 200) {					
					throw new RuntimeException("Failed : HTTP error code : "
					 + resp.getStatusLine().getStatusCode());
				}
				if( resp.getEntity() != null ) {
					resp.getEntity().consumeContent();
			    }
			}catch(Exception e){
				exception = e;
				return false;
			}
		}
		return true;
	}

}
