package es.recicloid.UbicacionRecogida;

import java.io.IOException;

import es.recicloid.DatosContacto.DatosContactoActivity;
import es.recicloid.models.CollectionPoint;
import es.uca.recicloid.R;
import es.recicloid.utils.json.JsonToFileManagement;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


/**
 * Dialogo para confirmar la direccion
 * @author diegorubio
 *
 */
public class ConfirmarDireccionDialFrag extends DialogFragment{
	private CollectionPoint mCollectionPoint;
	
	ConfirmarDireccionDialFrag(CollectionPoint c){
		mCollectionPoint = c;
	};
	
	public static ConfirmarDireccionDialFrag newInstance(CollectionPoint c){
		ConfirmarDireccionDialFrag dialog = new ConfirmarDireccionDialFrag(c);
		Bundle args = new Bundle();
		args.putParcelable("mCollectionPoint", c);
		dialog.setArguments(args);
		return dialog;
	}
	
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceState){
		 setRetainInstance(true); 
		 Bundle bundle = getArguments();
		 if(bundle != null){
			 mCollectionPoint = bundle.getParcelable("mCollectionPoint");
		 }
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 builder.setTitle(getActivity().getString(R.string.dialog_confirmar_ubicacion));
		 builder.setMessage(mCollectionPoint.getDirection());
		 builder.setPositiveButton(getActivity().getString(R.string.dialog_ok),
		 new DialogInterface.OnClickListener(){
			@Override
			
			/**
			 * Si se acepta se almacena el punto de recogida en un fichero json
			 * y acontinuacion se lanza la actividad solicitar datos de contacto.
			 */
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = 
						new Intent(getActivity(),DatosContactoActivity.class);
				
				 final JsonToFileManagement jsonToFile = 
							new JsonToFileManagement(getActivity(),
									"collection-point.json");
				try {
					jsonToFile.saveInJsonFile(mCollectionPoint);
				} catch (IOException e) {
					Log.e("ConfirmarDireccionDialFrag",e.toString());
				}
				startActivity(intent);   
			}
		 });
		 builder.setNegativeButton(getActivity().getString(R.string.cancel),
		 new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((UbicacionRecogidaActivity) getActivity()).showsGenericViewOfCity();				
			}
		 });
		 return builder.create();
	 }

	 @Override
	 public void onDestroyView() {
	     if (getDialog() != null && getRetainInstance()) {
	         getDialog().setDismissMessage(null);
	     }
	     super.onDestroyView();
	 }

}
