package es.recicloid.activities.SolicitudRecogida;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import es.recicloid.adapters.ItemsGridViewAdapter;
import es.recicloid.dialogs.DialogAlert;
import es.recicloid.dialogs.DialogMultiplesFurnitures;
import es.recicloid.models.Furniture;
import es.recicloid.parser.ItemsParser;
import es.recicloid.utils.json.JsonToFileManagement;
import es.uca.recicloid.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

/**
 * @author Diego Rubio Abujas
 * @version 1.0
 * Actividad que gestiona la seleccion de los enseres que se desean
 * entregar para su posterior recogida para reciclar. Se mostrar??n
 * los enseres organizados por categor??as.
 */
@ContentView(R.layout.activity_solicitud_enseres)
public class SolicitudEnseresActivity extends RoboFragmentActivity {
	private static final int CAT_BATHROOM = 0;
	private static final int CAT_KITCHEN = 1;
	private static final int CAT_BEDROOM = 2;
	private static final int CAT_OUTSIDE = 3;
	private static final int CAT_LIVING = 4;
	private static final int CAT_GENERAL = 5;
	private static final int MAX_PER_USER = 10;
	private static final int MAX_FURNITURES_PER_DAY = 4;
    @InjectView(R.id.GridView_items) private GridView mGridViewCategorY;
    @InjectView(R.id.buttonContinuar) public Button btn_continue;
    private Spinner mSpinnerCategories;
    private ArrayList<Furniture> 
    	mCategory_bath,
    	mCategory_kitchen,
    	mCategory_bedroom,
    	mCategory_outside,
    	mCategory_living,
    	mCategory_general;
    public ArrayList<Furniture> furnituresToRecic;
    private ItemsGridViewAdapter mGridViewAdapter;
    private int mOpcionActual;
    private List<String> OpcionesSpinner;
    private boolean botonActivo,mensaje4items;
	private final JsonToFileManagement jsonToFile = 
			new JsonToFileManagement(this,"furnitures.json");   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		OpcionesSpinner = Arrays.asList(getResources()
				.getStringArray(R.array.categorias_enseresa));
		
		// Creacion del Spinner de categorias
		createSpinnerCategorias();
		Log.i("SolicitudEnseresActivity", "The Spinner Menu was created.");	
		
		if(savedInstanceState == null){
			furnituresToRecic = new ArrayList<Furniture>();
			prepareCategories();
			botonActivo = false;
			mensaje4items = false;
			mOpcionActual = CAT_BATHROOM;
			showInitialMessage();
		}
		else{
			furnituresToRecic = savedInstanceState.getParcelableArrayList("itemsSelected");
			mCategory_bath = savedInstanceState.getParcelableArrayList("bathroom");
			mCategory_kitchen = savedInstanceState.getParcelableArrayList("kitchen");
			mCategory_bedroom = savedInstanceState.getParcelableArrayList("bedroom");
			mCategory_outside = savedInstanceState.getParcelableArrayList("outside");
			mCategory_living = savedInstanceState.getParcelableArrayList("living"); 
			mCategory_general = savedInstanceState.getParcelableArrayList("general");  
			mOpcionActual = savedInstanceState.getInt("CategoriaElegida");
			botonActivo = savedInstanceState.getBoolean("botonContinuar");
			mensaje4items = savedInstanceState.getBoolean("mensaje4items");
		}
		mGridViewAdapter = new ItemsGridViewAdapter(this, R.layout.grid_item, mCategory_bath);
		mGridViewCategorY.setAdapter(mGridViewAdapter);		
		Log.i("SolicitudEnseresActivity", "The GridView was created.");

		// Boton continuar
		if(!botonActivo){
			btn_continue.setEnabled(false);
		}
		else{
			btn_continue.setEnabled(true);
		}
		Log.i("SolicitudEnseresActivity", "The Boton to conitnue was created.");
		
