package es.recicloid.utils.conections;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

import es.recicloid.models.CollectionRequest;
import es.recicloid.models.ProvisionalAppointment;

public interface ConectorToDailyAppointmentService {

	/**
	 * 
	 * @param phone
	 * @param num_furnitures
	 * @param collection_point_id
	 * @return ProvisionalAppointment for collection furnitures.
	 * @throws Exception
	 */
	public List<ProvisionalAppointment> getProvisionalAppointments(String phone,
			int num_furnitures,int collection_point_id) throws Exception;
	
	/**
	 * 
	 * @param appointment
	 * @return HttpResponse with OK if sucess result.
	 * @throws Exception
	 */
	public HttpResponse confirmAppointment(CollectionRequest appointment) 
	throws Exception;
	
	/**
	 * 
	 * @param phone_number
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public HttpResponse deletePendingAppointments(String phone_number) 
			throws URISyntaxException, HttpException, IOException;
	
}