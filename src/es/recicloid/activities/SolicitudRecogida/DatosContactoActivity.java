package es.recicloid.activities.SolicitudRecogida;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import es.recicloid.dialogs.DialogAlert;
import es.recicloid.models.User;
import es.recicloid.utils.conections.ConectionToPostNewUser;
import es.recicloid.utils.json.JsonToFileManagement;
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
import android.webkit.WebView;
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
	@InjectView(R.id.webViewDatosContacto) private WebView textDatosContacto;
	private boolean mEditTextNameValid;
	private boolean mEditTextTelValid;
	private ConectionToPostNewUser conector; 
	private final String FILENAME = "user.json";
	private JsonToFileManagement jsonToFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textDatosContacto.loadData(getString(R.string.info_datos_contacto),"text/html", "utf-8");
		conector = new ConectionToPostNewUser(this);
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

	public void addListenerToBtnContinue(Button btn_continue){
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
							try {
								User user = new User(getUserName(), getUserPhone());
								conector.execute(user);
								if(!conector.get()){
									Log.e("DatosContactoActivity",
											"Cannot create user");
									if(conector.exception != null){
										throw conector.exception;
									}
								}
							}catch(Exception e){
								Log.w("DatosContactoActivity","No se pudo crear el usuario "+e.toString());
							}
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
						}
				    }}, 3000);  // 3000 milliseconds
            }
        });	
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
				if(s.length() < 3){
					mEditTextNameValid = false;
				}
				else{
					mEditTextNameValid = User.isValidName(s);
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
	public void addListenerToEditTextPhone(EditText phone){
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

	public void showDialogAlert(int titleStr,int  descriptionStr,String tagName){
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", titleStr);
		args.putInt("description",descriptionStr);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, tagName);		
	}
	
	private String getUserPhone(){
		return editTextPhone
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
