package es.recicloid.models;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mPhone,mName;

	public User(String name,String phone){
		if(!validPhone(phone)){
			throw new IllegalArgumentException(phone + " is a invalid phone number.");
		}
		mName = name;
		mPhone = phone;
	}
	
	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phoneNumber) {
		if(!validPhone(mPhone)){
			throw  new IllegalArgumentException("Invalid phone number "
					+phoneNumber);
		}
		this.mPhone = phoneNumber;
	}

	public static boolean validPhone(String phone_number){
		 try  
		  {  
		    Integer.parseInt(phone_number);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		if(phone_number.length() != 9){
			return false;
		}
		if(phone_number.charAt(0) != '6' && phone_number.charAt(0) == '9'
				&& phone_number.charAt(0) == '8'){
			return false;
		}
		return true;
	}
	
	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
	
	static public boolean isValidName(CharSequence s){
		boolean isValidName = true;
		for(int i = 0;i < s.length() && isValidName;i++){
			if(!Character.isLetter(s.charAt(i)) & 
					!Character.isWhitespace(s.charAt(i))){
				isValidName = false;
			}
		}
		return isValidName;
	}
	
	public boolean equals(User obj){
		return obj.getPhone().equals(mPhone);
	}
}
