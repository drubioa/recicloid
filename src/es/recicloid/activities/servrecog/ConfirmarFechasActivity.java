	package es.recicloid.activities.servrecog;

import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.TextView;

public class ConfirmarFechasActivity extends Activity {
	 CalendarView calendarView; 
	 TextView itemsToCollection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		calendarView = (CalendarView) findViewById(R.id.collectioncalendar);
		setContentView(R.layout.activity_confirmar_fechas);
		itemsToCollection = (TextView) findViewById(R.id.listfurniturestocollect);
		itemsToCollection.setEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}

}
