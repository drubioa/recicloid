package es.recicloid.clases;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;


public class Furniture implements Parcelable{
	private String idImg,idText;
	private int mNum;
	private String mName;
	private int category;
	
	public Furniture(String name,int category,String idText2,String idImg2){
		this.mName = name;
		this.category = category;
		this.setIdImg(idImg2);
		this.setIdText(idText2);
		this.mNum = 0;
	}

	public Furniture(Parcel in){
		idImg = in.readString();	
		idText = in.readString();
		mNum = in.readInt();
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
		dest.writeInt(mNum);
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

	public int getmNum() {
		return mNum;
	}

	public void setNum(int num) {
		mNum = num;
	}

	public void setmAumentarEnUno() {
		this.mNum += 1;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
    public String toString() {
        return mName;
    }

	 /** Cuenta el numero total de muebles y enseres
	 * @param furnitures
	 * @return
	 */
	public static int countFurnitures(ArrayList<Furniture> furnitures) {
		int total = 0;
		for(int i = 0;i < furnitures.size();i++){
			total += furnitures.get(i).getmNum();
		}
		return total;
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
