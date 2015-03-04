package es.recicloid.models;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectionRequest extends Request{
	private int mId;
	private ArrayList<Furniture> mFurnitures;
	private CollectionPoint mCollectionPoint;
	
	public CollectionRequest(){
		mFurnitures = new ArrayList<Furniture>();
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
	
	public CollectionRequest(Parcel in){
		mFurnitures = new ArrayList<Furniture>();
		mCollectionPoint = new CollectionPoint();
		readFromParcel(in);
	}	

    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(Parcel dest, int flags) {
    	super.writeToParcel(dest, flags);
    	dest.writeInt(mId);
    	dest.writeTypedList(mFurnitures); 
    	dest.writeParcelable(mCollectionPoint, flags);
    }
    
    public void readFromParcel(Parcel in) {
    	super.readFromParcel(in);
    	mId = in.readInt();
    	in.readTypedList(mFurnitures, Furniture.CREATOR);
    	mCollectionPoint = (CollectionPoint)
    			in.readParcelable(CollectionPoint.class.getClassLoader());
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
		if(getFurnitures() != null){
			for(Furniture furniture : getFurnitures()){
				cad = cad + furniture.toString();
			}
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

	public boolean equals(CollectionRequest obj){
		return obj.getFch_collection() == this.mFch_collection &&
				obj.getFch_request() == this.mFch_request &&
				obj.getTelephone() == this.mTelephone &&
				Furniture.countFurnituresArray(obj.getFurnitures()) == 
						Furniture.countFurnituresArray(this.getFurnitures());
	}
	
	public static final Parcelable.Creator<CollectionRequest> 
	CREATOR = new Parcelable.Creator<CollectionRequest>() {
		public CollectionRequest createFromParcel(Parcel in) {
			return new CollectionRequest(in);
		}

		public CollectionRequest[] newArray(int size) {
			return new CollectionRequest[size];
		}
	};
}
