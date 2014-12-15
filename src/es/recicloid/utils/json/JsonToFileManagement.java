package es.recicloid.utils.json;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.recicloid.models.CollectionPoint;
import es.recicloid.models.Furniture;
import es.recicloid.models.User;

public class JsonToFileManagement {
	private Context context;
	private String FILENAME;
	
	
	public JsonToFileManagement(Context context){
		this.context = context;
	}
	
	public JsonToFileManagement(Context context,String FILENAME){
		this.context = context;
		this.FILENAME = FILENAME;
	}
	
	public void changeFileName(String newFilename){
		this.FILENAME = newFilename;
	}
	
	/**
	 * Save user name and telephone in file.
	 * @throws IOException 
	 */
	public void saveInJsonFile(Serializable obj) 
			throws IOException{
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		OutputStreamWriter os = new OutputStreamWriter(fos);
		os.write(json);
		os.close();		
	}
	
	/**
	 * Save user name and telephone in file.
	 * @throws IOException 
	 */
	public void saveFurnituresInJsonFile(List<Furniture> obj) 
			throws IOException{
		FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String json = gson.toJson(obj);
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
