package es.recicloid.models;

import java.util.List;

public class CollectionRequest extends Request{
	private int id;
	private List<Furniture> mFurnitures;
	private CollectionPoint mCollectionPoint;
	
	public CollectionRequest(){
		
	}
	
	public CollectionRequest(ProvisionalAppointment appointment,List<Furniture> furnitures) 
			throws Exception{
		super(appointment.getNumFurnitures(),
				appointment.getTelephone(),
				appointment.getCollectionPointId(),
				appointment.getFch_collection(),
				appointment.getFch_request());
		if(furnitures.isEmpty()){
			throw new IllegalArgumentException("furnitures is empty.");
		}
		this.mFurnitures = furnitures;
		mNum_furnitures = numTotalFurniture(furnitures);
	}
	
	private static int numTotalFurniture(List<Furniture> furnitures){
		int totalFurnitures = 0;
		for(Furniture f: furnitures){
			totalFurnitures += f.getCantidad();
		}
		return totalFurnitures;
	}
	
	public List<Furniture> getFurnitures() {
		return mFurnitures;
	}

	public void setFurnitures(List<Furniture> furnitures) {
		super.setNumFurnitures(numTotalFurniture(furnitures));
		this.mFurnitures = furnitures;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CollectionPoint getCollectionPoint() {
		return mCollectionPoint;
	}
	
	public void setCollectionPoint(CollectionPoint collectionPoint) {
		this.mCollectionPoint = collectionPoint;
		super.setCollectionPointId(collectionPoint.getId());
	}
		
	@Override
	public String toString(){
		String cad = "Telephone: "+this.getTelephone()+"\n"+
				"numFurnitures: "+this.getNumFurnitures()+"\n"+
				"fch_collection: "+this.getFch_collection()+"\n"+
				"fch_request: "+this.getFch_request()+"\n"+
				"collectionPointId: "+this.getCollectionPointId();
		for(Furniture furniture : this.getFurnitures()){
			cad = cad + furniture.toString();
		}
		return cad;
	}

	/**
	 * Comprueba que todos los campos estan correctamente.
	 * @return
	 */
	public boolean checkCorrectRequest(){
		if(mFurnitures.size() == 0  || 
		getFch_collection() == null ||
		getFch_request() == null ||
		getNumFurnitures() <= 0 ||this.getTelephone().isEmpty()){
			return false;
		}
		if(!getFch_collection().isAfter(getFch_request())){
			return false;
		}
		int totalFurnitures = 0;
		for(Furniture f : mFurnitures){
			totalFurnitures += f.getCantidad();
		}
		if(totalFurnitures != getNumFurnitures()){
			return false;
		}
		return true;
	}
}
