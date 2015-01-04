package es.recicloid.Historial;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.EditText; 
import android.widget.Toast;
import es.recicloid.main.MainActivity;
import es.uca.recicloid.R;


public class TelephoneDialFrag extends RoboDialogFragment{
	private boolean mValidPhone;
	private String mPhoneNumber;
	@InjectView(R.id.editTextPhone) private EditText telephone;
	
	TelephoneDialFrag(){
		mValidPhone = false;
	}
	
	static TelephoneDialFrag newInstance(){
		return new TelephoneDialFrag();
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setView(inflater.inflate(R.layout.phone_fragment, null))
		    // Add action buttons
				   .setTitle(R.string.menu_datos_contacto_telefono)
				   .setMessage(R.string.dialog_ask_telephone)
		           .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                  if(mValidPhone){
		                	  // Conectar y cargar solicitudes de recogida
		                	  ((HistorialActivity)  getActivity()).findAndShowRequest(mPhoneNumber);
		                  }
		                  else{
		                	  // Mostrar Toast 'Numero No Valido' y volver al Menu principal.
		                	  Toast.makeText(getActivity(), 
										getResources().getString(R.string.message_no_valid_phone),
										Toast.LENGTH_LONG).show();
		                	  Intent intent = new Intent(getActivity(), MainActivity.class);
		                	  startActivityForResult(intent, 0);
		                  }
		               }
		           });
		    return builder.create();
	}
	
	public void addListenerToEditTextName(EditText name){
		name.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {		
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(s.length() == 9 && s.charAt(0) != '6' ||  s.charAt(0) != '0'){
					mPhoneNumber = s.toString();
					mValidPhone = true;
				}
				else{
					mValidPhone = false;
				}
			}
	       });	
	}
	
}
