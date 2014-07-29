package es.recicloid.dialogs;

import es.recicloid.activities.SolicitudEnseresActivity;
import es.uca.recicloid.R;	
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class DialogMultiplesFurnitures  extends DialogFragment {

	public static DialogMultiplesFurnitures newInstance (String furniture){
		DialogMultiplesFurnitures f = new DialogMultiplesFurnitures();
		Bundle args = new Bundle();
        args.putString("furniture", furniture);
        f.setArguments(args);
        return f;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String[] items = new String[2];
		final SolicitudEnseresActivity activity = (SolicitudEnseresActivity) getActivity();
		final String furniture = getArguments().getString("furniture");

		AlertDialog.Builder builder = 
				new AlertDialog.Builder(activity);
		  items[0] = getResources().getString(
				  R.string.dialog_more_funitures_add);
		  items[1] = getResources().getString(
				  R.string.dialog_more_furnitures_reset);
		  builder.setTitle(getResources().getString(
		          R.string.dialog_more_furnitures_title))
		         .setItems(items, new DialogInterface.OnClickListener() {
		    	   
		      public void onClick(DialogInterface dialog, int item){
		    	  // Añadir un item a la colección
		    	  if(item == 0){
		    		  try {  
		    			  activity.addFunritureToCollection(furniture);
		    			  Log.i("DialogMultiplesFurnitures",
		    					  "Se añade un enser adicional a "+furniture);
		    		  }catch (Exception e) {
		    			  Log.e("DialogMultiplesFurnitures",e.toString());
		    			  e.printStackTrace();
		    		  }
			  	  }
		    	  // Reiniciar el numero de items a 0
		    	  else{
			    	  try{
			    		  activity.removeFurnitureToCollecton(furniture);
			    	      Log.i("DialogMultiplesFurnitures",
			    	    		  "Se reinicia el contador a cero de "+furniture);
			    	  }catch (Exception e) {
			    		  Log.e("DialogMultiplesFurnitures",e.toString());
					      e.printStackTrace();
			    	  }			          	
		    	  }
			  	  dialog.dismiss();
		      }});
          return builder.create();
	   }
}