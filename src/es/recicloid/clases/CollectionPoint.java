package es.recicloid.clases;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectionPoint implements Parcelable,Serializable{

	private static final long serialVersionUID = 1L;
	double mLng,mLat;
	
	public CollectionPoint(double lng,double lat){
		mLng = lng;
		mLat = lat;
	}
	
	public double getLng(){
		return mLng;
	}
	
	public double getLat(){
		return mLat;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(mLng);
		dest.writeDouble(mLat);
	}
	
}
