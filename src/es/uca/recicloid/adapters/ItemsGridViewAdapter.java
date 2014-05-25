package es.uca.recicloid.adapters;

import java.util.ArrayList;

import es.uca.recicloid.R;
import es.uca.recicloid.clases.Furniture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemsGridViewAdapter extends ArrayAdapter<Furniture>{
	private Context context;
	private int layoutResourceId;
	private ArrayList<Furniture> data = new ArrayList<Furniture>();
	
	public ItemsGridViewAdapter(Context context,int layoutResourceId, ArrayList<Furniture> objects) {
		super(context, layoutResourceId, objects);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		RecordHolder holder;
		
		if( row == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new RecordHolder();
			holder.imageItem = (ImageView) row.findViewById(R.id.imageView_item);
			holder.textTitle = (TextView) row.findViewById(R.id.TextView_item);
		}
		else{
			holder = (RecordHolder) row.getTag();
		}
		row.setTag(holder);
		Furniture item = data.get(position);
		holder.imageItem.setImageResource(item.getIdImg());
		if(item.getmNum() > 0){
			holder.textTitle.setText(getContext().getString(item.getIdText())+"("+item.getmNum()+")");
		}
		else{
			holder.textTitle.setText(item.getIdText());
		}

		return row;
	}
	
	static class RecordHolder{
		ImageView imageItem;
		TextView textTitle;
	}
	
}