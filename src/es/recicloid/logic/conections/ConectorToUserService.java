package es.recicloid.logic.conections;

import org.apache.http.HttpResponse;

import es.recicloid.clases.User;

public interface ConectorToUserService {

	/**
	 * Se crea un nuevo usuario que solicita recogida de muebles y enseres.
	 * @param userName
	 * @param phoneNumber
	 * @return
	 * @throws Exception 
	 */
	public HttpResponse postNewUser(String userName,String phoneNumber) 
			throws Exception;
	/**
	 * Se elimina un usuario y su telefono de contacto del sistema.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public HttpResponse deleteUser(User user) throws Exception;
	
}
