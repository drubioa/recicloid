package es.recicloid.utils.conections;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import es.recicloid.models.ProvisionalAppointment;
import es.recicloid.utils.json.JSONConverter;
import android.content.Context;
import android.os.AsyncTask;

public class ConectorToGetProvisAppointment 
	extends AsyncTask<InfoToGetProvAppointments,Void,List<ProvisionalAppointment>>{
	public Exception exception;
	private ConectorTask conector;
	
	public ConectorToGetProvisAppointment(Context context){
		conector = new ConectorTask(context);
	}
	
	@Override
	protected List<ProvisionalAppointment> doInBackground(
			InfoToGetProvAppointments... params) {
		 List<ProvisionalAppointment> list = null;
		for(InfoToGetProvAppointments info : params){
			try{
				HttpGet getRequest =  
						new HttpGet("/FurnitureCollectionService/resources/appointment"
								+"?phone_number="+info.phone
								+"&num_funritures="+info.num_furnitures
								+"&collection_point_id="+info.collection_point_id);
				getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
	
				HttpResponse httpResponse = 
						conector.getHttpClient().execute(
								conector.getTarget(), getRequest);
				String respStr = EntityUtils.toString(httpResponse.getEntity());
				if(httpResponse.getStatusLine().getStatusCode() != 200){
					throw new RuntimeException("Failed : HTTP error code : "
							 + httpResponse.getStatusLine().getStatusCode());	
				}
				JSONObject respJSON = new JSONObject(respStr);
				if( httpResponse.getEntity() != null ) {
					httpResponse.getEntity().consumeContent();
			    }
				list = JSONConverter.JSONtoProvisionalAppointment(respJSON);
			}catch(Exception e){
				exception = e;
			}
		}
		return list;
	}

}
