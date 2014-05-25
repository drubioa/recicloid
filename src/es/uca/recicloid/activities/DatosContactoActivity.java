package es.uca.recicloid.activities;

import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class DatosContactoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_contacto);
		Button btn_continue = (Button) findViewById(R.id.button);
		btn_continue.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(DatosContactoActivity.this,CondicionesUsoActivity.class);
				startActivity(intent);   
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.datos_contacto, menu);
		return true;
	}

}
