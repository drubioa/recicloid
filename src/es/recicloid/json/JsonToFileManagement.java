package es.recicloid.json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.recicloid.clases.CollectionPoint;
import es.recicloid.clases.Furniture;
import es.recicloid.clases.User;

public class JsonToFileManagement {
	private Context context;
	private final String FILENAME;
	
	public JsonToFileManagement(Context context,String FILENAME){
		this.context = context;
		this.FILENAME = FILENAME;
	}
	
	/**
	 * Save user name and telephone in file.
	 * @throws IOException 
	 */
	public void saveUserInJsonFile(String name,String phone) 
			throws IOException{
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		User user = new User(name,phone);
		Gson gson = new Gson();
		String json = gson.toJson(user);
		OutputStreamWriter os = new OutputStreamWriter(fos);
		os.write(json);
		os.close();		
	}
	
	/**
	 * Load User from internal json file.
	 * @return User
	 * @throws IOException
	 */
	public User loadUserForJsonFile() throws IOException{
		FileInputStream fin = context.openFileInput(FILENAME);
		int c;
		String temp="";
		while( (c = fin.read()) != -1){
		   temp = temp + Character.toString((char)c);
		}
		//string temp contains all the data of the file.
		fin.close();
		Gson gson = new Gson();
		return gson.fromJson(temp,User.class);
	}
	
	/**
	 * Save list of furnitures in json file.
	 * @throws IOException 
	 */
	public void saveFurnituresInJsonFile(List<Furniture> furnitures) 
			throws IOException{
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = gson.toJson(furnitures);
		OutputStreamWriter os = new OutputStreamWriter(fos);
		os.write(json);
		os.close();		
	}
	
	/**
	 * Load list of furnitures to collection request from internal json file.
	 * @return List<Furniture> list of furnitures to collection request.
	 * @throws IOException
	 */
	public List<Furniture> loadFurnituresFromLocalFile() throws IOException{
		FileInputStream fin = context.openFileInput(FILENAME);
		int c;
		String temp="";
		while( (c = fin.read()) != -1){
		   temp = temp + Character.toString((char)c);
		}
		//string temp contains all the data of the file.
		fin.close();
		Gson gson = new Gson();
		Type list = new TypeToken<List<Furniture>>(){}.getType();
		List<Furniture> furnitures = gson.fromJson(temp, list);
		return furnitures;
	}
	
	/**
	 * Save a collectionPoint in local json file.
	 * @param point
	 * @throws IOException
	 */
	public void saveCollectionPointInJsonFile(CollectionPoint point) 
			throws IOException{
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = gson.toJson(point);
		OutputStreamWriter os = new OutputStreamWriter(fos);
		os.write(json);
		os.close();				
	} 
	
	/**
	 * Load list of furnitures to collection request from internal json file.
	 * @return List<Furniture> list of furnitures to collection request.
	 * @throws IOException
	 */
	public CollectionPoint loadCollectionPointFromLocalFile() throws IOException{
		FileInputStream fin = context.openFileInput(FILENAME);
		int c;
		String temp="";
		while( (c = fin.read()) != -1){
		   temp = temp + Character.toString((char)c);
		}
		//string temp contains all the data of the file.
		fin.close();
		Gson gson = new Gson();
		return gson.fromJson(temp,CollectionPoint.class);
	}
	
}
