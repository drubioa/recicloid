package es.recicloid.clases;

import java.util.Date;

public abstract class Request {
	private String telephone;
	private int collectionPointId;
	private Date fch_request;
	private Date fch_collection;
	private int num_furnitures;
	private static final int MAX_FURNITURES = 4;
	
	public Request(){
		
	}

	public Request(int num_furnitures,String telephone,int collectionPointId,
			Date fch_collection) throws Exception{
		setNumFurnitures(num_furnitures);
		this.telephone = telephone;
		this.fch_collection = fch_collection;
		this.fch_request = new Date();
		this.collectionPointId = collectionPointId;
	}
	
	public Date getFch_request() {
		return fch_request;
	}

	public void setFch_request(Date fch_request) {
		this.fch_request = fch_request;
	}

	public Date getFch_collection() {
		return fch_collection;
	}

	public void setFch_collection(Date fch_collection) {
		this.fch_collection = fch_collection;
	}


	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getNumFurnitures() {
		return num_furnitures;
	}

	public void setNumFurnitures(int furnitures) throws Exception{
		if(furnitures > MAX_FURNITURES || furnitures == 0){
			throw new Exception("Invalid number of furnitres in this request");
		}
		this.num_furnitures = furnitures;
	}

	public int getCollectionPointId() {
		return collectionPointId;
	}

	public void setCollectionPointId(int collectionPointId) {
		this.collectionPointId = collectionPointId;
	}
	
}
