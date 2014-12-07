	package es.recicloid.activities.servrecog;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import es.recicloid.clases.Furniture;
import es.recicloid.logic.conections.ConectorToServices;
import es.uca.recicloid.R;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

@ContentView(R.layout.activity_confirmar_fechas)
public class ConfirmarFechaActivity extends RoboActivity {
	@InjectView(R.id.collectioncalendar) private CalendarView mCalendarView; 
	@InjectView(R.id.listViewFurnituresPerDay) private ListView mItemsPerDay;
	@InjectView(R.id.buttonNext ) private Button mBtn_continuar;
	private boolean isEnable;
	private ConectorToServices mConector;  
	public ArrayList<Furniture> furnituresToRecic;
	private int numSolicitudes;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayCalendar();
		Bundle reicieveParams = getIntent().getExtras();
		if(savedInstanceState == null){
			mBtn_continuar.setEnabled(false);
			isEnable = false;
		}
	}

	/**
	 * Show calendar in activity with initial configuration and the
	 * collection day marked.
	 */
	private void displayCalendar(){
		mCalendarView.setFocusableInTouchMode(false);
	    LocalDate today = new LocalDate();
		mCalendarView.setDate(today.toDate().getTime());
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}

}
