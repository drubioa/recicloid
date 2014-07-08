	package es.uca.recicloid.activities;

import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CalendarView;

public class ConfirmarFechasActivity extends Activity {
	 CalendarView calendarView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		calendarView = (CalendarView) findViewById(R.id.calendar);
		setContentView(R.layout.activity_confirmar_fechas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}

}
