package es.recicloid.ConfirmarFecha;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import es.recicloid.adapters.ListAdaptor;
import es.recicloid.models.Furniture;
import es.uca.recicloid.R;

/**
 * Fragmento que muestra los enseres que seran recogidos en una fecha dada.
 * @author Diego Rubio Abujas
 *
 */
public class FurnituresToConfirmInCollDateFragment extends ListFragment{
	public int mDay,mMonth,mYear;
	public ArrayList<Furniture> mFurnitures,mFurnitesToCollect;
	private ListView furnituresToConfirm;
	private TextView mTitle;
	
	public FurnituresToConfirmInCollDateFragment(){
		
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {		
		mDay = savedInstanceState.getInt("day");
		mMonth = savedInstanceState.getInt("month");
		mYear = savedInstanceState.getInt("year");
		mFurnitures = savedInstanceState
				.getParcelableArrayList("furnitures");
		View view = inflater.inflate(R.layout.furnitutes_list_toconfirm_fragment, 
				container);
		mTitle = (TextView) view.findViewById(R.id.descrAppointmentDate);
		mFurnitesToCollect = new ArrayList<Furniture>();
		furnituresToConfirm.setAdapter(new ListAdaptor(getActivity(),
				R.layout.furnitutes_list_toconfirm_fragment,mFurnitures){
		
					@Override
					public void onEntrada(Object entrada, View view) {
						TextView texto_superior_entrada = (TextView)
			            		view.findViewById(R.id.textViewTitleItemList); 
			            if (texto_superior_entrada != null){ 
			            	texto_superior_entrada.setText(((Furniture) entrada)
			            			.getName()); 
			            }	
					}
		});
		
		return view;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFurnitures = savedInstanceState
				.getParcelableArrayList("furnitures");
		setListAdapter(new ArrayAdapter<Furniture>(getActivity(),
                R.layout.furnitutes_list_toconfirm_fragment, 
                mFurnitures));
    }
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }
}
