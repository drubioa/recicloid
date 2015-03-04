package es.recicloid.models;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectionPoint implements Parcelable,Serializable{
	private static final long serialVersionUID = 1L;
	private double mLng,mLat;
	private int pointId;
	private String mDirection;
	
	public CollectionPoint(){}
	
	public CollectionPoint(double lng,double lat){
		mLng = lng;
		mLat = lat;
	}
	
	public CollectionPoint(Parcel in){
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(mLng);
		dest.writeDouble(mLat);
		dest.writeInt(pointId);
		dest.writeString(mDirection);
	}
	
	public void readFromParcel(Parcel in) {
		mLng = in.readDouble();
		mLat = in.readDouble();
		pointId = in.readInt();
		mDirection = in.readString();
	}

	public void setLatitude(double lat){
		mLat = lat;
	}
	
	public double getLng(){
		return mLng;
	}
	
	public double getLat(){
		return mLat;
	}
	
	public void setLongitude(double lng){
		mLng = lng;
	}
	
	public void setPointId(int id){
		pointId = id;
	}
	
	public int getId(){
		return pointId;
	}
	
	public String getDirection() {
		return mDirection;
	}

	public void setDirection(String mDirection) {
		this.mDirection = mDirection;
	}
	
	public boolean equals(CollectionPoint obj){
		return obj.getId() == this.pointId;
	}
	
	public static final Parcelable.Creator<CollectionPoint> 
	CREATOR = new Parcelable.Creator<CollectionPoint>() {
		public CollectionPoint createFromParcel(Parcel in) {
			return new CollectionPoint(in);
		}

		public CollectionPoint[] newArray(int size) {
			return new CollectionPoint[size];
		}
	};
}
