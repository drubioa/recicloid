package es.recicloid.activities.SolicitudRecogida;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import es.recicloid.models.CollectionRequest;
import es.recicloid.models.Furniture;
import es.uca.recicloid.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class InfoCollectionDateFragment extends DialogFragment{
	private List<String> mFurnitures;
	private LocalDate mFecha;
	
	public void setFurnitures(ArrayList<String> f){
		mFurnitures = f;
	}
	
	public void setCollectionRequest(LocalDate fecha){
		mFecha = fecha;
	}

	 public static  InfoCollectionDateFragment newInstance(CollectionRequest req) {
		 InfoCollectionDateFragment f = new InfoCollectionDateFragment();

	     Bundle args = new Bundle();
	     f.setCollectionRequest(req.getFch_collection());
	     ArrayList<String> funritures = new ArrayList<String>();
	     for(Furniture furniture : req.getFurnitures()){
	    	 for(int i = 0 ; i < furniture.getCantidad();i++){
	    		 funritures.add(furniture.getName());
	    	 }
	     }
	     args.putStringArrayList("furnitures", funritures);
	     args.putInt("mDay", req.getFch_collection().getDayOfMonth());
	     args.putInt("mMonth", req.getFch_collection().getMonthOfYear());
	     args.putInt("mYear", req.getFch_collection().getYear());
	     f.setFurnitures(funritures);
	     f.setArguments(args);
	     
	     return f;
	 }
	 
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState){
		 Bundle bundle = getArguments();
		 if(bundle != null){
			 mFurnitures = bundle.getStringArrayList("furnitures");
			 mFecha = new LocalDate(bundle.getInt("mYear"),bundle.getInt("mMonth"),
					 bundle.getInt("mDay"));
		 }
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	     String title = new String(getResources().getString(R.string.dialog_info_solicitud_enseres_descr)+
	    		 " "+mFecha.getDayOfMonth()+
	    		 "/"+mFecha.getMonthOfYear()+"/"+mFecha.getYear());
		 builder.setTitle(title);
		 builder.setItems(getFurnituresNames(),
				 new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
         }
		 });	
		 builder.setPositiveButton(R.string.dialog_ok, 
				 new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {

             }
         });
		 return builder.create();
	}
	 
	private  CharSequence[] getFurnituresNames(){
		CharSequence[] furnituresNames = new CharSequence[mFurnitures.size()];
		for(int i = 0; i < mFurnitures.size();i++){
			Log.i("getFurnituresNames","@string/item_"+mFurnitures.get(i));
			int stringId = getResources().getIdentifier("@string/item_"+mFurnitures.get(i),
					"string", getActivity().getPackageName());        
			Log.i("getFurnituresNames","id: "+stringId);
			furnituresNames[i] = getActivity().getString(stringId);
		}
		return furnituresNames;
	}
}
