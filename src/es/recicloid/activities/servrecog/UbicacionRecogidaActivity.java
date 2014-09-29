package es.recicloid.activities.servrecog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import es.recicloid.clases.CollectionPoint;
import es.recicloid.clases.Furniture;
import es.recicloid.dialogs.DialogAlert;
import es.recicloid.dialogs.DialogSelecLocationType;
import es.recicloid.logic.conections.ConectorToServices;
import es.recicloid.logic.map.AddressTracker;
import es.recicloid.logic.map.GetAddressTask;
import es.recicloid.logic.map.LocationTracker;
import es.recicloid.logic.map.Zone;
import es.recicloid.logic.map.ZoneParser;
import es.uca.recicloid.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UbicacionRecogidaActivity extends FragmentActivity {
	private boolean mLocalizado;
	private boolean mShowedRuralProcAdvice;
	private boolean mIsRuralPoint;
	private double mLongitud;
	private double mLatitud;
	private ArrayList<Furniture> furnitures;
	private GoogleMap map;	
	private EditText editText;
	private Button btnNextStep; 
	private ConectorToServices conector;
	
	//private String direccion;
	private final LatLng LOCAL = 
			new LatLng(36.530375900000000000, -6.194416899999965000);
	AddressTracker localizador;
	private Zone urban , terminoMunicipal;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubicacion_recogida);
		btnNextStep = (Button) findViewById(R.id.buttonContinuar);
		conector = new ConectorToServices();
		
		if(!checkPlayServicesIsInstalled()){
			btnNextStep.setEnabled(false);
			FragmentManager fragmentManager = getSupportFragmentManager();
			Log.e("UbicacionRecogidaActivity","Play Services unviable");
			Bundle args = new Bundle();
			args.putInt("title", R.string.dialog_err_google_play_title);
			args.putInt("description",R.string.dialog_err_google_play_descr);
			DialogAlert newFragment = DialogAlert.newInstance(args);
			newFragment.show(fragmentManager, "tagAvisoLocNotValid");
		}
		else{
			map = ((SupportMapFragment) getSupportFragmentManager()
	                .findFragmentById(R.id.map)).getMap();
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			Log.i("UbicacionRecogidaActivity", "found and set GoogleMap.");
			editText = (EditText) findViewById(R.id.editText1);
			
			try {
				Log.i("UbicacionRecogidaActivity", "xml were parser.");
				urban = parserZone("urban-zone.xml");
				terminoMunicipal = parserZone("municipal-area.xml");
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("UbicacionRecogidaActivity","Cannot parser xml file ("
						+e.toString()+")");
			}
			
			
			if(savedInstanceState == null){
				editText.setText("Direcci??n de recogida, num");
				showsGenericViewOfCity();
				Log.i("UbicacionRecogidaActivity", "Showed generic view.");
				Intent intent = getIntent();
				furnitures = intent.getParcelableArrayListExtra("itemsSelected");
				mLocalizado = false;
				Log.i("UbicacionRecogidaActivity", 
						"Se procede a mostrar ventana inicial..");
				showLocationAutoOrManualDialog();
				mShowedRuralProcAdvice = false;
				btnNextStep.setEnabled(false);
			}		
			else{
				mLocalizado = savedInstanceState.getBoolean("mLocalizado");
				mLatitud = savedInstanceState.getDouble("mLatitud");
				mLongitud = savedInstanceState.getDouble("mLongitud");
				mShowedRuralProcAdvice = savedInstanceState.
						getBoolean("ShowedRuralProcAdvice");
				mIsRuralPoint = savedInstanceState.getBoolean("isRuralPoint");
				
				if(mLocalizado){
					Log.i("UbicacionRecogidaActivity", 
							"Se ha localizado correctamente.");
					LatLng point = new LatLng(mLatitud,mLongitud);
					addMarkToPosition(point);
					showChosenPointInMap(point);
					btnNextStep.setEnabled(true);
				}
				else{
					// By default shows the general city view
					Log.i("UbicacionRecogidaActivity",
							"No se ha localizado.Se procede"
							+ "a mostrar una vista generica de la localidad en el mapa.");
					showsGenericViewOfCity();
					btnNextStep.setEnabled(false);
				}
			}
			setListeners();
		}
		
	}
	
	/**
	 * Dado el nombre de un fichero xml genera una zona correspondiente a un
	 * area de la localidad
	 * @param xmlFileName
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private Zone parserZone(String xmlFileName) throws IOException,
		XmlPullParserException{
		ZoneParser parser = new ZoneParser();
		InputStream in = getAssets().open(xmlFileName);
		Zone zone = new Zone(parser.parse(in));
		return zone;
	}
	
	/**
	 * Creates Listener for Map and EditText 
	 */
	public void setListeners(){
				
		// Listeners of GoogleMap
		map.setOnMapClickListener(new OnMapClickListener() {

	        @Override
	        /**
	         * If the user clicks on a map position is marked and stored 
	         * in the text field the street and number.
	         */
	        public void onMapClick(LatLng point) {
	        	Log.i("UbicacionRecogidaActivity","On click in point "
	        +point.toString());
				markCollectionPoint(convertLagIntToLocation(point));
	        }
	    });		
		
		// Listener del boton continuar con la solicitud
		btnNextStep.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Log.i("UbicacionRecogidaActivity", 
						"Se pulsa en el boton de Solicitud.");
				Intent intent = new Intent(UbicacionRecogidaActivity
						.this,DatosContactoActivity.class);
				intent.putExtra("punto_recogida", 
						new CollectionPoint(mLongitud,mLatitud));
				startActivity(intent);   
            }
        });
	}

	/**
	 * Se muestra un dialogo en el que se consulta que tipo
	 */
	public void showLocationAutoOrManualDialog(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		DialogSelecLocationType dialog = new DialogSelecLocationType();
		dialog.show(fragmentManager, "tagLocalizManOrAut");		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ubicacion_recogida, menu);
		return true;
	}

	/**
	 * Show general view in map of the city
	 */
	private void showsGenericViewOfCity(){
		CameraPosition camPos = new CameraPosition.Builder()
        .target(LOCAL)  
        .zoom(14)         
        .build();
		CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
		map.moveCamera(camUpd);
	}
	
	/**
	 * Muestra un punto seleccionado en el mapa de la localidad
	 * @param point
	 */
	private void showChosenPointInMap(LatLng point){
		CameraPosition camPos = new CameraPosition.Builder()
        	.target(point)  
        	.zoom(18)         
        	.build();
		CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
		map.moveCamera(camUpd);
		Log.i("UbicacionRecogidaActivity", 
				"Se muestra posicion escogida en el mapa.");
	}

	public void checkLocation(LatLng point){
		markCollectionPoint(convertLagIntToLocation(point));
	}
	
	/**
	 * Conecta con el servicio y obiene el punto de localización mas cercano
	 * @param point
	 * @return
	 */
	public CollectionPoint findNearestCollectionPoint(Location point){
		CollectionPoint p = null;
		try {
			p = conector.getNearestCollectionPoint(point, mIsRuralPoint);
		} catch (ParseException e) {
			Log.e("findNearestCollectionPoint",
					"Error internos al parsear el fichero xml");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("findNearestCollectionPoint",
					"Error en el tratamiento del fichero JSON");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("findNearestCollectionPoint",
					e.toString());
			e.printStackTrace();
		}
		return p;
	}
	
	/**
	 * Checks if point belong a valid zone, and if it is urban or rural zone.
	 * Shows dialogs if it fail and advice if it is rural zone.
	 * @param point
	 */
	public void markCollectionPoint(Location point){
		// To resolve resolve NetworkOnMainThreadException
		StrictMode.ThreadPolicy policy 
			= new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		try {
			if(point == null){
				Log.e("markCollectionPoint","point param is null");
				throw new Exception("markCollectionPoint function receives null param");
			}
			if(terminoMunicipal.isInside(point)){
				mIsRuralPoint = (isRuralPoint(point));
				Log.i("markCollectionPoint","location dentro del termino municipal");
				CollectionPoint nearestPoint = 
						conector.getNearestCollectionPoint(point, mIsRuralPoint);
				if(nearestPoint == null){
					Log.w("markCollectionPoint","no hay ningun punto de recogida cercano");
				}
				else{
					mLatitud = nearestPoint.getLat();
					mLongitud = nearestPoint.getLng();
					LatLng p = new LatLng(mLatitud,mLongitud);
					Log.i("markCollectionPoint",
							"Se obtiene el punto ("+mLatitud+","+mLongitud+").");
					addMarkToPosition(p);
					showChosenPointInMap(p);
					if(mIsRuralPoint && !mShowedRuralProcAdvice){
						showRuralProcessToCollectMessage();
					}
					obtainAddress(point);
				}
			}
			else{
				showErrorCannotLocalize();
			}
			
		}
		catch (Exception e) {
			Log.e("markCollectionPoint",e.toString());
		}
	}
	
	/**
	 * Se muestra un mensaje informando al usuario del proceso que se sigue
	 * para la recogida de enseres en areas rurales.
	 */
	private void showRuralProcessToCollectMessage(){
		Log.i("UbicacionRecogidaActivity","location in invalid area");
		mShowedRuralProcAdvice = true;
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", R.string.dialog_title_location_not_valid);
		args.putInt("description",R.string
				.dialog_descr_location_not_valid);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, "tagAvisoLocNotValid");		
	}
	
	/**
	 * Se muestra un mensaje de error debido a que no se ha indiado
	 * una localización dentro del término municipal.
	 */
	private void showErrorCannotLocalize(){
		Log.i("UbicacionRecogidaActivity",
				"No se puso marcar el punto ya que la localizacion no es valida");
		mLocalizado = false;
		btnNextStep.setEnabled(false);
		editText.setText("");
		showsGenericViewOfCity();
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", R.string.dialog_title_location_not_valid);
		args.putInt("description",R.string.dialog_descr_location_not_valid);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, "tagAvisoLocNotValid");		
	}
	
	/**
	 * Obtiene la direccion en formato texto y lo rellena en el campo editText
	 * @param point
	 */
	private void obtainAddress(Location point){
		GetAddressTask add = new GetAddressTask(this);
		Address address = add.obtainsAddress(point);
		if(address != null){
			Log.w("UbicacionRecogidaActivity","adress is: "
		+address.getAddressLine(0));
			editText.setText(address.getAddressLine(0));
		}
		else{
			Log.w("UbicacionRecogidaActivity","cannot obtain address");
		}	
	}
	
	/**
	 * Obtains location by clients mobile technology
	 * @throws Exception 
	 */
	public void getLocationByMobilePhone() throws Exception {
		LocationTracker locTracker = new LocationTracker(this);
		Location currentLocation = locTracker.getLocation();
		if(currentLocation == null){
			Toast msjError = Toast.makeText(getApplicationContext(),
    				getResources().getString(
    		        		R.string.dialog_err_location_exception), 
					Toast.LENGTH_SHORT);
			msjError.show();
			Log.e("UbicacionRecogidaActivity", 
					"LocationTracker cannot obtains current location");
		}
		else{
			markCollectionPoint(currentLocation);
		}
    }
	
	/**
	 * Draw q mark in a map with location point
	 * @param localizacion
	 */
	private void addMarkToPosition(LatLng localizacion){
		Log.i("addMarkToPosition",
				"Se marca la posicion ("+localizacion.latitude+"," +
						""+localizacion.longitude+").");
		this.map.clear();
		this.mLocalizado = true;
		this.btnNextStep.setEnabled(true);
		this.map.addMarker(new MarkerOptions()
        	.position(localizacion)
        	.title("Ubicaci??n")
        	.icon(BitmapDescriptorFactory.defaultMarker(
        		BitmapDescriptorFactory.HUE_GREEN)));
	}
	

	/**
	 * Transforms a LatLng to Location
	 * @param point
	 * @return Location correspondents to point dispatched
	 */
	private Location convertLagIntToLocation(LatLng point){
		  Location location = new Location("PuntoRecogida");
		  location.setLatitude(point.latitude);
		  location.setLongitude(point.longitude);
		  location.setTime(new Date().getTime());
		  return location;
	}
	
	/**
	 * 
	 * @param point
	 * @return true if the point is in a rural area
	 */
	private boolean isRuralPoint(Location point){
		return terminoMunicipal.isInside(point) && !urban.isInside(point);
	}
	
	/**
	 * Comprueba si el Play Service de Google esta disponible en 
	 * el terminal movil.
	 * En caso de que no este disponible muestra un mensaje de error
	 * @return
	 */
	private boolean checkPlayServicesIsInstalled() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
				return false;
		    } else {
		      errorPlaySevicesNotInstalled();
		      finish();
		    }
		    return false;
		  }
		  return true;
	}
	
	/**
	 * Se muestra un mensaje de error debido a que no se ha indiado
	 * una localización dentro del término municipal.
	 */
	private void errorPlaySevicesNotInstalled(){
		Log.i("UbicacionRecogidaActivity",
				"Google Plays Services not installed");
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", R.string.dialog_err_google_play_title);
		args.putInt("description",R.string.dialog_err_google_play_descr	);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, "tagAvisoLocNotValid");		
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
	   outState.putBoolean("mLocalizado", mLocalizado);
	   outState.putDouble("mLongitud", mLongitud);
	   outState.putDouble("mLatitud", mLatitud);
	   outState.putParcelableArrayList("itemsSelected", furnitures);
	   outState.putBoolean("ShowedRuralProcAdvice", mShowedRuralProcAdvice);
	   outState.putBoolean("isRuralPoint", mIsRuralPoint);
       super.onSaveInstanceState(outState);
	}	
}
