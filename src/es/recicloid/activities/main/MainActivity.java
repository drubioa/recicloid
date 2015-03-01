package es.recicloid.activities.main;

import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


import es.recicloid.activities.Historial.HistorialActivity;
import es.recicloid.activities.SolicitudRecogida.SolicitudEnseresActivity;
import es.recicloid.activities.inforeciclaje.InformacionReciclajeActivity;
import es.recicloid.adapters.ListAdaptor;
import es.recicloid.adapters.TitleWithImg;
import es.uca.recicloid.R;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity implements MainView{

	@InjectView(R.id.listViewMainMenu) private ListView mainMenu;
	private ArrayList<TitleWithImg> options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = getOptiones();
		setMainMenuAdapter();
		setOnMainMenuItemClickListener();
    }		
	
	public void setOnMainMenuItemClickListener() {
		
		mainMenu.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> pariente, View view, 
					int posicion, long id) {
				TitleWithImg elegido = (TitleWithImg) 
						pariente.getItemAtPosition(posicion); 
				Intent intent = null;
				if(elegido.getTitulo().equals(getResources()
						.getString(R.string.title_solicitud))){
					intent = new Intent(MainActivity
							.this,SolicitudEnseresActivity.class);
				}
				else if(elegido.getTitulo().equals(getResources()
						.getString(R.string.title_historial))){
					intent = new Intent(MainActivity
							.this,HistorialActivity.class);
				}
				else if(elegido.getTitulo().equals(getResources()
						.getString(R.string.title_contact))){
					intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:956474448"));
				}
				else{
					intent = new Intent(MainActivity
							.this,InformacionReciclajeActivity.class);				
				}
				startActivity(intent);
			}
        });
	}

	public void setMainMenuAdapter(){
			mainMenu.setAdapter(new ListAdaptor(this,
					R.layout.list_item_image,options){
				
				@Override
				public void onEntrada(Object entrada,View view){
	            ImageView imagen_entrada = (ImageView) 
	            		view.findViewById(R.id.imageViewItemList); 
	            if (imagen_entrada != null){
	            	imagen_entrada
	            	.setImageResource(((TitleWithImg) entrada).getImageId());
	        	}		
	            TextView texto_superior_entrada = (TextView)
	            		view.findViewById(R.id.textViewTitleItemList); 
	            if (texto_superior_entrada != null){ 
	            	texto_superior_entrada.setText(((TitleWithImg) entrada)
	            			.getTitulo()); 
	            }
	            TextView texto_inferior_entrada = (TextView)
	            		view.findViewById(R.id.textViewDescriptionItemList); 
	            if (texto_inferior_entrada != null){
	            	texto_inferior_entrada.setText(((TitleWithImg) entrada)
	            			.getSubtitulo()); 
	            }
			}
		});
	}
	
	public ArrayList<TitleWithImg> getOptiones() {
		ArrayList<TitleWithImg> options = new ArrayList<TitleWithImg>();
		// Solicitar Recogida.
		options.add(new TitleWithImg(R.drawable.ic_truck,getResources()
				.getString(R.string.title_solicitud),getResources()
				.getString(R.string.descr_solicitud)));
		// Informacion de Reciclaje.
		options.add(new TitleWithImg(R.drawable.ic_papelera,getResources()
				.getString(R.string.title_info_reciclaje),getResources()
				.getString(R.string.descr_info_reciclaje)));		
		// Consultar Historial.
		options.add(new TitleWithImg(R.drawable.ic_historial,getResources()
				.getString(R.string.title_historial),getResources()
				.getString(R.string.descr_historial)));
		// Concact with us.
		options.add(new TitleWithImg(R.drawable.ic_telephone,getResources()
				.getString(R.string.title_contact),getResources()
				.getString(R.string.descr_contact)));
		return options;
	}


}
