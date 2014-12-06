	package es.recicloid.activities.servrecog;

import java.util.Calendar;

import org.joda.time.DateTime;

import es.recicloid.logic.conections.ConectorToServices;
import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

public class Confirmar1FechaActivity extends Activity {
	private CalendarView mCalendarView; 
	private ListView mItemsPerDay;
	private Button mBtn_continuar;
	private ConectorToServices mConector;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmar_fechas);
		mCalendarView = (CalendarView) findViewById(R.id.collectioncalendar);
		displayCalendar();
		mItemsPerDay = (ListView) findViewById(R.id.listViewFurnituresPerDay);
		mBtn_continuar = (Button) findViewById(R.id.buttonNext);
		/*try {
			mConector = new ConectorToServices(this);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private void displayCalendar(){
		mCalendarView = (CalendarView) findViewById(R.id.collectioncalendar);
		mCalendarView.setFocusableInTouchMode(false);
	    DateTime dt = new DateTime();
		mCalendarView.setDate(getCalendarDay(dt.getDayOfMonth(),
				dt.getMonthOfYear(),dt.getYear()));
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
