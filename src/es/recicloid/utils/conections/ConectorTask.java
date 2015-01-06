package es.recicloid.utils.conections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.AssetManager;

public class ConectorTask {
	public Exception exception;
	protected HttpHost target;
	protected DefaultHttpClient httpclient;
	public static String HOST;
	public static int PORT;
	
	public ConectorTask(Context context){
		AssetManager am = context.getAssets();
		InputStream is;
		try {
			is = am.open("config-connection.txt");
			readConnectionConfig(is);
			is.close();	
		} catch (IOException e) {
			exception = e;
		}
		httpclient = new DefaultHttpClient();
		target = new HttpHost(HOST, PORT,"http");	
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
	
	public HttpHost getTarget(){
		return target;
	}
	
	public DefaultHttpClient getHttpClient(){
		return httpclient;
	}
}
