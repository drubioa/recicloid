package es.recicloid.utils.map;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import es.recicloid.SolicitudRecogida.UbicacionRecogidaActivity;
import es.uca.recicloid.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

public class AddressGetter{
	public UbicacionRecogidaActivity activity;
	private Context mContext;
	private ProgressDialog progressDialog;
	
	public AddressGetter(Context context){
		mContext = context;
	}
	
	private void onCreateDialog() {
	    progressDialog = new ProgressDialog(mContext);
	    progressDialog.setMessage(mContext.getString(R.string.dialog_obtains_dir));
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progressDialog.setCancelable(false);
	    progressDialog.show();
	    Log.i("GetAddressTask","Show dialog");
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
	public Address obtainsAddress(Location loc) {
		Geocoder geocoder =
	        new Geocoder(mContext, Locale.getDefault());
		
		onCreateDialog();
		// Create a list to contain the result address
		List<Address> addresses = null;
		try {
		    /*
		     * Return 1 address.
		     */
		    addresses = geocoder.getFromLocation(loc.getLatitude(),
		            loc.getLongitude(), 1);
		} catch (IOException e1) {
			Log.e("GetAddressTask",
					e1.toString());
			progressDialog.cancel();
		} catch (IllegalArgumentException e2) {
			Log.e("GetAddressTask",e2.toString());
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(R.string.dialog_err_dir_exception)
		       .setTitle(R.string.dialog_err_dir_exception);
			builder.create();
			progressDialog.cancel();
		}
		// If the reverse geocode returned an address
		if (addresses != null && addresses.size() > 0) {
		    // Get the first address
		    Address address = addresses.get(0);
		    Log.i("GetAddressTask","return valid address; "+address.toString());
		    progressDialog.dismiss();
		    // Return the text
		    return address;
		} else if(addresses.size() > 0){
			Log.w("GetAddressTask","Multiples address");
			progressDialog.dismiss();
			return null;
		}
		else{
			Log.w("GetAddressTask","Cannot obtains address");
			progressDialog.dismiss();
			return null;
		}
	}
	
	
}