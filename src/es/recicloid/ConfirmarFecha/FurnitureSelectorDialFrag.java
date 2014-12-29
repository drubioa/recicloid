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
	protected int cont = 0;
	private List<Integer> mSelectedItems;
	
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
			List<Furniture> allFurnitures){
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
			 mMonth = bundle.getInt("mMomht");
			 mYear = bundle.getInt("mYear");
			 mAllFurnitures = bundle.getParcelableArrayList("allFurnitures");
			 mNumPerDate = bundle.getInt("numPerDate");
		 }
		 mSelectedItems = new ArrayList<Integer>();
		 final String[] furnitures = Furniture.toStringArray(mAllFurnitures);
		 final boolean[] selectedTypes = new boolean[furnitures.length];
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 builder.setTitle(getResources().getString(R.string.dialog_confirmar_furnitures)
				 +mNumPerDate+getResources().getString(R.string.dialog_confirmar_furnitures2));
		 Log.i("FurnitureSelectorDialFrag","Se crea el titulo del dialogo");
		 builder.setMultiChoiceItems(furnitures, selectedTypes,
				 new DialogInterface.OnMultiChoiceClickListener() { 
		
			 
			 @Override
		     public void onClick(DialogInterface dialog, int which,
		         boolean isChecked) {
				 	if(isChecked){
				 		mSelectedItems.add(which);
						cont++;
					 }
				 	 else if (mSelectedItems.contains(which)) {
				 		mSelectedItems.remove(Integer.valueOf(which));
					 	cont--;
					 }
					 Log.i("OnMultiChoiceClickListener","Pending "+cont+" request to confirm in date "
					 			+mDay+"/"+mMonth+"/"+mYear);
			}
			 
		 });
		 builder.setPositiveButton(getResources().getString(R.string.dialog_confirmar_aceptar), 
				 new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(cont < mNumPerDate){
							Log.i("FurnitureSelectorDialFrag.PositiveButton",
									cont+" is lower than mNumPerDate "+mNumPerDate);
							// Mensaje indicando que enseres faltan por confirmar
							Toast.makeText(getActivity(), 
									getResources().getString(R.string.dialog_confirmar_numeroNoValido2)
									+" "+(mNumPerDate-cont)+" "+getResources().getString(R.string.dialog_confirmar_numeroNoValido3), 
									Toast.LENGTH_LONG).show();
						}
						else if(cont > mNumPerDate){
							// Mensaje indicando que el numero se enseres seleccionado no es valido para esta fecha.
							Log.i("FurnitureSelectorDialFrag.PositiveButton",
									cont+" is higher than mNumPerDate "+mNumPerDate);
							Toast.makeText(getActivity(), 
									getResources().getString(R.string.dialog_confirmar_numeroNoValido), 
									Toast.LENGTH_LONG).show();
						}
						else{
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
		for(int id : mSelectedItems){
			addFurnitureToRequest(furnituresId[id]);
		}
		((ConfirmarFechaActivity) getActivity()).confirmAppointment(new LocalDate(mYear,mMonth,mDay),
				mFurnitureToRequest);
	}

	protected void addFurnitureToRequest(Integer id) {
		if(mFurnitureToRequest == null){
			throw new NullPointerException("mFurnitureToRequest is null in " +
					"FurnitureSelectorDialFrag.addFurnitureToRequest");
		}
		Furniture f = Furniture.findFurniture(id, mFurnitureToRequest);
		if(f == null){
			throw new IllegalArgumentException("Furniture with id "+id+" not exist" +
					" in addFurnitureToRequest method.");
		}
		// Se incrmentan en los confirmados
		mFurnitureToRequest = (ArrayList<Furniture>) 
				incrementFurniture(f,mFurnitureToRequest);
	}
	
	/**
	 * Se incremente el numero de items indicados en uno, si no existe se agrega; si existe
	 * se incrementa en uno.
	 * @param furniture que se desea incluir o incrementar en caso de que ya este incluido.
	 * @param list listado actual
	 * @return una lista que incluye el intem incrementado en 1.
	 */
	private static List<Furniture> incrementFurniture(Furniture furniture,
			List<Furniture> list){
		boolean encontrado = false;
		for(Furniture f: list){
			if(f.equals(furniture)){
				encontrado = true;
				f.setCantidad(f.getCantidad() + 1 );
				break;
			}
		}
		if(!encontrado){
			list.add(furniture);
		}
		return list;
	}

}
