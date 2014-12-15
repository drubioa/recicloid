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

	public String getDirection() {
		return mDirection;
	}

	public void setDirection(String mDirection) {
		this.mDirection = mDirection;
	}
}
