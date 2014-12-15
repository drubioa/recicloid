	package es.recicloid.ConfirmarFecha;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import es.recicloid.CondicionesUso.CondicionesUsoActivity;
import es.recicloid.DatosContacto.DatosContactoActivity;
import es.recicloid.main.MainActivity;
import es.recicloid.models.CollectionPoint;
import es.recicloid.models.CollectionRequest;
import es.recicloid.models.Furniture;
import es.recicloid.models.ProvisionalAppointment;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectorToDailyAppointmentService;
import es.recicloid.utils.conections.ConectorToDailyAppointmentServiceImp;
import es.recicloid.utils.json.JsonToFileManagement;
import es.uca.recicloid.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@ContentView(R.layout.activity_confirmar_fechas)
public class ConfirmarFechaActivity extends RoboFragmentActivity{
	@InjectView(R.id.button_finalize ) private Button mBtn_continuar;
	private ConectorToDailyAppointmentService mConector;  
	private List<Furniture> mTotalFurnituresToCollect;
	private List<ProvisionalAppointment> mProvisionalAppointment;
	private List<CollectionRequest> mConfirmedRquest;
	private CaldroidFragment caldroidFragment;
	private FurnituresToConfirmInCollDateFragment furnituresToConfirmFrag;
	
	public void setCustomResourceForDates(){
		if (caldroidFragment != null) {
			// Show request date as blue.
				caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					new LocalDate().toDate());
			if(mProvisionalAppointment.size() == 1){
				ProvisionalAppointment a = mProvisionalAppointment.get(0);
				caldroidFragment.setBackgroundResourceForDate(R.color.green,
						a.getFch_collection().toDate());
			}
			else{
				// Show collection request date as green.
				for(ProvisionalAppointment a: mProvisionalAppointment){
					caldroidFragment.setBackgroundResourceForDate(R.color.light_green,
							a.getFch_collection().toDate());
				}
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		caldroidFragment = new CaldroidFragment();
		try {
			mConector = new ConectorToDailyAppointmentServiceImp(this);
		} catch (IOException e1) {
			Log.e("ConfirmarFechaActivity.onCreate","Cannot configure calendar "+
				e1.toString());
		}
		if(savedInstanceState == null){
			configureCalendar();
			loadFurnituresFromJsonFile();
			Log.i("ConfirmarFechaActivity.onCreate","Cargadas furnirues del fichero json");
			try {
				mProvisionalAppointment = obtainsProvisionalAppointments();
				Log.i("ConfirmarFechaActivity.onCreate","Obtenidas solicitudes pendientes de confirmacion");
			} catch (Exception e) {
				Log.e("ConfirmarFechaActivity.onCreate","Cannot load request " +
						e.toString());
				throw new RuntimeException("Cannot obtains ProvisionalAppointments.");
			}
			if(mProvisionalAppointment.size() == 1){
				Log.i("ConfirmarFechaActivity.onCreate","Una Sola solicitud de recogida pdte de confirmar.");
				try {
					mConfirmedRquest = new ArrayList<CollectionRequest>();
					CollectionRequest c = new CollectionRequest(
							mProvisionalAppointment.get(0),mTotalFurnituresToCollect);
					mConfirmedRquest.add(c);
					mBtn_continuar.setEnabled(true);
					Log.i("ConfirmarFechaActivity.onCreate","Creada solicitud de recogida.");
				} catch (Exception e) {
					Log.e("ConfirmarFechaActivity.onCreate",
							e.toString());
					throw new RuntimeException("Connot create collection request.");
				}
			}
			else{
				Log.i("ConfirmarFechaActivity.onCreate","Varias solicitudes de recogida");
				mBtn_continuar.setEnabled(false);
			}
		}
		else{
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");	
		}
		setCustomResourceForDates();
		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
		Log.i("ConfirmarFechaActivity.onCreate","Se incluye fragment del calendario.");
		final Bundle state = savedInstanceState;
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			/**
			 * Si el usuario realiza una pulsacion en un dia recogida entonces
			 * se muestra una ventana con los muebles que seran recogidos 
			 * en dicha fecha.
			 */
			@Override
			public void onSelectDate(Date date, View view) {
				if(isCollectionDate(date)){
					Log.i("CaldroidListener","Se pulsa un dia de recogida");
					furnituresToConfirmFrag = 
							new FurnituresToConfirmInCollDateFragment();
					final String dialogTag = "FURNITURES_CONFIRMATION"
							+"_DIALOG_FRAGMENT";
					// If activity is recovered from rotation
					if (state != null) {
						
					}
				}
				else{
		
				}
			}
			
			/**
			 * Si el usuario manteien una pulsacion larga sobre un dia de recogida
			 * que haya sido confirmado, esta confirmacion se cancela y los
			 * muebles vuelven al listado de furnitures to confirm.
			 */
			@Override
			public void onLongClickDate(Date date, View view) {
				if(isCollectionDate(date)){
					Log.i("CaldroidListener",
							"Pulsacon larga en un dia de recogida");
				}
			}
					
		};
		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
		
		mBtn_continuar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mConfirmedRquest.isEmpty()){
					Log.e("ConfirmarFechaActivity","No se puede registrar, solicitud vacia");
					throw new RuntimeException("cannot confirm because "
				+"Confirmed request is empty.");
				}
				for(final CollectionRequest c : mConfirmedRquest){
					if(!c.checkCorrectRequest()){
						final String errorMessage = 
								"Caonnot confirm appointment because request is "
								+"in bad format.";
						Log.e("ConfirmarFechaActivity",errorMessage+
								c.toString());
						throw new RuntimeException(errorMessage);	
					}
					try {
						Log.i("ConfirmarFechaActivity","Confirmes request "+
								c.toString());
						final ProgressDialog dialog = ProgressDialog.show(ConfirmarFechaActivity.this, getResources()
								.getText(R.string.loadind_title),
								getResources().getText(R.string.message_confirmAppointment_descr),true);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
						    public void run() {
						        try {
									mConector.confirmAppointment(c);
									Log.i("ConfirmarFechaActivity","Se confirma la solicitud de recogida");
								} catch (Exception e) {
									Log.e("ConfirmarFechaActivity","Cannot confirm appointment "+e.toString());
									throw new RuntimeException("Cannot confirm appointment ");
								}
						        dialog.dismiss();
						    }   
						}, 3000);  // 3000 milliseconds	
					} catch (Exception e) {
						Log.e("ConfirmarFechaActivity",
								"Caonnot confirm appointment because "+e.toString());
						throw new RuntimeException(e.toString());
					}
				}
				Intent intent = new Intent(ConfirmarFechaActivity
						.this,MainActivity.class);
				startActivity(intent);
			}
			
		});
	}

	/**
	 * Comprueba si el dia d es un dia de recogida.
	 * @param d
	 * @return
	 */
	protected boolean isCollectionDate(Date d) {
		LocalDate date = new LocalDate(d);
		for(ProvisionalAppointment p : mProvisionalAppointment){
			if(p.getFch_collection().equals(date)){
				return true;
			}
		}
		return false;
	}

	private List<ProvisionalAppointment> obtainsProvisionalAppointments() {
			mTotalFurnituresToCollect = loadFurnituresFromJSONFile();
			User user = loadUserFromJSONFile();
			CollectionPoint collectionPoint = loadCollectionPointFromJSONFile();
			try {
				List<ProvisionalAppointment> provisionalAppointment = 
						mConector.getProvisionalAppointments(user.getPhone(), 
						Furniture.countFurnituresArray(mTotalFurnituresToCollect),collectionPoint.getId());
				if(provisionalAppointment == null){
					Log.w("ConfirmarFechasActivity.obtainsProvisionalAppointments",
							"No se obtuvo ninguna solicitud pendiente de confirmar para el telefono"+
							user.getPhone()+"\ncollectionPoint: "+collectionPoint.getId()+"\nFurnitures to collect: "+
							Furniture.countFurnituresArray(mTotalFurnituresToCollect));
				}
				return provisionalAppointment;
			} catch (Exception e) {
				Log.e("ConfirmarFechaActivity.obtainsProvisionalAppointments",
						"Cannot getProvisionalAppointments: "
			+e.toString());
				throw new RuntimeException();
			}
	}
	
	
	private User loadUserFromJSONFile(){
		try {
			JsonToFileManagement jsonFile = new JsonToFileManagement(this);
			jsonFile.changeFileName("user.json");
			User user = jsonFile.loadUserForJsonFile();
			return user;
		} catch (IOException e) {
			Log.e("ConfirmarFechaActivity.loadUserFromJSONFile",
					"Cannot load user "+e.toString());
			throw new RuntimeException();
		}	
	}
	
	private CollectionPoint loadCollectionPointFromJSONFile(){
		JsonToFileManagement jsonFile = new JsonToFileManagement(this);
		jsonFile.changeFileName("collection-point.json");
		try {
			return jsonFile.loadCollectionPointFromLocalFile();
		} catch (IOException e) {
			Log.e("ConfirmarFechaActivity.obtainsProvisionalAppointments",
					"Cannot load collectionPoint"+
					e.toString());
			throw new RuntimeException();
		}
	}
	
	private List<Furniture> loadFurnituresFromJSONFile(){
		try {
			JsonToFileManagement jsonFile = new JsonToFileManagement(this);
			jsonFile.changeFileName("furnitures.json");
			return jsonFile.loadFurnituresFromLocalFile();
		} catch (IOException e) {
			Log.e("ConfirmarFechaActivity.loadFurnituresFromJSONFile",
					"Cannot load furnitures "+e.toString());
			throw new RuntimeException();
		}			
	}

	private void loadFurnituresFromJsonFile(){
		try {
			JsonToFileManagement jsonFile = new JsonToFileManagement(this);
			jsonFile.changeFileName("furnitures.json");
			mTotalFurnituresToCollect= jsonFile.loadFurnituresFromLocalFile();
		} catch (IOException e) {
			Log.e("ConfirmarFechaActivity", e.toString());
			throw new RuntimeException("Cannot load furnitures.");
		}
	}
	
	public void configureCalendar(){
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
		args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
		args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
		// Uncomment this to customize startDayOfWeek
		// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
		// CaldroidFragment.TUESDAY); // Tuesday
		caldroidFragment.setArguments(args);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}

}
