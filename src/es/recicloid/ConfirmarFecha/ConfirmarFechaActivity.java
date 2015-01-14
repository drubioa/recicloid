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

import es.recicloid.main.MainActivity;
import es.recicloid.models.CollectionPoint;
import es.recicloid.models.CollectionRequest;
import es.recicloid.models.Furniture;
import es.recicloid.models.ProvisionalAppointment;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectorToConfirmAppointment;
import es.recicloid.utils.conections.ConectorToGetProvisAppointment;
import es.recicloid.utils.conections.InfoToGetProvAppointments;
import es.recicloid.utils.json.JsonToFileManagement;
import es.uca.recicloid.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@ContentView(R.layout.activity_confirmar_fechas)
public class ConfirmarFechaActivity extends RoboFragmentActivity{
	@InjectView(R.id.button_finalize ) private Button mBtn_continuar;
	private ArrayList<Furniture> mTotalFurnituresToCollect;
	private ArrayList<ProvisionalAppointment> mProvisionalAppointment;
	private ArrayList<CollectionRequest> mConfirmedRquest;
	private CaldroidFragment caldroidFragment;
	private int mNumFurnituresForConfirm;
	private FragmentTransaction t;
	private boolean mIsBtnContinuarActive;
	private static final int CONFIRMED_COLOR = R.color.green;
	private static final int UNCONFIRMED_COLOR = R.color.light_green;
	private static final int CURRENTDAYE_COLOR = R.color.blue;
	private ConectorToConfirmAppointment conector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		conector = new ConectorToConfirmAppointment(this);
		caldroidFragment = new CaldroidFragment();
		if(savedInstanceState == null){
			mTotalFurnituresToCollect = loadFurnituresFromJSONFile();
			Log.i("ConfirmarFechaActivity.onCreate",
					"Cargadas "+Furniture.countFurnituresArray(mTotalFurnituresToCollect)
					+" furnitures del fichero json");
			mConfirmedRquest = new ArrayList<CollectionRequest>();
			try {
				mProvisionalAppointment = (ArrayList<ProvisionalAppointment>) 
						obtainsProvisionalAppointments();
				Log.i("ConfirmarFechaActivity.onCreate",
						"Obtenidas solicitudes pendientes de confirmacion");
			} catch (Exception e) {
				Log.e("ConfirmarFechaActivity.onCreate",
						"Cannot load request " + e.toString());
				throw new RuntimeException("Cannot obtains ProvisionalAppointments.");
			}
			if(mProvisionalAppointment.size() == 1){
				Log.i("ConfirmarFechaActivity.onCreate",
						"Una Sola solicitud de recogida pdte de confirmar.");
				mNumFurnituresForConfirm = 0;
				try {
					CollectionRequest c = new CollectionRequest(
							mProvisionalAppointment.get(0),
							mTotalFurnituresToCollect);
					mConfirmedRquest.add(c);
					mBtn_continuar.setEnabled(true);
					mIsBtnContinuarActive = true;
					Log.i("ConfirmarFechaActivity.onCreate",
							"Creada solicitud de recogida.");
				} catch (Exception e) {
					Log.e("ConfirmarFechaActivity.onCreate",
							e.toString());
					throw new RuntimeException("Connot create collection request.");
				}
			}
			else{
				Log.i("ConfirmarFechaActivity.onCreate",
						"Varias solicitudes de recogida");
				
				mNumFurnituresForConfirm = 
						Furniture.countFurnituresArray(mTotalFurnituresToCollect);
				if(mNumFurnituresForConfirm == 0){
					mBtn_continuar.setEnabled(true);
					mIsBtnContinuarActive = true;
				}
				else{
					mBtn_continuar.setEnabled(false);
					mIsBtnContinuarActive = false;
				}
			}
		}
		else{
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
			mNumFurnituresForConfirm = savedInstanceState
					.getInt("mNumFurnituresForConfirm");
			mIsBtnContinuarActive = savedInstanceState
					.getBoolean("mIsBtnContinuarActive");
			mTotalFurnituresToCollect = 
					savedInstanceState.getParcelableArrayList("mTotalFurnituresToCollect");
			mProvisionalAppointment = 
					savedInstanceState.getParcelableArrayList("mProvisionalAppointment");
			mConfirmedRquest = 
					savedInstanceState.getParcelableArrayList("mConfirmedRquest");
		}
		configureCalendar();
		setCustomResourceForDates();
		// Attach to the activity
		t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();
		Log.i("ConfirmarFechaActivity.onCreate",
				"Se incluye fragment del calendario.");
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
					Log.i("CaldroidListener","Fecha confirmada");
					FragmentManager fragmentManager = getSupportFragmentManager();
					if(isConfirmedDate(date)){
						Log.i("ConfirmarFechaActivity.onSelectDate",
								"Se pulsa sobre un dia de recogida");
						CollectionRequest req = null;
						for(CollectionRequest r : mConfirmedRquest){
							if(r.getFch_collection().equals(new LocalDate(date))){
								req = r;
								Log.i("ConfirmarFechaActivity.onSelectDate",
										"fecha de recogida localizada");
								break;
							}
						}
						if(req == null){
							Log.e("ConfirmarFechaActivity.onSelectDate","Cannot find " +
									"collection date when calender were consulted.");
							throw new NullPointerException("Cannot find collection date" +
									" when calender were consulted.");
						}
						Log.i("ConfirmarFechaActivity.onSelectDate",
								"Solicitud de recogida para el dia "
						+req.getFch_collection().toString());
						InfoCollectionDateFragment dialog = 
								InfoCollectionDateFragment.newInstance(req);
						dialog.show(fragmentManager, "tagShowAppointmentInfo");
					}
					/**
					 * Se pulsa en una fecha pendiente de confirmar.
					 */
					else{
						Log.i("CaldroidListener","Fecha pendiente de confirmacion");
						ProvisionalAppointment appointment = 
								getProvisionalAppointment(new LocalDate(date));
						
						FurnitureSelectorDialFrag dialog = 
								FurnitureSelectorDialFrag.newInstance(new LocalDate(date),
										appointment.getNumFurnitures(),mTotalFurnituresToCollect);
						dialog.show(fragmentManager, "furnitureSelector");
					}
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
		Log.i("ConfirmarFechaActivity","Solicitudes para "+this.mNumFurnituresForConfirm+" enseres");
		mBtn_continuar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(mConfirmedRquest.isEmpty()){
					Log.e("ConfirmarFechaActivity","No se puede registrar, solicitud vacia");
					throw new RuntimeException("cannot confirm because "
						+"Confirmed request is empty.");
				}
				
