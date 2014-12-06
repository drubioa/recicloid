package es.recicloid.adapters;

import java.util.ArrayList;
import java.util.List;

import es.recicloid.clases.Furniture;
import es.uca.recicloid.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemsGridViewAdapter extends ArrayAdapter<Furniture>{
	private Context context;
	private int layoutResourceId;
	private List<Furniture> data = new ArrayList<Furniture>();
	
	public ItemsGridViewAdapter(Context context,int layoutResourceId, 
			List<Furniture> mCategory_bath) {
		super(context, layoutResourceId, mCategory_bath);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = mCategory_bath;
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
		int image = context.getResources().getIdentifier((item.getIdImg()), 
				"drawable", context.getPackageName());	
		holder.imageItem.setImageResource(image);
		if(item.getmNum() > 0){
			int text = context.getResources()
					.getIdentifier(item.getIdText(),"string", context.getPackageName());	
			holder.textTitle.setText(context.getText(text)+"\n("+item.getmNum()+")");
			holder.textTitle.setTypeface(null, Typeface.BOLD);
		}
		else{
			int text = context.getResources()
					.getIdentifier(item.getIdText(),"string", context.getPackageName());	
			holder.textTitle.setText(text);
			holder.textTitle.setTypeface(null, Typeface.NORMAL);
		}

		return row;
	}
	
	static class RecordHolder{
		ImageView imageItem;
		TextView textTitle;
	}
	
}