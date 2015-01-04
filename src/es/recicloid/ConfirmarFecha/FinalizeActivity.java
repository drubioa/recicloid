package es.recicloid.ConfirmarFecha;

import es.recicloid.main.MainActivity;
import es.uca.recicloid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FinalizeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finalize);
		Button btn_end = (Button) findViewById(R.id.buttonEnd);
		btn_end.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(FinalizeActivity.this,
						MainActivity.class);
				startActivity(intent);   
            }
        });
	}
}
