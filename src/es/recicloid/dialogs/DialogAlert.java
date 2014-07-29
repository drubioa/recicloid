package es.recicloid.dialogs;

import es.uca.recicloid.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DialogAlert extends DialogFragment {

	public static final String TAG = "DialogAlert";
	
	public static DialogAlert newInstance (Bundle args){
		DialogAlert f = new DialogAlert();
        f.setArguments(args);
        return f;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		int description = getArguments().getInt("description");
		
		return new AlertDialog.Builder(getActivity())
			.setMessage(description)
            .setTitle(title)
            .setPositiveButton(getResources().getString(R.string.dialog_ok), 
            		   new DialogInterface.OnClickListener() {
            	
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               })
			.create();
    }
   
}