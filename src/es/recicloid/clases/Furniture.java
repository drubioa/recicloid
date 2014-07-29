package es.recicloid.clases;

import android.os.Parcel;
import android.os.Parcelable;


public class Furniture implements Parcelable{
	private int idImg,idText;
	private int mNum;
	private String mName;
	private int category;
	
	public Furniture(String name,int category,int idText,int idImg){
		this.mName = name;
		this.category = category;
		this.setIdImg(idImg);
		this.setIdText(idText);
		this.mNum = 0;
	}

	public Furniture(Parcel in){
		idImg = in.readInt();	
		idText = in.readInt();
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
		dest.writeInt(idImg);
		dest.writeInt(idText);
		dest.writeInt(mNum);
		dest.writeString(mName);
		dest.writeInt(category);
	}
	
	public int getIdImg() {
		return idImg;
	}

	public void setIdImg(int idImg) {
		this.idImg = idImg;
	}

	public String getName() {
		return mName;
	}
	
	public int getIdText() {
		return idText;
	}

	public int getCategory() {
		return category;
	}
	
	public void setIdText(int idText) {
		this.idText = idText;
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

	 public static final Parcelable.Creator<Furniture> CREATOR = new Parcelable.Creator<Furniture>() {
	        public Furniture createFromParcel(Parcel in) {
	            return new Furniture(in);
	        }

	        public Furniture[] newArray(int size) {
	            return new Furniture[size];
	        }
	    };
	
}
