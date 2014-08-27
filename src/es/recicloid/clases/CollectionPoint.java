package es.recicloid.clases;

public class CollectionPoint {
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
	
}
