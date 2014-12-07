package es.recicloid.clases;

import org.joda.time.LocalDate;

public class ProvisionalAppointment{
	private String telephone;
	private int collectionPointId;
	protected int collectionDay,collectionMonth,collectionYear;
	protected int requestDay,requestMonth,requestYear;
	private LocalDate fch_collection;
	private LocalDate fch_request;
	private int num_furnitures;
	
	
	public ProvisionalAppointment(){
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public int getCollectionPointId() {
		return collectionPointId;
	}


	public void setCollectionPointId(int collectionPointId) {
		this.collectionPointId = collectionPointId;
	}


	public LocalDate getFch_collection() {
		return fch_collection;
	}


	public void setFch_collection(LocalDate fch_collection) {
		this.fch_collection = fch_collection;
	}


	public LocalDate getFch_request() {
		return fch_request;
	}


	public void setFch_request(LocalDate fch_request) {
		this.fch_request = fch_request;
	}


	public int getNum_furnitures() {
		return num_furnitures;
	}


	public void setNum_furnitures(int num_furnitures) {
		this.num_furnitures = num_furnitures;
	}

	
}
