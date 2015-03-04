package es.recicloid.activities.SolicitudRecogida;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import es.recicloid.models.CollectionPoint;
import es.recicloid.models.CollectionRequest;
import es.recicloid.models.Furniture;
import es.recicloid.utils.json.JsonToFileManagement;
import es.uca.recicloid.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

@ContentView(R.layout.activity_finalize)
public class FinalizeActivity extends RoboActivity {
	@InjectView(R.id.webViewTextFinalize ) WebView text;
	@InjectView(R.id.buttonEnd ) Button btn_end;
	public boolean savedInCalendar;
	public ArrayList<CollectionRequest> mRequests;
	public ArrayList<Intent> intents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null){
			savedInCalendar = false;
		}
		else{
			savedInCalendar = savedInstanceState.getBoolean("savedInCalendar");
			intents = savedInstanceState.getParcelableArrayList("intents");
			if(savedInCalendar && intents.size() > 0){
				Intent i = (Intent) intents.get(0).clone();
				intents.remove(0);
				startActivity(i);
			}
		}
		text.loadData(getString(R.string.descrip_confirmaton), "text/html", 
				"utf-8");
		btn_end.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();  
            }
        });
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(!savedInCalendar){
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.finalize, menu);
		    return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(!savedInCalendar){
		    // Handle item selection
		    switch (item.getItemId()) {
		        case R.id.calendar_date:{
		        	savedInCalendar = true;
		        	optionSaveInGCal();
		        	return true;
		        }
		        default:
		            return super.onOptionsItemSelected(item);
		    }
	    }
		else{
			return false;
		}
	}


	public Intent addCalendarEvent(CollectionRequest req ){
		// First get collections day and converting to Calendar
		Date date = req.getFch_collection().toDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "Recogida de muebles y enseres");
        String furnitures = new String();
        for(Furniture f: req.getFurnitures()){
        	int stringId = getResources().getIdentifier("@string/item_"+f.getName(),
					"string", this.getPackageName());        
        	furnitures += this.getString(stringId)+"\n";
        }
        intent.putExtra("description", 
        		"Para dicha fecha se recogerán los siguientes enseres:\n"+furnitures);
        CollectionPoint point = loadCollectionPointFromJSONFile();
    	intent.putExtra("eventLocation", point.getDirection()+" Puerto Real");
    	Log.e("FinalizeActivity.addCalendarEvent",
				"addCalendarEvent"+req.getFch_collection().toString());
    	return intent;
    }
	
	public void optionSaveInGCal(){
    	Bundle b = this.getIntent().getExtras();
    	mRequests = 
    			b.getParcelableArrayList("collectionRequests");
    	if(mRequests != null){
    		if(mRequests.size() > 0){
    			intents = new ArrayList<Intent>();
	    		for(CollectionRequest req: mRequests){
		        	if(req != null){
		        		if(req.checkCorrectRequest()){
			        		Log.i("FinalizeActivity.options",
			        				"Solicitud en formato correcto");
			        		intents.add(addCalendarEvent(req));
			        		Log.i("FinalizeActivity.options",
			        				"Add date to local calendar");
		        		}
		        	}
	        		else{
	        			Log.e("FinalizeActivity.options",
		        				"Solicitud de recogida en formato no valido");
	       				Log.e("Furnitures is null",
	       						String.valueOf(req.getFurnitures() == null));
	       				Log.e("Furnitures is void",
	       						String.valueOf(req.getFurnitures().size() == 0));
	       				Log.e("getFch_collection is null",
	       						String.valueOf(req.getFch_collection() == null));
	       			}
	    		}
	    		if(savedInCalendar && intents.size() > 0){
					Intent i = (Intent) intents.get(0).clone();
					intents.remove(0);
			    	Log.e("FinalizeActivity.optionSaveInGCal",
							"Se incluye una solicitud en el calendario");
					startActivity(i);
				}
    	}
       	else{
       			Log.e("FinalizeActivity.options",
       					"Solicitud de recogida nula");
      	}
   	}
   	else{
   		Log.w("FinalizeActivity","No fue posible cargar anteriores solicitudes" +
   				"de recogida, posible error en el Parcelable");
    	}		
	}
	
	private CollectionPoint loadCollectionPointFromJSONFile(){
		JsonToFileManagement jsonFile = new JsonToFileManagement(this);
		jsonFile.changeFileName("collection-point.json");
		try {
			return jsonFile.loadCollectionPointFromLocalFile();
		} catch (IOException e) {
			Log.e("ConfirmarFechaActivity.obtainsProvisionalAppointments",
					"Cannot load collectionPoint"+
					e.toString());
			throw new RuntimeException();
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(savedInCalendar){
			invalidateOptionsMenu();
			if(intents.size() > 0){
				Intent i = (Intent) intents.get(0).clone();
				intents.remove(0);
		    	Log.e("FinalizeActivity.optionSaveInGCal",
						"Se incluye otra solicitud en el calendario");
		    	startActivity(i);
			}
		}	
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.w("ConfirmadFechaActivity","onSaveInstanceState");
		outState.putBoolean("savedInCalendar", savedInCalendar);
		outState.putParcelableArrayList("intents", intents);
	}

}
