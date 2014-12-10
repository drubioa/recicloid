package es.recicloid.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.recicloid.clases.CollectionPoint;
import es.recicloid.clases.CollectionRequest;
import es.recicloid.clases.Furniture;
import es.recicloid.clases.ProvisionalAppointment;

public class JSONConverter{

	public JSONConverter(){
		
	}
	
	public static CollectionPoint JSONToCollectionPoint(JSONObject object) 
			throws JSONException{
		CollectionPoint point = new CollectionPoint(
				object.getDouble("longitude"),
				object.getDouble("latitude"));
		return point;
	}
	
	public static JSONObject userToJson(String user_name,String phone)
			throws JSONException{
		JSONObject dato = new JSONObject();
		dato.put("name", user_name);
		dato.put("phone_number", phone);
		return dato;
	}
	
	public static JSONObject CollectionRequestToJSON(CollectionRequest appointment) 
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("telephone", appointment.getTelephone());
		obj.put("collectionPointId", appointment.getCollectionPointId());
		obj.put("collectionDay", appointment.getFch_collection().getDayOfMonth());
		obj.put("collectionMonth", appointment.getFch_collection().getMonthOfYear());
		obj.put("collectionYear", appointment.getFch_collection().getYear());
		obj.put("requestDay", appointment.getFch_request().getDayOfMonth());
		obj.put("requestMonth", appointment.getFch_request().getMonthOfYear());
		obj.put("requestYear", appointment.getFch_request().getYear());
		obj.put("num_furnitures",appointment.getNum_furnitures());
		JSONArray furnitures = new JSONArray();
		for(Furniture furniture : appointment.getFurnitures()){
			JSONObject item = new JSONObject();
			item.put("name", furniture.getIdText());
			item.put("cantidad", furniture.getmNum());
			furnitures.put(item);
		}
		obj.put("furnitures",  furnitures);
		return obj;
	}

	public static List<ProvisionalAppointment> JSONtoProvisionalAppointment(JSONObject arg)
			throws JSONException, ParseException {
		List<ProvisionalAppointment> appointments 
			= new ArrayList<ProvisionalAppointment>();
		JSONArray objects  = arg.getJSONArray("provisionalAppointment");
		for(int i = 0; i < objects.length();i++){
			JSONObject object = objects.getJSONObject(i);
	        ProvisionalAppointment appointment = new ProvisionalAppointment();
	        appointment.setCollectionPointId(object.getInt("collectionPointId"));
	        LocalDate fch_collection = new LocalDate(object.getString("fch_collection"));
	        appointment.setFch_collection(fch_collection);
	        LocalDate fch_request = new LocalDate(object.getString("fch_request"));
	        appointment.setFch_request(fch_request);
	        appointment.setNum_furnitures(object.getInt("numFurnitures"));
	        appointment.setTelephone(object.getString("telephone"));
	        appointments.add(appointment);
		}
		return appointments;
	}
}
