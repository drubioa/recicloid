package es.uca.recicloid.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.uca.recicloid.R;
import es.uca.recicloid.adapters.ItemsGridViewAdapter;
import es.uca.recicloid.clases.Furniture;
import es.uca.recicloid.dialogs.DialogAlert;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

/**
 * 
 * @author Diego Rubio Abujas
 * @version 1.0
 * Actividad que gestiona la seleccion de los enseres que se desean
 * entregar para su posterior recogida para reciclar. Se mostrarán
 * los enseres organizados por categorías.
 */
public class SolicitudEnseresActivity extends FragmentActivity {
	private static final int CAT_BATHROOM = 0;
	private static final int CAT_KITCHEN = 1;
	private static final int CAT_BEDROOM = 2;
	
    private Spinner mSpinnerCategories;
    private ArrayList<Furniture> mCategory_bath,mCategory_kitchen,mCategory_bedroom;
    private ArrayList<Furniture> furnituresToRecic;
    private ItemsGridViewAdapter mGridViewAdapter;
    private GridView mGridViewCategorY;
    private int mOpcionActual;
    private List<String> OpcionesSpinner;
    private boolean botonActivo,mensaje4items;
    private Button btn_continue;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solicitud_enseres);

		OpcionesSpinner = Arrays.asList(getResources().getStringArray(R.array.categorias_enseresa));
		
		// Creación del Spinner de categorias
		createSpinnerCategorias();
		
		// Creacion de los GridViews
		mGridViewCategorY = (GridView) findViewById(R.id.GridView_items);
				
		if(savedInstanceState == null){
			furnituresToRecic = new ArrayList<Furniture>();
			mCategory_bath = prepareCategory(CAT_BATHROOM);
			mCategory_kitchen = prepareCategory(CAT_KITCHEN);
			mCategory_bedroom = prepareCategory(CAT_BEDROOM);
			// Inicialmente se escoge la opcion baño por defecto
			mOpcionActual = CAT_BATHROOM; 
			botonActivo = false;
			mensaje4items = false;
		}
		else{
			furnituresToRecic = savedInstanceState.getParcelableArrayList("itemsSelected");
			mCategory_bath = savedInstanceState.getParcelableArrayList("bathroom");
			mCategory_kitchen = savedInstanceState.getParcelableArrayList("kitchen");
			mCategory_bedroom = savedInstanceState.getParcelableArrayList("bedroom");
			mOpcionActual = savedInstanceState.getInt("CategoriaElegida");
			botonActivo = savedInstanceState.getBoolean("botonContinuar");
			mensaje4items = savedInstanceState.getBoolean("mensaje4items");
		}
		mGridViewAdapter = new ItemsGridViewAdapter(this, R.layout.grid_item, mCategory_bath);

		
     	// Adaptar GridView
		mGridViewCategorY.setAdapter(mGridViewAdapter);		
		
		// Boton continuar
		btn_continue = (Button) findViewById (R.id.buttonContinuar);
		
		if(!botonActivo){
			btn_continue.setEnabled(false);
		}
		else{
			btn_continue.setEnabled(true);
		}
		
		// Listener para los GridView
		mGridViewCategorY.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
				Furniture item = (Furniture) parent.getItemAtPosition (position);
				if(item.getmNum() == 0){
					furnituresToRecic.add(item);
				}
				item.setmAumentarEnUno();
				if(countFurnitures(furnituresToRecic) == 4 && !mensaje4items){
					FragmentManager fm = getSupportFragmentManager();
					Bundle args = new Bundle();
					args.putInt("title", R.string.dialog_title_4items);
					args.putInt("description",R.string.dialog_descr_4items);
					DialogAlert newFragment = DialogAlert.newInstance(args);
					newFragment.show(fm, "tagAviso4Items");
			        mensaje4items = true;
				}
				
				if(!botonActivo){
					btn_continue.setEnabled(true);
					botonActivo = true;
				}
				mGridViewCategorY.invalidateViews();
			}			
		});
	
		// Listener que atiende cada uno de los onClicks del Spinner
		mSpinnerCategories.setOnItemSelectedListener(
				new AdapterView.OnItemSelectedListener(){		
					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int pos, long id) {
							String opcionElegida = parent.getItemAtPosition(pos).toString();							
							cambiarGridView(opcionElegida,parent);
							mOpcionActual = getCategoryValueOf(opcionElegida);
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// Nothing
					}
					
		});
		
		// Listener que atiende cada uno de los onClick del bototon continuar con la solicitud
		btn_continue.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(SolicitudEnseresActivity.this,UbicacionRecogidaActivity.class);
				intent.putExtra("itemsSelected", furnituresToRecic);
				startActivity(intent);   
            }
        });

	}
	
	private void cambiarGridView(String opcionElegida,AdapterView<?> parent){
		switch(getCategoryValueOf(opcionElegida)){
			case CAT_BATHROOM:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(), R.layout.grid_item, mCategory_bath);
				mGridViewCategorY.setAdapter(mGridViewAdapter);
				break;
			}
			case CAT_KITCHEN:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(), R.layout.grid_item, mCategory_kitchen);
				mGridViewCategorY.setAdapter(mGridViewAdapter);				
				break;
			}
			case CAT_BEDROOM:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(), R.layout.grid_item, mCategory_bedroom);
				mGridViewCategorY.setAdapter(mGridViewAdapter);		
				break;
			}
		}
	}
	
	private void createSpinnerCategorias(){
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(this,
						R.array.categorias_enseresa,android.R.layout.simple_spinner_item);
		mSpinnerCategories = (Spinner) findViewById(R.id.spinner_items);
		adapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		mSpinnerCategories.setAdapter(adapter);		
	}
	
	/**
	 * 
	 * @param category Number extracted by the string array resource.
	 * @return ArrayList with all the elemtns of a categoryNumber
 	 */
	private ArrayList<Furniture> prepareCategory(int categoryNumber){
		ArrayList<Furniture> categoria = new ArrayList<Furniture>();
		switch(categoryNumber){
			case CAT_BATHROOM:{
				categoria.add(new Furniture("bathtub",categoryNumber,R.string.item_bathtub,R.drawable.tuck));
				categoria.add(new Furniture("wc",categoryNumber,R.string.item_wc,R.drawable.tuck));				
				break;
			}
			case CAT_KITCHEN:{
				categoria.add(new Furniture("desk",categoryNumber,R.string.item_desk,R.drawable.tuck));
				categoria.add(new Furniture("bed",categoryNumber,R.string.item_bed,R.drawable.tuck));
				break;
			}
			case CAT_BEDROOM:{
				categoria.add(new Furniture("sink",categoryNumber,R.string.item_sink,R.drawable.tuck));
				break;
			}
		}
		return categoria;
		
	}
	
	/**
	 * 
	 * @param categoryName
	 * @return integer value of category name. Used to identify the array string resource
	 */
	private int getCategoryValueOf(String categoryName){
		for(int i = 0;i < OpcionesSpinner.size();i++){
			if(categoryName.equals(OpcionesSpinner.get(i))){
				return i;
			}
		}
		return 0;
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
	   outState.putParcelableArrayList("itemsSelected", furnituresToRecic);
       outState.putParcelableArrayList("bathroom", mCategory_bath);
       outState.putParcelableArrayList("kitchen", mCategory_kitchen);
       outState.putParcelableArrayList("bedroom", mCategory_bedroom);
       outState.putInt("CategoriaElegida", mOpcionActual);
       outState.putBoolean("botonContinuar", botonActivo);
       outState.putBoolean("mensaje4items", mensaje4items);
       super.onSaveInstanceState(outState);
	}	
	
	private int countFurnitures(ArrayList<Furniture> furnitures) {
		int total = 0;
		for(int i = 0;i < furnitures.size();i++){
			total += furnitures.get(i).getmNum();
		}
		return total;
	}
}
