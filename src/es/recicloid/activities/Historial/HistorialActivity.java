package es.recicloid.activities.Historial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import es.recicloid.activities.SolicitudRecogida.InfoCollectionDateFragment;
import es.recicloid.activities.main.MainActivity;
import es.recicloid.models.CollectionRequest;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectorToDeletePendingRequests;
import es.recicloid.utils.conections.ConectorToGetCollectionReq;
import es.recicloid.utils.json.JsonToFileManagement;
import es.uca.recicloid.R;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_historial)
public class HistorialActivity extends RoboFragmentActivity {
	@InjectView(R.id.buttonCancelRequests) private Button btn_cancelRequests;
	private ConectorToGetCollectionReq conectGetAppointments;
	private ConectorToDeletePendingRequests conectDelPendReqs;
	private ArrayList<CollectionRequest> request;
	private CaldroidFragment caldroidFragment;
	private static final int CONFIRMED_COLOR = R.color.green;
	private static final int PAST_APPOINTMENT = R.color.red;
	private static final int CURRENTDAYE_COLOR = R.color.blue;
	private boolean mShowTelephoneDialog;
	private FragmentTransaction t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		conectGetAppointments = new ConectorToGetCollectionReq(this);
		conectDelPendReqs = new ConectorToDeletePendingRequests(this);
		btn_cancelRequests.setEnabled(false);
		if(savedInstanceState != null){
			onRestoreInstanceState(savedInstanceState);
		}
		if(mShowTelephoneDialog){
			caldroidFragment = new CaldroidFragment();
			setCustomResourceForDates();
			t = getSupportFragmentManager().beginTransaction();
			t.replace(R.id.calendarRequests, caldroidFragment);
			t.commit();
			Log.i("HistorialActivity.onCreate","Se crea el calendario");
		}
		else{
			FragmentManager fm = getSupportFragmentManager();
			JsonToFileManagement jsonFile = new JsonToFileManagement(this);
			jsonFile.changeFileName("user.json");
			TelephoneDialFrag dialog;
			try {
				// Se carga telefono de solicitud previa
				User user = jsonFile.loadUserForJsonFile();
				dialog = TelephoneDialFrag.newInstance(user.getPhone());
			} catch (FileNotFoundException e) {
				// Sino se localiza se solicita al usuario el telefono
				Log.w("HistorialActivity","cannot find previous contact data file");
				dialog = TelephoneDialFrag.newInstance();
			} catch (IOException e) {
				Log.e("HistorialActivity",e.toString());
				throw new RuntimeException(e);
			}
			dialog.show(fm, "telephoneDialog");
			Log.i("HistorialActivity","show dialog and ask telephone number.");
		}
	}
	
	private void setBtnCancelReqListener() {
		btn_cancelRequests.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(request.isEmpty()){
					Log.e("BtnCanel.OnClickListener","request is null");
					throw new RuntimeException("request is null");
				}
				showAskDialog();
			}
			
		});
	}

	protected void showAskDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title_cancel_request);
		builder.setMessage(R.string.dialog_descr_cancel_request);
		builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   try {
					cancelAllPendingRequests();
					Toast.makeText(HistorialActivity.this, 
							getResources().getString(R.string.dialog_canceling_ok), 
							Toast.LENGTH_LONG).show();
					finish();
	        	   } catch (Exception e) {
	        		   
					Toast.makeText(HistorialActivity.this, 
							getResources().getString(R.string.dialog_error_canceling), 
							Toast.LENGTH_LONG).show();
	        	   }
	           }
	    });
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // Nothing to do.
	           }
	    });
		builder.create().show();
	}

	/**
	 * Se eliminan todas las solicitudes pendientes de realizar.
	 * @throws Exception 
	 */
	protected void cancelAllPendingRequests() throws Exception {
		if(request.isEmpty()){
			throw new RuntimeException("Request is empty");
		}
		final String phoneNumber = request.get(0).getTelephone();
		try {
			conectDelPendReqs.execute(phoneNumber);
			if(!conectDelPendReqs.get()){
				throw conectDelPendReqs.exception;
			}
			
		} catch (Exception e) {
			Log.e("HistorialActivity.cancelAllPendingRequests",e.toString());
			throw e;
		}
	}

	/**
	 * Busca las solicitudes pendientes de realizar de un numero de telefono 
	 * y las muestra en un calendario.
	 * @param phone
	 */
	public void findAndShowRequest(String phone){
		List<CollectionRequest> list = null;
		try {
			list = conectGetAppointments.execute(phone).get();
			if(conectGetAppointments.exception != null){
				throw conectGetAppointments.exception;
			}
			Log.i("HistorialActivity.findAndShowRequest"
					,"Localizadas "+list.size()+" citas de recogida.");;
			
		} catch (Exception e) {
			if(e.toString().contains("HTTP error code : 404")){
				Log.e("HistorialActivity.findAndShowRequest","No existe el usuario con " +
						"telefono "+phone);
				Toast.makeText(this,"No se pudo establecer la conexion",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent(this, MainActivity.class);
     	  		startActivityForResult(intent, 0);
			}
			else if(e.toString().contains("HTTP error code : 204")){
				Log.e("HistorialActivity.findAndShowRequest","El usuario con telefono " +
						phone+" no tiene ninguna peticion en curso");
				TextView text = new TextView(this);
				text.setId((int)System.currentTimeMillis());
				text.setText(R.string.not_exist_prev_req);
				LinearLayout layout = (LinearLayout) this.findViewById(R.id.historial_layout);
				layout.addView(text);
				btn_cancelRequests.setEnabled(false);
				btn_cancelRequests.setVisibility(View.INVISIBLE);
			}
			else{
				Log.e("HistorialActivity.findAndShowRequest","Error " +
						e.toString());
				btn_cancelRequests.setEnabled(false);
				btn_cancelRequests.setVisibility(View.INVISIBLE);
			}
		}
		if(list != null){
			mShowTelephoneDialog = true;
			request = (ArrayList<CollectionRequest>) list;
			caldroidFragment = new CaldroidFragment();
			setCustomResourceForDates();
			t = getSupportFragmentManager().beginTransaction();
			t.replace(R.id.calendarRequests, caldroidFragment);
			t.commit();
			caldroidFragment.setCaldroidListener(getCalendarListener());
			getCalendarListener();
			Log.i("HistorialActivity.onCreate","Se crea el calendario");
			if(existPendingRequest(request)){
				btn_cancelRequests.setEnabled(true);
				setBtnCancelReqListener();
			}
			else{
				btn_cancelRequests.setEnabled(false);
			}
		}
	}

	public void setCustomResourceForDates(){
		if (caldroidFragment != null) {
			// Show request date as blue.
			caldroidFragment.setBackgroundResourceForDate(CURRENTDAYE_COLOR,
					new LocalDate().toDate());
			if(request != null){
				for(CollectionRequest a: request){
					if(a.getFch_collection().isBefore(new LocalDate())){
						caldroidFragment.setBackgroundResourceForDate(PAST_APPOINTMENT,
								a.getFch_collection().toDate());	
					}
					else if(a.getFch_collection().isAfter(new LocalDate())){
						caldroidFragment.setBackgroundResourceForDate(CONFIRMED_COLOR,
								a.getFch_collection().toDate());	
					}

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
		for(CollectionRequest p : request){
			if(p.getFch_collection().equals(date)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Se comprueba si de un listado de solicitudes alguna esta pendiente de realizar,
	 * es decir a una fecha posterior a la fecha del sistema.
	 * @param req
	 * @return
	 */
	private static boolean existPendingRequest(ArrayList<CollectionRequest> req){
		for(CollectionRequest r : req){
			if(r.getFch_collection().isAfter(new LocalDate())){
				return true;
			}
		}
		return false;
	}
	
	private CaldroidListener getCalendarListener(){
		return new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				if(isCollectionDate(date)){
					Log.i("CaldroidListener","Fecha confirmada");
					FragmentManager fragmentManager = getSupportFragmentManager();
					CollectionRequest req = null;
					for(CollectionRequest r : request){
						if(r.getFch_collection().equals(new LocalDate(date))){
							req = r;
							Log.i("HistorialActivity.onSelectDate",
									"fecha de recogida localizada");
							break;
						}
					}
					Log.i("onSelectDate",req.toString());
					InfoCollectionDateFragment dialog = 
							InfoCollectionDateFragment.newInstance(req);
					dialog.show(fragmentManager, "tagShowAppointmentInfo");
				}
			}
		};
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		 super.onRestoreInstanceState(savedInstanceState);
		 mShowTelephoneDialog = savedInstanceState.getBoolean("mShowTelephoneDialog");
		request = savedInstanceState.getParcelableArrayList("request");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
		outState.putBoolean("mShowTelephoneDialog", mShowTelephoneDialog);
		outState.putParcelableArrayList("request", request);
		super.onSaveInstanceState(outState);
	}
}
