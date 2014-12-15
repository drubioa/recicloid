package es.recicloid.models;

import java.io.Serializable;

import org.joda.time.LocalDate;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Clase que representra una solicitud de recogida pendiente de confirmacion.
 * @author Diego Rubio Abujas (dbiosag@gmail.com)
 *
 */
public class ProvisionalAppointment
	implements Parcelable,Serializable{	
	private static final long serialVersionUID = 1L;
	private String telephone;
	private int collectionPointId;
	protected int collectionDay,collectionMonth,collectionYear;
	protected int requestDay,requestMonth,requestYear;
	private LocalDate fch_collection;
	private LocalDate fch_request;
	private int num_furnitures;

	 private ProvisionalAppointment(Parcel in) {
		 telephone = in.readString();
		 collectionPointId = in.readInt();
		 collectionDay = in.readInt();
		 collectionMonth = in.readInt();
		 collectionYear = in.readInt();
		 requestDay = in.readInt();
		 requestMonth = in.readInt();
		 requestYear = in.readInt();
		 num_furnitures = in.readInt();
     }

	
	public ProvisionalAppointment(){
	}

	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public int getCollectionPointId() {
		return collectionPointId;
	}


	public void setCollectionPointId(int collectionPointId) {
		this.collectionPointId = collectionPointId;
	}


	public LocalDate getFch_collection() {
		if(fch_collection == null){
			fch_collection = new LocalDate(collectionYear,
					collectionMonth,collectionDay);
		}
		return fch_collection;
	}


	public void setFch_collection(LocalDate fch_collection) {
		this.fch_collection = fch_collection;
	}


	public LocalDate getFch_request() {
		if(fch_request == null){
			fch_request = new LocalDate(requestYear,
					requestMonth,requestDay);
		}
		return fch_request;
	}


	public void setFch_request(LocalDate fch_request) {
		this.fch_request = fch_request;
	}


	public int getNumFurnitures() {
		return num_furnitures;
	}


	public void setNumFurnitures(int num_furnitures) {
		this.num_furnitures = num_furnitures;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(telephone);
		dest.writeInt(collectionPointId);
		dest.writeInt(collectionDay);
		dest.writeInt(collectionMonth);
		dest.writeInt(collectionYear);
		dest.writeInt(requestDay);
		dest.writeInt(requestMonth);
		dest.writeInt(requestYear);
		dest.writeInt(num_furnitures);
	}
	
	 public static final Parcelable.Creator<ProvisionalAppointment> CREATOR
	 	= new Parcelable.Creator<ProvisionalAppointment>() {
		 	public ProvisionalAppointment createFromParcel(Parcel in) {
		 		return new ProvisionalAppointment(in);
		 	}

		 	public ProvisionalAppointment[] newArray(int size) {
		 		return new ProvisionalAppointment[size];
		 	}
	 };


	
}
