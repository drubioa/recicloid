package es.recicloid.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class Furniture implements Parcelable,Serializable{

	private static final long serialVersionUID = 1L;
	private int mId;
	private String idImg,idText;
	private int cantidad;
	private String mName;
	private int category;
	
	public Furniture(int id,int cantidad){
		this.mId = id;
		this.cantidad = cantidad;
	}
	
	public Furniture(int id ,String name,int category,String idText2,String idImg2){
		this.mId = id;
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
		if(cantidad > 1){
			return mName+"  x"+cantidad;
		}
		else{
			return mName;
		}
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
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public static String[] toStringArray(List<Furniture> furnitures){
		List<String> list = new ArrayList<String>();
		for(Furniture f : furnitures){
			for(int i = 0;i < f.getCantidad();i++){
				list.add(f.getName());
			}
		}
		return list.toArray(new String[Furniture.countFurnituresArray(furnitures)]);
	}
	
	public static Integer[] toIntegerArray(List<Furniture> furnitures){
		List<Integer> list = new ArrayList<Integer>();
		for(Furniture f : furnitures){
			for(int i = 0;i < f.getCantidad();i++){
				list.add(f.getId());
			}
		}
		return list.toArray(new Integer[Furniture.countFurnituresArray(furnitures)]);
	}
	
	/**
	 * Busca entre un listado de enseres el enser con el id indicado.
	 * @param id
	 * @param list
	 * @return nulo en caso de no encontrado, el tipo de mueble en otro caso.
	 */
	public static Furniture findFurniture(int id,List<Furniture> list){
		for(Furniture f: list){
			if(f.getId() == id){
				return f;
			}
		}
		return null;
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
	
	public boolean equals(Furniture obj){
		return obj.getId() == mId;
	}
}
