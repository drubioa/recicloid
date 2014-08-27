package es.recicloid.logic.conections;

import org.json.JSONException;
import org.json.JSONObject;

import es.recicloid.clases.CollectionPoint;

public class JSONConverter {

	public JSONConverter(){
		
	}
	
	/**
	 * Convierte un objeto de tipo JSON en un punto de recogida
	 * @param object
	 * @return
	 * @throws JSONException
	 */
	public CollectionPoint JSONToCollectionPoint(JSONObject object) 
			throws JSONException{
		CollectionPoint point = new CollectionPoint(
				object.getDouble("longitude"),
				object.getDouble("latitude"));
		return point;
	}
	
	/**
	 * Se obtiene un objeto JSON a partir de un nombre y telefono de usuario.
	 * @param user_name
	 * @param phone
	 * @return
	 * @throws JSONException
	 */
	public JSONObject userToJson(String user_name,String phone)
			throws JSONException{
		JSONObject dato = new JSONObject();
		dato.put("name", user_name);
		dato.put("phone_number", phone);
		return dato;
	}
}
