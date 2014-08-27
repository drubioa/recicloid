package es.recicloid.activities.servrecog;

import es.uca.recicloid.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author Diego Rubio Abujas
 * @version 1.0
 * Actividad que gestiona los datos de contactos del usuario.
 * Esta actividad forma parte del proceso de solicitud de recogida
 * de muebles y enseres.
 */
public class DatosContactoActivity extends Activity {
	private boolean mEditTextNameValid;
	private boolean mEditTextTelValid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_contacto);
		
		if(savedInstanceState == null){
			mEditTextNameValid = false;
			mEditTextTelValid = false;
		}
		else{
			mEditTextNameValid = savedInstanceState.getBoolean("editTextNameValid");
			mEditTextTelValid = savedInstanceState.getBoolean("editTextTelValid");
		}
		
		Button btn_continue = (Button) findViewById(R.id.button);
		addListenerToBtnContinue(btn_continue);
		isValidTextActiveBtn();
		EditText editTextName = 
				(EditText)  findViewById(R.id.editTextNameOfContact);
		addListenerToEditTextName(editTextName);
		EditText editTextPhone = 
				(EditText)  findViewById(R.id.editTextPhone);
		addListenerToEditTextPhone(editTextPhone);
		
	}
	
	/**
	 * Se a??ade un Listener que permita continuar al boton de
	 * continuar con solicitud de recogida.
	 * @param btn_continue
	 */
	private void addListenerToBtnContinue(Button btn_continue){
		btn_continue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(DatosContactoActivity
						.this,CondicionesUsoActivity.class);
				startActivity(intent);   
            }
        });	
	}
	
	/**
	 * Se crea un Listener para el campo nombre que compruebe que 
	 * el telefono es un n??mero de tel??fono v
	 * @param name
	 */
	private void addListenerToEditTextName(EditText name){
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
				if(s.length() < 3){
					mEditTextNameValid = false;
				}
				else{
					mEditTextNameValid = true;
					for(int i = 0;i < s.length() && mEditTextNameValid;i++){
						if(!Character.isLetter(s.charAt(i)) & 
								!Character.isWhitespace(s.charAt(i))){
							mEditTextNameValid = false;
						}
					}
				}
				isValidTextActiveBtn();
			}
	       });	
	}
	
	/**
	 * Se comprueba que el n??mero de tel??fono de contacto es un tel??fono
	 * v??lido.
	 * @param phone
	 */
	private void addListenerToEditTextPhone(EditText phone){
		phone.addTextChangedListener(new TextWatcher() {

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
				if(s.length() != 9){
					mEditTextTelValid = false;
				}
				else{
					
					if(s.charAt(0) != '9' && 
							s.charAt(0) != '6' && 
							s.charAt(0) != '8'){
						mEditTextTelValid = false;
					}
					else{
						mEditTextTelValid = true;
					}
				}
				isValidTextActiveBtn();
			}
	    });	
	}
	
	private void isValidTextActiveBtn(){
		Button btn_continue = (Button) findViewById(R.id.button);
		if(mEditTextNameValid && mEditTextTelValid){
			btn_continue.setEnabled(true);
		}
		else{
			btn_continue.setEnabled(false);
		}	
	}
	
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
	   outState.putBoolean("editTextNameValid", mEditTextNameValid);
	   outState.putBoolean("editTextTelValid", mEditTextTelValid);
       super.onSaveInstanceState(outState);
	}	

}
