package es.recicloid.SolicitudRecogida;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import es.recicloid.main.MainActivity;
import es.uca.recicloid.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

@ContentView(R.layout.activity_finalize)
public class FinalizeActivity extends RoboActivity {
	@InjectView(R.id.webViewTextFinalize ) WebView text;
	@InjectView(R.id.buttonEnd ) Button btn_end;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		text.loadData(getString(R.string.descrip_confirmaton), "text/html", "utf-8");
		
		btn_end.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(FinalizeActivity.this,
						MainActivity.class);
				startActivity(intent);   
            }
        });
	}
}
