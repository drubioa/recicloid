package es.uca.recicloid.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class GetAddressTask extends
	AsyncTask<Location, Void, Address> {
	Context mContext;
	private ProgressDialog progressDialog;

	public GetAddressTask(Context context){
		super();
		mContext = context;
	}
	
	protected Dialog onCreateDialog() {
	    progressDialog = new ProgressDialog(mContext);
	    progressDialog.setMessage("Obteniendo dirección");
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
	    progressDialog.show();
	    return progressDialog;
	}
	

	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        onCreateDialog();
    }
	
	/**
	* Get a Geocoder instance, get the latitude and longitude
	* look up the address, and return it
	*
	* @params params One or more Location objects
	* @return A string containing the address of the current
	* location, or an empty string if no address can be found,
	* or an error message
	*/
	@Override
	protected Address doInBackground(Location... params) {
		Geocoder geocoder =
	        new Geocoder(mContext, Locale.getDefault());
		
		// Get the current location from the input parameter list
		Location loc = params[0];
		// Create a list to contain the result address
		List<Address> addresses = null;
		try {
		    /*
		     * Return 1 address.
		     */
		    addresses = geocoder.getFromLocation(loc.getLatitude(),
		            loc.getLongitude(), 1);
		} catch (IOException e1) {
			Log.e("LocationSampleActivity",
			        "IO Exception in getFromLocation()");
			e1.printStackTrace();
			return  null;
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
			return null;
		}
		// If the reverse geocode returned an address
		if (addresses != null && addresses.size() > 0) {
		    // Get the first address
		    Address address = addresses.get(0);
		    progressDialog.dismiss();
		    // Return the text
		    return address;
		} else {
			progressDialog.dismiss();
			return null;
		}
	}
	
	
}