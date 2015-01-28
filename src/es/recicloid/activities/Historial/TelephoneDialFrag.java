package es.recicloid.activities.Historial;

import roboguice.fragment.RoboDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText; 
import android.widget.Toast;
import es.recicloid.activities.main.MainActivity;
import es.uca.recicloid.R;


public class TelephoneDialFrag extends RoboDialogFragment{
	private boolean mValidPhone;
	private String mPhoneNumber;
	private EditText telephone;
	
	TelephoneDialFrag(){
		mValidPhone = false;
	}
	
	static TelephoneDialFrag newInstance(){
		return new TelephoneDialFrag();
		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		telephone = new EditText(getActivity());
		telephone.setInputType(InputType.TYPE_CLASS_PHONE);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(9);
		telephone.setFilters(filterArray);
		builder.setView(telephone)
		    // Add action buttons
				   .setTitle(R.string.menu_datos_contacto_telefono)
				   .setMessage(R.string.dialog_ask_telephone)
		           .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                  if(mValidPhone){
		                	  Log.i("TelephoneDialFrag","Valid phone number");
		                	  // Conectar y cargar solicitudes de recogida
		                	  ((HistorialActivity)  
		                			  getActivity()).findAndShowRequest(mPhoneNumber);
		                  }
		                  else{
		                	  // Mostrar Toast 'Numero No Valido' y volver al Menu principal.
		                	  Log.i("TelephoneDialFrag","Invalid phone number "+mPhoneNumber);
		                	  Toast.makeText(getActivity(), 
										getResources().getString(R.string.message_no_valid_phone),
										Toast.LENGTH_LONG).show();
		                	  Intent intent = new Intent(getActivity(), MainActivity.class);
		                	  startActivityForResult(intent, 0);
		                  }
		               }
		           });
			addListenerToEditTextName(telephone);
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
				if(s.length() == 9 && (s.charAt(0) != '6' ||  s.charAt(0) != '9')){
					Log.i("TelephoneDialFrag","Valid number");
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
