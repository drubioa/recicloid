package es.recicloid.main;

import java.util.ArrayList;

import es.recicloid.adapters.TitleWithImg;

/**
 * Vista de la actividad del menu principal de la app de recicloid.
 * @author Diego Rubio Abujas (dbiosag@gmail.com)
 *
 */
public interface MainView {
	/**
	 * 
	 * @return Array with all options icons and text of Main Menu List.
	 */
	public ArrayList<TitleWithImg> getOptiones();
	
	public void setOnMainMenuItemClickListener();
	
	public void setMainMenuAdapter();
	
}
