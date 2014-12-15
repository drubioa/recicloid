package es.recicloid.CondicionesUso;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import es.recicloid.ConfirmarFecha.ConfirmarFechaActivity;
import es.uca.recicloid.R;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

@ContentView(R.layout.activity_condiciones_uso)
public class CondicionesUsoActivity extends RoboActivity {
	boolean mAcepted;
	@InjectView(R.id.button1) Button btn_continue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null){
			mAcepted = false;
		}
		else{
			mAcepted = savedInstanceState.getBoolean("acepted");
		}
		if(!mAcepted){
			btn_continue.setEnabled(false);
		}
		
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxAceptar);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   if(isChecked){
					   btn_continue.setEnabled(true);
				   }
				   else{
					   btn_continue.setEnabled(false);
				   }
				   mAcepted = isChecked;
			   }
		});
		
		btn_continue.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(CondicionesUsoActivity.this,
						ConfirmarFechaActivity.class);
				startActivity(intent);   
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.condiciones_uso, menu);
		return true;
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
       outState.putBoolean("acepted", mAcepted);
       super.onSaveInstanceState(outState);
	}	

}
