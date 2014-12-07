package es.recicloid.clases;

import java.util.List;

public class CollectionRequest extends ProvisionalAppointment{
	private int id;
	private List<Furniture> furnitures;
	
	public List<Furniture> getFurnitures() {
		return furnitures;
	}
	public void setFurnitures(List<Furniture> furnitures) {
		this.furnitures = furnitures;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
