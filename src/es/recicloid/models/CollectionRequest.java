package es.recicloid.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectionRequest extends Request implements Parcelable{
	private int mId;
	private ArrayList<Furniture> mFurnitures;
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
		mFurnitures = (ArrayList<Furniture>) furnitures;
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
		this.mFurnitures = (ArrayList<Furniture>) furnitures;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
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
		String cad = "Telephone: "+getTelephone()+"\n"+
				"numFurnitures: "+getNumFurnitures()+"\n"+
				"fch_collection: "+getFch_collection()+"\n"+
				"fch_request: "+getFch_request()+"\n"+
				"collectionPointId: "+getCollectionPointId();
		for(Furniture furniture : getFurnitures()){
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeString(mTelephone);
		dest.writeInt(mCollectionPointId);
		dest.writeInt(collectionDay);
		dest.writeInt(collectionMonth);
		dest.writeInt(collectionYear);
		dest.writeInt(requestDay);
		dest.writeInt(requestMonth);
		dest.writeInt(requestYear);
		dest.writeInt(mNum_furnitures);
		dest.writeParcelableArray(mFurnitures.toArray(
				new Furniture[mFurnitures.size()]), 0);
		dest.writeParcelable(mCollectionPoint, 0);
	}
}
