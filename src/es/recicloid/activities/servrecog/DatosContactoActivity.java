package es.recicloid.activities.servrecog;

import java.io.IOException;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import es.recicloid.clases.User;
import es.recicloid.dialogs.DialogAlert;
import es.recicloid.json.JsonToFileManagement;
import es.recicloid.logic.conections.ConectorToUserService;
import es.recicloid.logic.conections.ConectorToUserServiceImp;
import es.uca.recicloid.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.ProgressDialog;
import android.content.Intent;
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
@ContentView(R.layout.activity_datos_contacto)
public class DatosContactoActivity extends RoboFragmentActivity {
	@InjectView(R.id.editTextNameOfContact) private EditText editTextName;
	@InjectView(R.id.editTextPhone) private EditText editTextPhone;
	@InjectView(R.id.button) private Button btn_continue;
	private boolean mEditTextNameValid;
	private boolean mEditTextTelValid;
	private ConectorToUserService conector; 
	private final String FILENAME = "user.json";
	private JsonToFileManagement jsonToFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			conector = new ConectorToUserServiceImp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		jsonToFile = new JsonToFileManagement(this,FILENAME); 
		if(savedInstanceState == null){
			mEditTextNameValid = false;
			mEditTextTelValid = false;
		}
		else{
			mEditTextNameValid = savedInstanceState.getBoolean("editTextNameValid");
			mEditTextTelValid = savedInstanceState.getBoolean("editTextTelValid");
		}
		
		addListenerToBtnContinue(btn_continue);
		isValidTextActiveBtn();
		addListenerToEditTextName(editTextName);
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
							Intent intent = new Intent(DatosContactoActivity
									.this,CondicionesUsoActivity.class);
							conector.postNewUser(getUserName(), getUserPhone());
							Log.i("DatosContactoActivity",
									"registrado nuevos datos de contacto");
							jsonToFile
								.saveInJsonFile(new User(getUserName(),getUserPhone()));
							dialog.dismiss();
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
		return editTextName
				.getText().toString();			
	}
	
	/**
	 * 
	 * @return nombre introducido por el usuario
	 */
	private String getUserName(){
		return editTextName
				.getText().toString();	
	}
	
	private void isValidTextActiveBtn(){
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
