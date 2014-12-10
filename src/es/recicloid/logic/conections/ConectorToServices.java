package es.recicloid.logic.conections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;

import es.recicloid.json.JSONConverter;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * Clase que represente objetos conectores con el servicio encargado de 
 * localizar los puntos de recogida mas cercanos.
 * @author Diego Rubio Abujas
 *
 */
public abstract class ConectorToServices{
	protected HttpHost target;
	protected DefaultHttpClient httpclient;
	protected JSONConverter jsonConverter;
	public String HOST;
	public int PORT;
	
	
	public ConectorToServices(Context context) 
			throws IOException{
		AssetManager am = context.getAssets();
		InputStream is = am.open("config-connection.txt");
		readConnectionConfig(is);
		is.close();	
		httpclient = new DefaultHttpClient();
		target = new HttpHost(HOST, PORT,"http");	
		jsonConverter = new JSONConverter();
	}
	
	/**
	 * Read connection file and assign parameters.
	 * @param is
	 * @throws IOException
	 */
	private void readConnectionConfig(InputStream is) throws IOException{
		String str="";
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		if (is!=null) {						
			while ((str = reader.readLine()) != null) {	
				StringTokenizer st = new StringTokenizer (str);
				String title = st.nextToken();
				if(title.equals("HOST")){
					HOST = st.nextToken();
				}
				else if(title.equals("PORT")){
					PORT = Integer.parseInt(st.nextToken());
				}
			}				
		}		
	}
}
