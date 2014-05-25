package es.uca.recicloid.activities;

import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ConfirmarFechasActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmar_fechas);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirmar_fechas, menu);
		return true;
	}

}
