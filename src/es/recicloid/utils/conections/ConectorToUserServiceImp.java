package es.recicloid.utils.conections;

import java.io.IOException;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import es.recicloid.models.User;
import es.recicloid.utils.json.JSONConverter;

import android.content.Context;

public class ConectorToUserServiceImp  extends ConectorToServices 
implements ConectorToUserService{
	
	public ConectorToUserServiceImp(Context context) throws IOException {
		super(context);
	}

	public HttpResponse postNewUser(String userName,String phoneNumber) 
			throws JSONException, ClientProtocolException, IOException{
			HttpPost postRestues = new
					HttpPost("/FurnitureCollectionService/resources/users");
			postRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
			JSONObject dato = JSONConverter.userToJson(userName,phoneNumber);
			StringEntity entity = new StringEntity(dato.toString());
			entity.setContentType(MediaType.APPLICATION_JSON);
			postRestues.setEntity(entity);
			HttpResponse resp = httpclient.execute(target, postRestues);
			if (resp.getStatusLine().getStatusCode() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				 + resp.getStatusLine().getStatusCode());
			}
			if( resp.getEntity() != null ) {
				resp.getEntity().consumeContent();
		    }
			return resp;	
	}

	public HttpResponse deleteUser(User user) throws Exception{
		HttpDelete deleteRestues = new
				HttpDelete("/FurnitureCollectionService/resources/users"
						+"/"+user.getPhone());
		deleteRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse resp = httpclient.execute(target, deleteRestues);
		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		if( resp.getEntity() != null ) {
			resp.getEntity().consumeContent();
	    }
		return resp;
	}
}
