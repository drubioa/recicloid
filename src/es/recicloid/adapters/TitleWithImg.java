package es.recicloid.adapters;

public class TitleWithImg extends Title{
	int imageId;
	
	public TitleWithImg(int imageId,String tit, String sub) {
		super(tit, sub);
		this.imageId = imageId;
	}

    public int getImageId(){
        return imageId;
    }
}