		// Listener para los GridView
		mGridViewCategorY.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
				if(Furniture.countFurnituresArray(furnituresToRecic) <= MAX_PER_USER){
					Furniture item = (Furniture) parent.getItemAtPosition (position);
					if(item.getCantidad() == 0){
						furnituresToRecic.add(item);
						addFunritureToCollection(item.getName());
					}
					else{
						DialogFragment newFragment = DialogMultiplesFurnitures.newInstance(
								item.getName());
					    newFragment.show(getSupportFragmentManager(), "addMoreItems");
					}
				}
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
		
		// Listener que atiende cada uno de los onClick del bototon 
		// continuar con la solicitud
		btn_continue.setOnClickListener(new View.OnClickListener() {
            
			public void onClick(View v) {
				Intent intent = new Intent(SolicitudEnseresActivity.this,
						UbicacionRecogidaActivity.class);
				try {
					jsonToFile.saveFurnituresInJsonFile(furnituresToRecic);
					startActivity(intent);   
				} catch (IOException e) {
					Log.e("SolicitudEnseresActivity", e.toString());
				} 
				
            }
        });

	}
	
	private void showInitialMessage() {
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", R.string.dialog_info_solicitud_enseres);
		args.putInt("description",R.string.dialog_descr_info_solicitud_enseres);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, "tagMensajeInicialSolicitudEnseres");	
	}

	private void prepareCategories() {
		try {
			mCategory_bath = prepareCategory(CAT_BATHROOM);
			mCategory_kitchen = prepareCategory(CAT_KITCHEN);
			mCategory_bedroom = prepareCategory(CAT_BEDROOM);
			mCategory_outside = prepareCategory(CAT_OUTSIDE);
			mCategory_living = prepareCategory(CAT_LIVING);
			mCategory_general = prepareCategory(CAT_GENERAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param category Number extracted by the string array resource.
	 * @return ArrayList with all the elemtns of a categoryNumber
	 * @throws Exception 
	 */
	private ArrayList<Furniture> prepareCategory(int categoryNumber) throws Exception{
		String xmlFileName;
		switch(categoryNumber){
		case CAT_BATHROOM:
			xmlFileName = "items-bathroom.xml";
			break;
		case CAT_KITCHEN:
			xmlFileName = "items-kitchen.xml";
			break;
		case CAT_BEDROOM:
			xmlFileName = "items-bedroom.xml";
			break;
		case CAT_OUTSIDE:
			xmlFileName = "items-outside.xml";
			break;
		case CAT_LIVING:
			xmlFileName = "items-living.xml";
			break;
		case CAT_GENERAL:
			xmlFileName = "items-general.xml";
			break;
		default:
			throw new Exception("Invalid category introduced");
	}
		InputStream in = getAssets().open(xmlFileName);
		Log.i("SolicitudEnseresActivity", "xml file '"+xmlFileName+"' opened.");
		ItemsParser  parser = new ItemsParser(categoryNumber);
		parser.filename = xmlFileName;
		ArrayList<Furniture> furnitures =  (ArrayList<Furniture>) parser.parse(in);
		for(Furniture f : furnitures){
			Log.i("SolicitudEnseresActivity","Se incluye "+f.toString());
		}
		Log.i("SolicitudEnseresActivity", "xml file '"+xmlFileName+"' were parser.");
		in.close();
		Log.i("SolicitudEnseresActivity", "closed conection with '"+xmlFileName+"'.");
		return furnitures;
	}

	private void cambiarGridView(String opcionElegida,AdapterView<?> parent){
		switch(getCategoryValueOf(opcionElegida)){
			case CAT_BATHROOM:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(),
						R.layout.grid_item, mCategory_bath);
				mGridViewCategorY.setAdapter(mGridViewAdapter);
				break;
			}
			case CAT_KITCHEN:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(),
						R.layout.grid_item, mCategory_kitchen);
				mGridViewCategorY.setAdapter(mGridViewAdapter);				
				break;
			}
			case CAT_BEDROOM:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(),
						R.layout.grid_item, mCategory_bedroom);
				mGridViewCategorY.setAdapter(mGridViewAdapter);		
				break;
			}
			case CAT_OUTSIDE:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(),
						R.layout.grid_item, mCategory_outside);
				mGridViewCategorY.setAdapter(mGridViewAdapter);		
				break;
			}
			case CAT_LIVING:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(),
						R.layout.grid_item, mCategory_living);
				mGridViewCategorY.setAdapter(mGridViewAdapter);		
				break;				
			}
			case CAT_GENERAL:{
				mGridViewAdapter = new ItemsGridViewAdapter(parent.getContext(),
						R.layout.grid_item, mCategory_general);
				mGridViewCategorY.setAdapter(mGridViewAdapter);		
				break;				
			}
		}
	}
	
	private void createSpinnerCategorias(){
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(this,
						R.array.categorias_enseresa,android.R.layout
						.simple_spinner_item);
		mSpinnerCategories = (Spinner) findViewById(R.id.spinner_items);
		adapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		mSpinnerCategories.setAdapter(adapter);		
	}
	
	/**
	 * @param categoryName
	 * @return integer value of category name. Used to identify the array
	 * string resource
	 */
	private int getCategoryValueOf(String categoryName){
		for(int i = 0;i < OpcionesSpinner.size();i++){
			if(categoryName.equals(OpcionesSpinner.get(i))){
				return i;
			}
		}
		return 0;
	}
	
	public void addFunritureToCollection(String item){
		Log.i("SolicitudEnseresActiity ",item+" incrementado en uno");
		for(int i = 0;i < furnituresToRecic.size();i++){
			if(furnituresToRecic.get(i).getName() == item){
				furnituresToRecic.get(i).setmAumentarEnUno();
			}
		}
		if(furnituresToRecic.size() > 0){
			btn_continue.setEnabled(true);
			botonActivo = true;					
		}
		if(!mensaje4items && 
				Furniture.countFurnituresArray(furnituresToRecic) 
				> MAX_FURNITURES_PER_DAY){
			showMessage4Items();
		}
		mGridViewCategorY.invalidateViews();
	}
	
	/**
	 * Remueve de las muebles y enseres pendientes de recoger el item
	 * @param item
	 */
	public void removeFurnitureToCollecton(String item){
		Log.i("SolicitudEnseresActiity",item+" reducido a 0");
		for(int i = 0;i < furnituresToRecic.size();i++){
			if(furnituresToRecic.get(i).getName()  == item){
				furnituresToRecic.get(i).setCantidad(0);
				mGridViewCategorY.invalidateViews();
				furnituresToRecic.remove(i);
			}			
		}
		if(furnituresToRecic.size() == 0){
			btn_continue.setEnabled(false);
			botonActivo = false;					
		}
	}
	
	/**
	 * If 4 items were chosen, the App must show a message informing
	 *  the user of the use  conditions.
	 */
	private void showMessage4Items() {
		FragmentManager fm = getSupportFragmentManager();
		Bundle args = new Bundle();
		args.putInt("title", R.string.dialog_title_4items);
		args.putInt("description",R.string.dialog_descr_4items);
		DialogAlert newFragment = DialogAlert.newInstance(args);
		newFragment.show(fm, "tagAviso4Items");
        mensaje4items = true;
	}			        
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
	   outState.putParcelableArrayList("itemsSelected", furnituresToRecic);
       outState.putParcelableArrayList("bathroom", mCategory_bath);
       outState.putParcelableArrayList("kitchen", mCategory_kitchen);
       outState.putParcelableArrayList("bedroom", mCategory_bedroom);
       outState.putParcelableArrayList("outside", mCategory_outside);
       outState.putParcelableArrayList("living", mCategory_living);
       outState.putParcelableArrayList("general", mCategory_general);
       outState.putInt("CategoriaElegida", mOpcionActual);
       outState.putBoolean("botonContinuar", botonActivo);
       outState.putBoolean("mensaje4items", mensaje4items);
       super.onSaveInstanceState(outState);
	}	
}
