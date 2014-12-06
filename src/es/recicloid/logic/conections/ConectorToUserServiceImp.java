package es.recicloid.logic.conections;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;

public class ConectorToUserServiceImp  extends ConectorToServices 
implements ConectorToUserService{

	public ConectorToUserServiceImp(Context context) throws IOException {
		super(context);
	}

	public HttpResponse postNewUser(String userName,String phoneNumber) 
			throws Exception{
		try{
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
		catch(Exception e){
			throw e;
		}
	}

}
