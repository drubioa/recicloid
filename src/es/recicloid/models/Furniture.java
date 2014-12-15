package es.recicloid.models;

import java.io.Serializable;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class Furniture implements Parcelable,Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String idImg,idText;
	private int cantidad;
	private String mName;
	private int category;
	
	public Furniture(int id,int cantidad){
		this.id = id;
		this.cantidad = cantidad;
	}
	
	public Furniture(int id ,String name,int category,String idText2,String idImg2){
		this.id = id;
		this.mName = name;
		this.category = category;
		this.setIdImg(idImg2);
		this.setIdText(idText2);
		this.cantidad = 0;
	}

	public Furniture(Parcel in){
		idImg = in.readString();	
		idText = in.readString();
		cantidad = in.readInt();
		mName = in.readString();
		category = in.readInt();
		
		readFromParcel(in);
	}	
	
	private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(idImg);
		dest.writeString(idText);
		dest.writeInt(cantidad);
		dest.writeString(mName);
		dest.writeInt(category);
	}
	
	public String getIdImg() {
		return idImg;
	}

	public void setIdImg(String idImg2) {
		this.idImg = idImg2;
	}

	public String getName() {
		return mName;
	}
	
	public String getIdText() {
		return idText;
	}

	public int getCategory() {
		return category;
	}
	
	public void setIdText(String idText2) {
		this.idText = idText2;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int num) {
		cantidad = num;
	}

	public void setmAumentarEnUno() {
		this.cantidad += 1;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
    public String toString() {
        return "id: "+id+"\nname: "+mName+"\ncantidad"+cantidad+"\n";
    }

	 /** Cuenta el numero total de muebles y enseres
	 * @param furnitures
	 * @return
	 */
	public static int countFurnituresArray(List<Furniture> furnitures) {
		int total = 0;
		for(int i = 0;i < furnitures.size();i++){
			total += furnitures.get(i).getCantidad();
		}
		return total;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static final Parcelable.Creator<Furniture> 
		CREATOR = new Parcelable.Creator<Furniture>() {
		public Furniture createFromParcel(Parcel in) {
			return new Furniture(in);
	    }

	    public Furniture[] newArray(int size) {
	    	return new Furniture[size];
	    }
	};
	
}
