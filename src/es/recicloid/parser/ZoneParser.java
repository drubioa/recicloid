package es.recicloid.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

public class ZoneParser extends Parser{
	
	public List<LatLng> parse(InputStream in)
			throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readZone(parser);
        } finally {;
            in.close();
        }
    }
	
	private List<LatLng> readZone(XmlPullParser parser) 
			throws XmlPullParserException, IOException {
		List<LatLng> points = new ArrayList<LatLng>();

	    parser.require(XmlPullParser.START_TAG, ns, "zone");
	    
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("item")) {
	        	points.add(readPoint(parser));
	        } else {
	            skip(parser);
	        }
	    }  
	    return points;
	}
	
	private LatLng readPoint(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "item");
	    Double latitude = null;
	    Double longitude = null;

	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("latitude")) {
	        	latitude = readLatitude(parser);
	        } else if (name.equals("longitude")) {
	        	longitude = readLongitude(parser);
	        } else {
	            skip(parser);
	        }
	    }
	    return new LatLng(latitude, longitude);
	}
	
	// Processes longitude
	private Double readLatitude(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "latitude");
	    String latitude = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "latitude");
	    return Double.valueOf(latitude);
	}
	
	// Processes latitude
	private Double readLongitude(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "longitude");
	    String longitude = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "longitude");
	    return Double.valueOf(longitude);
	}
	
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
}
