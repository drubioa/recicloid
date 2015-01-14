package es.recicloid.ConfirmarFecha;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;
import es.recicloid.models.Furniture;
import es.uca.recicloid.R;

/**
 * Fragmento que muestra los enseres que seran recogidos en una fecha dada.
 * @author Diego Rubio Abujas
 *
 */
public class FurnitureSelectorDialFrag extends DialogFragment{
	private ArrayList<Furniture> mAllFurnitures,mFurnitureToRequest;
	private int mDay,mMonth,mYear;
	private int mNumPerDate;
	private List<Integer> mSelectedItems;
	private int cont = 0;
	
	public FurnitureSelectorDialFrag(){
		mFurnitureToRequest = new  ArrayList<Furniture>();
	}
	
	public void setCollDay(int day){
		mDay = day;
	}
	
	public void setCollMonth(int m){
		mMonth = m;
	}
	
	public void setCollYear(int y){
		mYear = y;
	}
	
	public void setAllFurnitures(List<Furniture> furnitures){
		mAllFurnitures = (ArrayList<Furniture>) furnitures;
	}
	
	public void setNumFurnituresPerDate(int n){
		mNumPerDate = n;
	}
	
	static FurnitureSelectorDialFrag newInstance(LocalDate date,int numPerDate,
			final List<Furniture> allFurnitures){
		FurnitureSelectorDialFrag f = new FurnitureSelectorDialFrag();
		Bundle args = new Bundle();
		f.setAllFurnitures(allFurnitures);
		args.putParcelableArrayList("allFurnitures",
				(ArrayList<Furniture>) allFurnitures );
		f.setCollDay(date.getDayOfMonth());
		f.setCollMonth(date.getMonthOfYear());
		f.setCollYear(date.getYear());
		args.putInt("mDay", date.getDayOfMonth());
		args.putInt("mMonth", date.getMonthOfYear());
		args.putInt("mYear", date.getYear());
		f.setNumFurnituresPerDate(numPerDate);
		args.putInt("numPerDate", numPerDate);
		f.setArguments(args);
		return f;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
		Bundle bundle = getArguments();
		 if(bundle != null){
			 mDay = bundle.getInt("mDay");
			 mMonth = bundle.getInt("mMonth");
			 mYear = bundle.getInt("mYear");
			 mAllFurnitures = bundle.getParcelableArrayList("allFurnitures");
			 mNumPerDate = bundle.getInt("numPerDate");
		 }
		 mSelectedItems = new ArrayList<Integer>();
		 final boolean[] selectedTypes = new boolean[Furniture.countFurnituresArray(mAllFurnitures)];
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 builder.setTitle(getResources().getString(R.string.dialog_confirmar_furnitures)
				 +" "+mNumPerDate+" "+getResources().getString(R.string.dialog_confirmar_furnitures2));
		 Log.i("FurnitureSelectorDialFrag","Se crea el titulo del dialogo");
		 builder.setMultiChoiceItems(getFurnituresNames(), selectedTypes,
				 new DialogInterface.OnMultiChoiceClickListener() { 
		
			 
			 @Override
		     public void onClick(DialogInterface dialog, int which,
		         boolean isChecked) {
				 	if(isChecked){
				 		Log.i("FurnitureSelectorDialFrag.onclick",
				 				"Add "+which+" was clicked, add to the selected items array.");
				 		mSelectedItems.add(which);
				 		cont++;
					 }
				 	 else if (mSelectedItems.contains(which)) {
					 	Log.i("FurnitureSelectorDialFrag.onclick",
					 				"Remove "+which+" was clicked, delete to the selected items array.");
					 		mSelectedItems.add(which);
				 		mSelectedItems.remove(Integer.valueOf(which));
				 		cont--;
					 }
			}
		 });
		 builder.setPositiveButton(getResources().getString(R.string.dialog_confirmar_aceptar), 
				 new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Aun quedan item por seleccionar para dicha fecha.
						if(cont < mNumPerDate){
							Log.i("FurnitureSelectorDialFrag.PositiveButton",
									Furniture.countFurnituresArray(mFurnitureToRequest)+" is lower than mNumPerDate "+mNumPerDate);
							// Mensaje indicando que enseres faltan por confirmar
							Toast.makeText(getActivity(), 
									getResources().getString(R.string.dialog_confirmar_numeroNoValido2)
									+" "+(mNumPerDate-Furniture.countFurnituresArray(mFurnitureToRequest))
									+" "+getResources().getString(R.string.dialog_confirmar_numeroNoValido3), 
									Toast.LENGTH_LONG).show();
						}
						else if(cont > mNumPerDate){
							// Mensaje indicando que el numero de enseres seleccionado no es valido para esta fecha.
							Log.i("FurnitureSelectorDialFrag.PositiveButton",
									Furniture.countFurnituresArray(mFurnitureToRequest)
									+" is higher than mNumPerDate "+mNumPerDate);
							Toast.makeText(getActivity(), 
									getResources().getString(R.string.dialog_confirmar_numeroNoValido), 
									Toast.LENGTH_LONG).show();
						}
						else{
							Log.i("FurnitureSelectorDialFrag","Confirm appointment");
							confirmAppointments();
							Toast.makeText(getActivity(), 
									getResources().getString(R.string.message_finalize), 
									Toast.LENGTH_LONG).show();
							
						}
					}
			 
		 });
		 builder.setNegativeButton(getResources().getString(R.string.cancel),
				 new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {

             }
         });
		 return builder.create();
	}

	protected void confirmAppointments() {
		final Integer[] furnituresId = 
				Furniture.toIntegerArray(mAllFurnitures);
		// Obtain selected furnitures
		for(int id : mSelectedItems){
			Log.i("FurnitureSelectorDialFrag.confirmAppointments",
					"Se incluye item "+id);
			addFurnitureToRequest(furnituresId[id]);
		}
		Log.i("FurnitureSelectorDialFrag.confirmAppointments",
				"add "+Furniture.countFurnituresArray(mFurnitureToRequest)
				+" items to appointment for date "+mYear+"/"+mMonth+"/"+mDay);
		((ConfirmarFechaActivity) getActivity()).confirmAppointment(
				new LocalDate(mYear,mMonth,mDay),
				mFurnitureToRequest);
	}

	/**
	 * Incrementa en uno el numero de muebles seleccionados.
	 * @param id
	 */
	protected void addFurnitureToRequest(Integer id) {
		if(mFurnitureToRequest == null){
			throw new NullPointerException("mFurnitureToRequest is null in " +
					"FurnitureSelectorDialFrag.addFurnitureToRequest");
		}
		Furniture f = Furniture.findFurniture(id, mAllFurnitures);
		if(f == null){
			throw new IllegalArgumentException("Furniture with id "+id+" not exist" +
					" in addFurnitureToRequest method.");
		}
		Log.i("addFurnitureToRequest",
				"Add "+f.getName()+" to the request");
		mFurnitureToRequest = (ArrayList<Furniture>) 
				Furniture.incrementFurnitureInOne(f,mFurnitureToRequest);
	}
	
	private  CharSequence[] getFurnituresNames(){
		CharSequence[] furnituresNames = Furniture.toStringArray(mAllFurnitures);
		for(int i = 0 ; i < furnituresNames.length; i++){
			Log.i("getFurnituresNames","@string/item_"+furnituresNames[i]);
			int stringId = getResources()
					.getIdentifier("@string/item_"+furnituresNames[i],
					"string", getActivity().getPackageName());        
			Log.i("getFurnituresNames","id: "+stringId);
			furnituresNames[i] = getActivity().getString(stringId);
		}
		return furnituresNames;
	}

}

