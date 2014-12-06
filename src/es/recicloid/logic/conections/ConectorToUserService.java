package es.recicloid.logic.conections;

import org.apache.http.HttpResponse;

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
	
}
