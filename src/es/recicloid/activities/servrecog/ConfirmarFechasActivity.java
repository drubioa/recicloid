	package es.recicloid.activities.servrecog;

import java.util.Calendar;

import es.recicloid.logic.conections.ConectorToServices;
import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.ListView;

public class ConfirmarFechasActivity extends Activity {
	private CalendarView mCalendarView; 
	private ListView itemsPerDay;
	private ConectorToServices conector;  
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmar_fechas);
		mCalendarView = (CalendarView) findViewById(R.id.collectioncalendar);
		displayCalendar();
		itemsPerDay = (ListView) findViewById(R.id.listViewFurnituresPerDay);
		conector = new ConectorToServices();
	}

	private void displayCalendar(){
		mCalendarView = (CalendarView) findViewById(R.id.collectioncalendar);
		mCalendarView.setFocusableInTouchMode(false);
		mCalendarView.setDate(getCalendarDay(29,8,2014));
	}
	
	/**
	 * Devuelve la fecha en formato milliTime correspondiente a
	 * una fecha pasaa por parametro.
	 * @param day
	 * @param month
	 * @param year
	 * @return milliTime
	 */
	private long getCalendarDay(int day,int month,int year){
		 Calendar calendar = Calendar.getInstance();
		 calendar.set(Calendar.YEAR, year);
		 calendar.set(Calendar.MONTH, month);
		 calendar.set(Calendar.DAY_OF_MONTH, day);

		 long milliTime = calendar.getTimeInMillis();
		 return milliTime;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}

}