				try {
						final ProgressDialog dialog = ProgressDialog.show(
								ConfirmarFechaActivity.this, getResources()
								.getText(R.string.loadind_title),
								getResources().getText(R.string
										.message_confirmAppointment_descr),true);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
						    
						public void run() {
							try {
								CollectionRequest[] reqs = new CollectionRequest[mConfirmedRquest.size()];
								for(int i = 0; i < mConfirmedRquest.size();i++){
									reqs[i] = mConfirmedRquest.get(i);
								}
								conector.execute(reqs);
								if(!conector.get()){
									Log.e("ConfirmarFechaActivity",
											"No se pudo confirmar la solicitud de recogida");
									if(conector.exception != null){
										throw conector.exception;
									}
								}
								else{
									Log.i("ConfirmarFechaActivity",
											"Se confirma la solicitud de recogida");
									// Show message before finalize.
									Toast.makeText(ConfirmarFechaActivity.this, 
											getResources().getString(R.string.message_finalize), 
											Toast.LENGTH_LONG).show();
								}
							} catch (Exception e) {
								Log.e("ConfirmarFechaActivity",
										"Cannot confirm appointment "+e.toString());
								throw new RuntimeException("Cannot confirm appointment ");								}
						        dialog.dismiss();
						        Intent intent = new Intent(ConfirmarFechaActivity
									.this,FinalizeActivity.class);
								startActivity(intent);
						    }   
						}, 5000);  // 5000 milliseconds							
					} catch (Exception e) {
						Log.e("ConfirmarFechaActivity",
								"Caonnot confirm appointment because "+e.toString());
						Toast.makeText(ConfirmarFechaActivity.this, 
								getResources().getString(R.string.message_error_confirmation), 
								Toast.LENGTH_LONG).show();
						throw new RuntimeException(e.toString());
					}
				}
		});
	}

	public void setCustomResourceForDates() {
		if (caldroidFragment != null) {
			// Show request date as blue.
				caldroidFragment.setBackgroundResourceForDate(CURRENTDAYE_COLOR,
					new LocalDate().toDate());
			if(mProvisionalAppointment.size() == 1){
				ProvisionalAppointment a = mProvisionalAppointment.get(0);
				caldroidFragment.setBackgroundResourceForDate(CONFIRMED_COLOR,
						a.getFch_collection().toDate());
			}
			else{
				// Show collection request date as green.
				for(ProvisionalAppointment a: mProvisionalAppointment){
					caldroidFragment.setBackgroundResourceForDate(UNCONFIRMED_COLOR,
							a.getFch_collection().toDate());
				}
				for(CollectionRequest a: mConfirmedRquest){
					caldroidFragment.setBackgroundResourceForDate(CONFIRMED_COLOR,
							a.getFch_collection().toDate());
				}
			}
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
	
	/**
	 * @param date
	 * @return provisional appointment corresponds to argument date.
	 */
	private ProvisionalAppointment getProvisionalAppointment(LocalDate date){
		for(ProvisionalAppointment p : mProvisionalAppointment){
			if(p.getFch_collection().equals(date)){
				return p;
			}
		}
		throw new IllegalArgumentException("No exist provisional appoinbtmet "
				+" for "+date.toString());
	}

	private ArrayList<ProvisionalAppointment> obtainsProvisionalAppointments() {
		User user = loadUserFromJSONFile();
		CollectionPoint collectionPoint = loadCollectionPointFromJSONFile();
		List<ProvisionalAppointment> provisionalAppointment = null;
		try {
			ConectorToGetProvisAppointment conector =
					new ConectorToGetProvisAppointment(this);
			InfoToGetProvAppointments info = new 
					InfoToGetProvAppointments(user.getPhone(), 
							Furniture.countFurnituresArray(mTotalFurnituresToCollect),collectionPoint.getId());
			conector.execute(info);
			provisionalAppointment = conector.get();
			if(provisionalAppointment == null){
				Log.w("ConfirmarFechasActivity.obtainsProvisionalAppointments",
						"No se obtuvo ninguna solicitud pendiente de confirmar para el telefono"+
						user.getPhone()+"\ncollectionPoint: "+collectionPoint.getId()
						+"\nFurnitures to collect: "+
						Furniture.countFurnituresArray(mTotalFurnituresToCollect));
			}
			if(conector.exception != null){
				throw conector.exception;
			}
			return (ArrayList<ProvisionalAppointment>) provisionalAppointment;
		} catch (Exception e) {
			if(e.toString().contains("HTTP error code : 400")){
				// Solicitud en curso : muestra un mensaje de error.
				Log.e("ConfirmarFechaActivity.obtainsProvisionalAppointments",
						"Se muestra mensaje de error y se vuelve al menu principal.");
				Toast.makeText(ConfirmarFechaActivity.this, 
						getResources().getString(R.string.message_finalize), 
						Toast.LENGTH_LONG).show();
		        Intent intent = new Intent(ConfirmarFechaActivity.this,
		        		MainActivity.class);
				startActivity(intent);
			}
			else{
				Log.e("ConfirmarFechaActivity.obtainsProvisionalAppointments",
						"No se ha capturado el error.");
				Log.e("ConfirmarFechaActivity.obtainsProvisionalAppointments",
						"Cannot getProvisionalAppointments: "
								+e.toString());
				throw new RuntimeException();	
			}
		}
		return null;
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
	
	private ArrayList<Furniture> loadFurnituresFromJSONFile(){
		try {
			JsonToFileManagement jsonFile = new JsonToFileManagement(this);
			jsonFile.changeFileName("furnitures.json");
			return (ArrayList<Furniture>) jsonFile.loadFurnituresFromLocalFile();
		} catch (IOException e) {
			Log.e("ConfirmarFechaActivity.loadFurnituresFromJSONFile",
					"Cannot load furnitures "+e.toString());
			throw new RuntimeException();
		}			
	}

	private boolean isConfirmedDate(Date date) {
		for(CollectionRequest c : mConfirmedRquest){
			if(c.getFch_collection().equals(new LocalDate(date))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Se confirma una solicitud de recogida correspondiente a la fecha
	 * @param 
	 */
	public void confirmAppointment(LocalDate appointmentDate,
			List<Furniture> furnitures){
		ProvisionalAppointment p = findAppointment(appointmentDate);
		Log.i("ConfirmarFechaActivity.confirmAppointment","Date of appointmet was found");
		try {
			CollectionRequest req = new CollectionRequest(p,furnitures);
			mConfirmedRquest.add(req);
			Log.i("ConfirmarFechaActivity.confirmAppointment",
					"confirmed request");
			mProvisionalAppointment.remove(p);
			Log.i("ConfirmarFechaActivity.confirmAppointment",
					"removing provisional request");
			if(Furniture.countFurnituresArray(furnitures) ==
					Furniture.countFurnituresArray(mTotalFurnituresToCollect)){
				Log.w("confirmAppointment",
						"furnitures selected are equal than mTotalFurnituresToCollect");
			}
			Log.i("confirmAppointment","Before: "+
					Furniture.countFurnituresArray(mTotalFurnituresToCollect));
			for(Furniture f : furnitures){
				Log.i("ConfirmarFechaActivity.confirmAppointment",
						"Debe decrementar "+f.getName()
					+" en "+f.getCantidad());
				mTotalFurnituresToCollect = (ArrayList<Furniture>) 
						Furniture.decrementFurniture(f, mTotalFurnituresToCollect);
			}
			Log.i("ConfirmarFechaActivity.confirmAppointment","Removes "+Furniture.countFurnituresArray(furnitures)
					+" to furniture list.");
			Log.i("confirmAppointment","After: "+
					Furniture.countFurnituresArray(mTotalFurnituresToCollect));
			if(Furniture.countFurnituresArray(mTotalFurnituresToCollect) == 0){
				mBtn_continuar.setEnabled(true);
				mIsBtnContinuarActive = true;
				Log.i("ConfirmarFechaActivity.confirmAppointment","Active button to continue");
			}
			// Change the color of the date.
			caldroidFragment.setBackgroundResourceForDate(CONFIRMED_COLOR,
					appointmentDate.toDate());
			t.replace(R.id.calendar1, caldroidFragment);
			Log.i("ConfirmarFechaActivity.confirmAppointment",
					"Changes the colour of the appointment confirmed date");
		} 
		catch (Exception e) {
			Log.e("ConfirmarFechaActivity","In confirmAppointment method "+e.toString());
		}
	}
	
	private ProvisionalAppointment findAppointment(LocalDate appointmentDate) {
		String existentDays = new String();
		for(ProvisionalAppointment p : mProvisionalAppointment){
			existentDays = existentDays+p.getFch_collection()+" ";
			if(p.getFch_collection().isEqual(appointmentDate)){
				return p;
			}
		}
		Log.e("ConfirmarFechaActivity.findAppointment","Cannot find appointments "
				+appointmentDate+" in exsitent days: "+existentDays);
		throw new IllegalArgumentException("ProvisionalAppointment to "
				+appointmentDate+" not found.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.w("ConfirmadFechaActivity","onSaveInstanceState");
		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
		outState.putInt("mNumFurnituresForConfirm", mNumFurnituresForConfirm);
		outState.putBoolean("mIsBtnContinuarActive", mIsBtnContinuarActive);
		outState.putParcelableArrayList("mTotalFurnituresToCollect",
				mTotalFurnituresToCollect);
		outState.putParcelableArrayList("mProvisionalAppointment", mProvisionalAppointment);
		outState.putParcelableArrayList("mConfirmedRquest", mConfirmedRquest);	
	}
}
