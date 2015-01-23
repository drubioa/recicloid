package es.recicloid.SolicitudRecogida;

import java.io.IOException;

import android.widget.Button;
import android.widget.EditText;

import es.recicloid.utils.conections.ConectorToUserService;

public interface DatosContactoView {
	public ConectorToUserService getConector() throws IOException;
	
	/**
	* Se anade un Listener que permita continuar al boton de
	* continuar con solicitud de recogida.
	* @param btn_continue
	*/
	public void addListenerToBtnContinue(Button btn_continue);
	
	
	/**
	 * Se crea un Listener para el campo nombre que compruebe que 
	 * el telefono es un n??mero de tel??fono v
	 * @param name
	 */
	public void addListenerToEditTextName(EditText name);
	
	
	/**
	 * Se comprueba que el numero de telefono de contacto es un telefono
	 * valido.
	 * @param phone
	 */
	public void addListenerToEditTextPhone(EditText phone);
	
	/**
	 * Se muestra un mensaje de error debido a que no se ha indiado
	 * una localizacion dentro del termino municipal.
	 */
	public void showDialogAlert(int titleStr,int  descriptionStr,String tagName);
}
