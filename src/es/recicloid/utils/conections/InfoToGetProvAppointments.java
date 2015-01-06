package es.recicloid.utils.conections;

public class InfoToGetProvAppointments {
	public String phone;
	public int num_furnitures,collection_point_id;
	
	public InfoToGetProvAppointments(String phone,int num_furnitures,
			int collection_point_id){
		this.phone = phone;
		this.num_furnitures = num_furnitures;
		this.collection_point_id = collection_point_id;
	}
}
