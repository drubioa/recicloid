package es.recicloid.Historial;

import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import es.recicloid.main.MainActivity;
import es.recicloid.models.CollectionRequest;
import es.recicloid.utils.conections.ConectorToDailyAppointmentService;
import es.recicloid.utils.conections.ConectorToDailyAppointmentServiceImp;
import es.uca.recicloid.R;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HistorialActivity extends RoboFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historial);
		FragmentManager fm = getSupportFragmentManager();
		TelephoneDialFrag dialog = TelephoneDialFrag.newInstance();
		dialog.show(fm, "telephoneDialog");
		Log.i("HistorialActivity","show dialog and ask telephone number.");
	}
	
	/**
	 * Busca las solicitudes pendientes de realizar de un numero de telefono 
	 * y las muestra en un calendario.
	 * @param phone
	 */
	public void findAndShowRequest(String phone){
		List<CollectionRequest> list = null;
		try {
			ConectorToDailyAppointmentService conector = new ConectorToDailyAppointmentServiceImp(this);
			list = conector.getPendingRequest(phone);
		} catch (Exception e) {
			// Muestra mensaje de error y vuelve al menu principal.
			Log.e("HistorialActivity.findAndShowRequest",e.toString());
			Toast.makeText(this,"No se pudo establecer la conexion",
						Toast.LENGTH_LONG).show();
     	  Intent intent = new Intent(this, MainActivity.class);
     	  startActivityForResult(intent, 0);
		}
		if(list != null){
			// Muestra el calendario con las solicitudes de recogida.
			
		}
		else{
			// Muestra mensaje de que no tiene ninguna solicitud de recogida y el boton de cancelar disabled.
			TextView text = new TextView(this);
			text.setId((int)System.currentTimeMillis());
			text.setText("No se encontro ninguna solicitud de recogida");
			LinearLayout layout = (LinearLayout) this.findViewById(R.id.historial_layout);
			layout.addView(text);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.historial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
