package es.uca.recicloid.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import es.uca.recicloid.R;
import es.uca.recicloid.clases.Furniture;
import es.uca.recicloid.dialogs.DialogAlert;
import es.uca.recicloid.dialogs.DialogSelecLocationType;
import es.uca.recicloid.map.AddressTracker;
import es.uca.recicloid.map.GetAddressTask;
import es.uca.recicloid.map.LocationTracker;
import es.uca.recicloid.map.Zone;
import es.uca.recicloid.map.ZoneParser;

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
	private double mLongitud;
	private double mLatitud;
	private ArrayList<Furniture> furnitures;
	private GoogleMap map;	
	private EditText editText;
	private Button btnNextStep;
	

	//private String dirección;
	private final LatLng LOCAL = 
			new LatLng(36.530375900000000000, -6.194416899999965000);
	AddressTracker localizador;
	private Zone urban , municipal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_ubicacion_recogida);		
		map = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		editText = (EditText) findViewById(R.id.editText1);
		btnNextStep = (Button) findViewById(R.id.buttonContinuar);
		
		try {
			urban = parserZone("urban-zone.xml");
			municipal = parserZone("municipal-area.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("UbicacionRecogidaActivity","Cannot parser xml file ("+e.toString()+")");
		}
		
		
		if(savedInstanceState == null){
			editText.setText("Dirección de recogida, num");
			showsGenericView();
			Intent intent = getIntent();
			furnitures = intent.getParcelableArrayListExtra("itemsSelected");
			mLocalizado = false;
			showInitialDialog();
			mShowedRuralProcAdvice = false;
			btnNextStep.setEnabled(false);
		}		
		else{
			mLocalizado = savedInstanceState.getBoolean("mLocalizado");
			mLatitud = savedInstanceState.getDouble("mLatitud");
			mLongitud = savedInstanceState.getDouble("mLongitud");
			mShowedRuralProcAdvice = savedInstanceState.
					getBoolean("ShowedRuralProcAdvice");
			
			if(mLocalizado){
				LatLng point = new LatLng(mLatitud,mLongitud);
				addMarkToPosition(point);
				showChosenPointInMap(point);
				btnNextStep.setEnabled(true);
			}
			else{
				// By default shows the general city view
				showsGenericView();
				btnNextStep.setEnabled(false);
			}
		}
		setListeners();
	}
	
	/**
	 * Dado el nombre de un fichero xml genera una zona correspondiente a un
	 * área de la localidad
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
	        	Log.i("UbicacionRecogidaActivity","On click in point "+point.toString());
				checkLocation(convertLagIntToLocation(point));
	        }
	    });		
		
		// Listener del boton continuar con la solicitud
		btnNextStep.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(UbicacionRecogidaActivity
						.this,DatosContactoActivity.class);
				startActivity(intent);   
            }
        });
	}

	public void showInitialDialog(){
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
	private void showsGenericView(){
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
	}

	public void checkLocation(LatLng point){
		this.checkLocation(convertLagIntToLocation(point));
	}
	
	/**
	 * Checks if point belong a valid zone, and if it is urban or rural zone.
	 * Shows dialogs if it fail and advice if it is rural zone.
	 * @param point
	 */
	public void checkLocation(Location point){
		try {
			if(point == null){
				throw new Exception("checkLocation function receives null param");
			}
			if(municipal.isInside(point)){
				Log.i("UbicacionRecogidaActivity","location in urban area");
				mLatitud = point.getLatitude();
				mLongitud = point.getLongitude();
				addMarkToPosition(convertLocationToLagLog(point));
				showChosenPointInMap(convertLocationToLagLog(point));
				if(isRuralPoint(convertLocationToLagLog(point)) && 
						!mShowedRuralProcAdvice){
					Log.i("UbicacionRecogidaActivity","location in invalid area");
					mShowedRuralProcAdvice = true;
					FragmentManager fm = getSupportFragmentManager();
					DialogAlert newFragment = DialogAlert.newInstance(
							R.string.dialog_title_location_not_valid,
							R.string.dialog_descr_location_not_valid);
					newFragment.show(fm, "tagAvisoLocNotValid");
				}
			}
			else{
				Log.i("UbicacionRecogidaActivity","location in rural area");
				// point could not be located in the service area
				mLocalizado = false;
				btnNextStep.setEnabled(false);
				editText.setText("");
				showsGenericView();
				FragmentManager fm = getSupportFragmentManager();
				DialogAlert newFragment = DialogAlert.newInstance(
						R.string.dialog_title_location_not_valid,
						R.string.dialog_descr_location_not_valid);
				newFragment.show(fm, "tagAvisoLocNotValid");
			}
			GetAddressTask add = new GetAddressTask(this);
			Address address = add.obtainsAddress(point);
			if(address != null){
				Log.w("UbicacionRecogidaActivity","adress is: "+address.getAddressLine(0));
				editText.setText(address.getAddressLine(0));
			}
			else{
				Log.w("UbicacionRecogidaActivity","cannot obtain address");
			}
		} catch (Exception e) {
			Log.e("UnbicacionRecogidaActity","Exception in checkLocation function: "
					+e.toString());
		}
	}
	
	/**
	 * Obtains location by client´s mobile technology
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
			checkLocation(currentLocation);
		}
    }
	
	/**
	 * Draw q mark in a map with location point
	 * @param localizacion
	 */
	private void addMarkToPosition(LatLng localizacion){
		this.map.clear();
		this.mLocalizado = true;
		this.btnNextStep.setEnabled(true);
		this.map.addMarker(new MarkerOptions()
        	.position(localizacion)
        	.title("Ubicación")
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
	 * Transforms a LatLng to Location
	 * @param point
	 * @return Location correspondents to point dispatched
	 */
	private LatLng convertLocationToLagLog(Location point){
		  return new LatLng(point.getLatitude(),point.getLongitude());
	}
	
	/**
	 * 
	 * @param point
	 * @return true if the point is in a rural area
	 */
	private boolean isRuralPoint(LatLng point){
		return municipal.isInside(point) && !urban.isInside(point);
	}
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
	   outState.putBoolean("mLocalizado", mLocalizado);
	   outState.putDouble("mLongitud", mLongitud);
	   outState.putDouble("mLatitud", mLatitud);
	   outState.putParcelableArrayList("itemsSelected", furnitures);
	   outState.putBoolean("ShowedRuralProcAdvice", mShowedRuralProcAdvice);
       super.onSaveInstanceState(outState);
	}	
}
