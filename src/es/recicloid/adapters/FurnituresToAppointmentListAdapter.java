package es.recicloid.adapters;

import java.util.ArrayList;
import java.util.List;

import es.recicloid.clases.Furniture;
import android.content.Context;
import android.widget.ArrayAdapter;

public class FurnituresToAppointmentListAdapter extends ArrayAdapter<Furniture>{
	private ArrayList<Furniture> mFurnituresToRecic;
	
	public FurnituresToAppointmentListAdapter(Context context, int resource,
			List<Furniture> objects) {
		super(context, resource, objects);
		mFurnituresToRecic = new ArrayList<Furniture>();
		mFurnituresToRecic.addAll(objects);
	}

}
