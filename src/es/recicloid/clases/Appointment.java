package es.recicloid.clases;

import java.util.ArrayList;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Appointment implements Parcelable{
	Date mCollectionDate;
	ArrayList<Furniture> mFurnitues;
	
	public Appointment(Date collectionDate, ArrayList<Furniture> furnitues ){
		mCollectionDate = collectionDate;
		mFurnitues = furnitues;
	}
	
	public Appointment(Parcel in){
		mCollectionDate = (java.util.Date) in.readSerializable();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	private void readFromParcel(Parcel in) {
		mCollectionDate	= (java.util.Date) in.readSerializable();
		//mFurnitues = in.readArrayList(Furniture.class.);
	}
	
	@Override
	public void writeToParcel(Parcel in, int args) {
		in.writeSerializable(mCollectionDate);
		in.writeList(mFurnitues);
	}
	
}
