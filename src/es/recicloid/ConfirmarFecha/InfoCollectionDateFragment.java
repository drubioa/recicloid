package es.recicloid.ConfirmarFecha;

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

public class InfoCollectionDateFragment extends DialogFragment{
	private String mTitle;
	private List<String> mFurnitures;
	
	public void setTitle(String title){
		mTitle = title;
	}
	
	public void setFurnitures(ArrayList<String> f){
		mFurnitures = f;
	}

	 static  InfoCollectionDateFragment newInstance(CollectionRequest req) {
		 InfoCollectionDateFragment f = new InfoCollectionDateFragment();

	     Bundle args = new Bundle();
	     LocalDate fecha = req.getFch_collection();
	     String title = new String("Solicitud de recogida para el día "+fecha.getDayOfMonth()+
	    		 "/"+fecha.getMonthOfYear()+"/"+fecha.getYear());
	     f.setTitle(title);
	     args.putString("title", title);
	     ArrayList<String> funritures = new ArrayList<String>();
	     for(Furniture furniture : req.getFurnitures()){
	    	 funritures.add(furniture.getIdText());
	     }
	     args.putStringArrayList("furnitures", funritures);
	     f.setFurnitures(funritures);
	     f.setArguments(args);
	     
	     return f;
	 }
	 
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState){
		 Bundle bundle = getArguments();
		 if(bundle != null){
			 mTitle = bundle.getString("title");
			 mFurnitures = bundle.getStringArrayList("furnitures");
		 }
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		 builder.setTitle(mTitle);
		 builder.setItems(mFurnitures.toArray(new CharSequence[mFurnitures.size()]),
				 new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
         }
		 });	
		 builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {

             }
         });
		 return builder.create();
	 }
}
