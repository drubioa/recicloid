package es.recicloid.activities.servrecog;

import java.io.IOException;

import es.recicloid.dialogs.DialogAlert;
import es.recicloid.logic.conections.ConectorToUserService;
import es.recicloid.logic.conections.ConectorToUserServiceImp;
import es.uca.recicloid.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
public class DatosContactoActivity extends FragmentActivity {
	private boolean mEditTextNameValid;
	private boolean mEditTextTelValid;
	private ConectorToUserService conector; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_contacto);
		try {
			conector = new ConectorToUserServiceImp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	 * Se añade un Listener que permita continuar al boton de
	 * continuar con solicitud de recogida.
	 * @param btn_continue
	 */
	private void addListenerToBtnContinue(Button btn_continue){
		btn_continue.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final ProgressDialog dialog = ProgressDialog
						.show(DatosContactoActivity.this,
						"",getResources().getString(R.string.message_registrer_user) , true); 
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				    public void run() {
						try {
							conector.postNewUser(getUserName(), getUserPhone());
							Log.i("DatosContactoActivity",
									"registrado nuevos datos de contacto");
							dialog.dismiss();
							Intent intent = new Intent(DatosContactoActivity
										.this,CondicionesUsoActivity.class);
							intent.putExtra("user_name", getUserName());
							intent.putExtra("user_phone", getUserPhone());
							startActivity(intent); 
						} catch (Exception e) {
							Log.w("DatosContactoActivity",e.getMessage());
							dialog.dismiss();
							if(e.getMessage().contains("HTTP error code : 500")){
								showDialogAlert(R.string.dialog_title_not_valid_user,
										R.string.dialog_title_not_valid_user_descr,"tagDialError");
							}
							e.printStackTrace();
						}
				    }}, 3000);  // 3000 milliseconds
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
	 * Se comprueba que el numero de telefono de contacto es un telefono
	 * valido.
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
	
	/**
	 * Se muestra un mensaje de error debido a que no se ha indiado
	 * una localización dentro del término municipal.
	 */
	private void showDialogAlert(int titleStr,int  descriptionStr,String tagName){
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", titleStr);
		args.putInt("description",descriptionStr);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, tagName);		
	}
	
	private String getUserPhone(){
		EditText editTextName = 
				(EditText)  findViewById(R.id.editTextPhone);
		return editTextName
				.getText().toString();			
	}
	
	/**
	 * 
	 * @return nombre introducido por el usuario
	 */
	private String getUserName(){
		EditText editTextName = 
				(EditText)  findViewById(R.id.editTextNameOfContact);
		return editTextName
				.getText().toString();	
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
