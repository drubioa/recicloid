package es.recicloid.utils.conections;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import es.recicloid.models.CollectionRequest;
import es.recicloid.models.ProvisionalAppointment;
import es.recicloid.utils.json.JSONConverter;

public class ConectorToDailyAppointmentServiceImp 
extends ConectorToServices implements ConectorToDailyAppointmentService{

	public ConectorToDailyAppointmentServiceImp(Context context)
			throws IOException {
		super(context);
	}

	@Override
	public List<ProvisionalAppointment> getProvisionalAppointments(
			String phone, int num_furnitures, int collection_point_id)
					throws URISyntaxException, HttpException, IOException, JSONException, 
					ParseException{
				HttpGet getRequest =  
						new HttpGet("/FurnitureCollectionService/resources/appointment"
								+"?phone_number="+phone
								+"&num_funritures="+num_furnitures
								+"&collection_point_id="+collection_point_id);
				getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);

				HttpResponse httpResponse = 
						httpclient.execute(target, getRequest);
				String respStr = EntityUtils.toString(httpResponse.getEntity());
				if(httpResponse.getStatusLine().getStatusCode() != 200){
					throw new RuntimeException("Failed : HTTP error code : "
							 + httpResponse.getStatusLine().getStatusCode());	
				}
				JSONObject respJSON = new JSONObject(respStr);
				if( httpResponse.getEntity() != null ) {
					httpResponse.getEntity().consumeContent();
			    }
				return JSONConverter.JSONtoProvisionalAppointment(respJSON);
	}

	public List<CollectionRequest> getPendingRequest(String phone) 
			throws ClientProtocolException, IOException, JSONException{
		if(phone == null){
			throw new NullPointerException("Phone argument is null in getPendingCollectionRequest method");
		}
		HttpGet getRequest =  
				new HttpGet("/FurnitureCollectionService/resources/collectionrequests"
						+"?phone_number="+phone);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse = 
				httpclient.execute(target, getRequest);
		if(httpResponse == null){
			throw new NullPointerException("httpResponse is null");
		}
		
		if(httpResponse.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());	
		}
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		JSONObject respJSON = new JSONObject(respStr);
		if( httpResponse.getEntity() != null ) {
			httpResponse.getEntity().consumeContent();
	    }
		return JSONConverter.JSONtoCollectionRequest(respJSON);
	}
	
	@Override
	public HttpResponse confirmAppointment(CollectionRequest appointment)
			throws URISyntaxException, HttpException, IOException, JSONException{
		HttpPost post  = new
				HttpPost("/FurnitureCollectionService/resources/appointment");
		post.setHeader("content-type", MediaType.APPLICATION_JSON);
		if(!(appointment.getFch_collection()).isAfter(appointment.getFch_request())){
			throw new RuntimeException("Bad dates of appointment");
		}
		JSONObject dato;
		try {
			dato = JSONConverter.CollectionRequestToJSON(appointment);
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		StringEntity entity = new StringEntity(dato.toString());
		post.setEntity(entity);
		HttpResponse resp = httpclient.execute(target, post);
		if (resp.getStatusLine().getStatusCode() != 200) {					
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		if( resp.getEntity() != null ) {
			resp.getEntity().consumeContent();
	    }
		return resp;
	}

	@Override
	public void deletePendingAppointments(String phone_number)
			throws URISyntaxException, HttpException, IOException{

		HttpDelete deleteRestues = new
				HttpDelete("/FurnitureCollectionService/resources/appointment"
						+"?phone_number="+phone_number);
		deleteRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse resp = httpclient.execute(target, deleteRestues);

		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode()+"\n"+resp.getParams());
		}
		if(resp.getEntity() != null) {
			resp.getEntity().consumeContent();
	    }
	}

}
