package es.recicloid.json;

import org.json.JSONException;
import org.json.JSONObject;

import es.recicloid.clases.CollectionPoint;

public class JSONConverterImp implements JSONConverter{

	public JSONConverterImp(){
		
	}
	
	public CollectionPoint JSONToCollectionPoint(JSONObject object) 
			throws JSONException{
		CollectionPoint point = new CollectionPoint(
				object.getDouble("longitude"),
				object.getDouble("latitude"));
		return point;
	}
	
	public JSONObject userToJson(String user_name,String phone)
			throws JSONException{
		JSONObject dato = new JSONObject();
		dato.put("name", user_name);
		dato.put("phone_number", phone);
		return dato;
	}
}
