package es.recicloid.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import es.recicloid.clases.Furniture;

public class ItemsParser extends Parser{
	private int category;
	
	public ItemsParser(int category){
		this.category = category;
		
	}
	
	public List<Furniture> parse(InputStream in)
			throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFurnitures(parser);
        } finally {;
            in.close();
        }
    }

	private List<Furniture> readFurnitures(XmlPullParser parser) 
			throws XmlPullParserException, IOException {
		List<Furniture> furnitures = new ArrayList<Furniture>();
		 parser.require(XmlPullParser.START_TAG, ns, "furnitures");
		 while (parser.next() != XmlPullParser.END_TAG) {
			 if (parser.getEventType() != XmlPullParser.START_TAG) {
		            continue;
			 }
			 String name = parser.getName();
		     // Starts by looking for the entry tag
		     if (name.equals("item")) {
		    	 furnitures.add(readFurniture(parser));
		     }else {
		          skip(parser);
		     }
		 }
		return furnitures;
	}

	private Furniture readFurniture(XmlPullParser parser) 
			throws XmlPullParserException, IOException {
		String idImg = null,idText = null,title = null;
		parser.require(XmlPullParser.START_TAG, ns, "item");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("title")) {
	        	title = readTag(parser,name);
	        }else if(name.equals("idText")){
	        	idText = readTag(parser,name);
	        }else if(name.equals("idImg")){
	        	idImg = readTag(parser,name);
	        }else{
	            skip(parser);
	        }
		 }
		if(title != null && idImg != null && idText != null){
			return new Furniture(title,category,idText,idImg);
		}
		else{
			throw new XmlPullParserException("Invalid tags in file "+filename+".");
		}
	}
	
	private String readTag(XmlPullParser parser,String tagName) 
			throws XmlPullParserException, IOException{
		parser.require(XmlPullParser.START_TAG, ns, tagName);
	    String name = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, tagName);
	    return name;		
	}
	
	private String readText(XmlPullParser parser) 
			throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
}
