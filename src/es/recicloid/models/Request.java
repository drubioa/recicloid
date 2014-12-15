package es.recicloid.models;
import org.joda.time.LocalDate;


public abstract class Request {
	private String mTelephone;
	private int mCollectionPointId;
	protected int collectionDay,collectionMonth,collectionYear;
	protected int requestDay,requestMonth,requestYear;
	private LocalDate mFch_collection;
	private LocalDate mFch_request;
	protected int mNum_furnitures;
	
	
	public Request(){
		
	}
	
	public Request(int numFurnitures, String telephone,
			int collectionPointId, LocalDate fch_collection,
			LocalDate fch_request) {
		checkDateTimes(fch_collection,fch_request);
		mTelephone = telephone;
		mFch_collection = fch_collection;
		mFch_request = fch_request;
		mCollectionPointId = collectionPointId;
		mNum_furnitures = numFurnitures;
	}

	private void checkDateTimes (LocalDate fch_collection,
	LocalDate fch_request) {
		final LocalDate today = new LocalDate();
		if(fch_collection.isBefore(today)){
			throw new IllegalArgumentException("fch_collection ("+fch_collection+")"+
		" is before today.");
		}
		if(fch_request.isBefore(today)){
			throw new IllegalArgumentException("fch_request ("+fch_request+")"+
		" is before today.");
		}
		if(!fch_collection.isAfter(fch_request)){
			throw new IllegalArgumentException("fch_collection ("+fch_collection+")"+
		" must be after to fch_request ("+fch_request+").");
		}
	}
	
	public LocalDate getFch_request() {
		if(mFch_request == null && requestDay != 0 && 
				requestMonth != 0 && requestYear != 0){
			mFch_request = new LocalDate(requestYear,requestMonth,requestDay);
		}
		return mFch_request;
	}
	
	public LocalDate getFch_collection() {
		if(mFch_collection == null && collectionDay != 0 && 
				collectionMonth != 0 && collectionYear != 0){
			mFch_collection = new LocalDate(collectionYear,collectionMonth,collectionDay);
		}
		return mFch_collection;
	}

	public void setCollectionDay(int day){
		collectionDay = day;
	}
	
	public int getCollectionDay(){
		return collectionDay;
	}
	
	public void setCollectionMonth(int month){
		collectionMonth = month;
	}
	
	public int getCollectionMonth(){
		return collectionMonth;
	}

	public void setCollectionYear(int year){
		collectionYear = year;
	}
	
	public int getCollectionYear(){
		return collectionYear;
	}
	
	public void setRequestDay(int day){
		requestDay = day;
	}
	
	public int getRequestDay(){
		return requestDay;
	}
	
	public void setRequestMonth(int month){
		requestMonth = month;
	}

	public int getRequestMonth(){
		return requestMonth;
	}
	
	public void setRequestYear(int year){
		requestYear = year;
	}
	
	public int getRequestYear(){
		return requestYear;
	}
	
	
	public void setFch_request(LocalDate fch_request) {
		this.mFch_request = fch_request;
	}

	public void setFch_collection(LocalDate fch_collection) {
		this.mFch_collection = fch_collection;
	}


	public String getTelephone() {
		return mTelephone;
	}

	public void setTelephone(String telephone) {
		this.mTelephone = telephone;
	}

	public int getNumFurnitures() {
		return mNum_furnitures;
	}

	public void setNumFurnitures(int furnitures) throws IllegalArgumentException{
		if(furnitures < 1){
			throw new IllegalArgumentException("num of furnitures must be 1 or more.");
		}
		mNum_furnitures = furnitures;
	}

	public int getCollectionPointId() {
		return mCollectionPointId;
	}

	public void setCollectionPointId(int collectionPointId) {
		this.mCollectionPointId = collectionPointId;
	}
	
}
