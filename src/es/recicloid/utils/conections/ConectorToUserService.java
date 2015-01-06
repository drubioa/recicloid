package es.recicloid.utils.conections;

import org.apache.http.HttpResponse;

import es.recicloid.models.User;

public interface ConectorToUserService {

	/**
	 * Se elimina un usuario y su telefono de contacto del sistema.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public HttpResponse deleteUser(User user) throws Exception;
	
}
