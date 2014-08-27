package es.recicloid.dialogs;

import es.recicloid.activities.servrecog.UbicacionRecogidaActivity;
import es.uca.recicloid.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


	public class DialogSelecLocationType extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	 
	      final String[] items = new String[2];
	 
	        AlertDialog.Builder builder =
	        		new AlertDialog.Builder(getActivity());
	        items[0] = getResources().getString(
	        		R.string.dialog_selectlocation_auto);
	        items[1] = getResources().getString(
	        		R.string.dialog_selectlocation_manual);
	        builder.setTitle(getResources().getString(
	        		R.string.dialog_selectlocacion_title))
	           .setItems(items, new DialogInterface.OnClickListener() {
	        	   
	                public void onClick(DialogInterface dialog, int item) {
	                	if(item == 0){
		                	 UbicacionRecogidaActivity callingActivity = 
		                			 (UbicacionRecogidaActivity) getActivity();
		                	 try {
								callingActivity.getLocationByMobilePhone();
							} catch (Exception e) {
								e.printStackTrace();
							}
		                }
		                dialog.dismiss();
	                }
	                
	            });
	        return builder.create();
	    }
	}