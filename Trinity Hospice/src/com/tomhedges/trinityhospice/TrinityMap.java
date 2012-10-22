package com.tomhedges.trinityhospice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

public class TrinityMap extends MapActivity {
	private JSONObject jObject;
	private String LocationData;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trinitymap);

        MapView mapView = (MapView) findViewById(R.id.mapmain);
        mapView.setBuiltInZoomControls(true);
        MapController mapController = mapView.getController();
        mapController.setCenter(new GeoPoint(51461442,-145274));
        mapController.setZoom(14);

        // create overlays array
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable marker = this.getResources().getDrawable(R.drawable.trinitylogohdpiclear);
        TrinityLocationsItemizedOverlay itemizedoverlay = new TrinityLocationsItemizedOverlay (marker, this);
        
        //LocationData = getString(R.string.TrinityLocation);
        
		HttpClient client = new  DefaultHttpClient();
		HttpGet get = new HttpGet("http://www.trinityhospice.org.uk/sites/default/files/file_attach/locations.txt");
	      
		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		try{
			LocationData = client.execute(get, responseHandler);
			Toast.makeText(TrinityMap.this, "Online Load OK", 1000000).show();
		}catch(Exception ex) {
			try {	
	        	FileInputStream openLocations = openFileInput("trinityhospicelocations.dat");
	        	InputStreamReader isr = new InputStreamReader(openLocations);
	            /* Prepare a char-Array that will
	            * hold the chars we read back in. */
	            char[] inputBuffer = new char[1024];
	            // Fill the Buffer with data from the file
	            isr.read(inputBuffer);
	            // Transform the chars to a String
	            LocationData = new String(inputBuffer);
	            isr.close();
	            
	            Toast.makeText(TrinityMap.this, "Unable to update Trinity Hospice locations via the internet.\nWill try to update again on next visit to this page." +LocationData, 1000000).show();
		    } catch (IOException e) {
		    	Toast.makeText(TrinityMap.this, "NO ONLINE. NO FILE. FAIL" + LocationData, 1000000).show();
		    	LocationData="{[\"test\":\"test\"]}";
		    }
		}
        
	    Toast.makeText(TrinityMap.this, "gfjccyjkUnable to update Trinity Hospice locations via the internet.\nWill try to update again on next visit to this page." +LocationData, 1000000).show();
		   
		
		
        try {
			jObject = new JSONObject(LocationData);
			JSONObject trinitylocationsObject = jObject.getJSONObject("trinitylocations");
			JSONArray trinitylocationsArray = trinitylocationsObject.getJSONArray("locations");

			for (int i = 0; i < trinitylocationsArray.length(); i++) {

			    //parse location details
			    String strName = trinitylocationsArray.getJSONObject(i).getString("name").toString();
			    String strAddress = trinitylocationsArray.getJSONObject(i).getString("address").toString();
			    Integer intLat = trinitylocationsArray.getJSONObject(i).getInt("lat");
			    Integer intLng = trinitylocationsArray.getJSONObject(i).getInt("lng");

			    GeoPoint point = new GeoPoint(intLat,intLng);
		        OverlayItem overlayitem = new OverlayItem(point, strName, strAddress);         
		        itemizedoverlay.addOverlay(overlayitem);
		    
			}
	        
		} catch (JSONException e) {
	        Toast.makeText(TrinityMap.this, "error displaying", 1000000).show();
		}

        // add overlay to map
        mapOverlays.add(itemizedoverlay); 
		
        try {
        	FileOutputStream saveLocations = openFileOutput("trinityhospicelocations.dat", Context.MODE_PRIVATE);
        	OutputStreamWriter osw = new OutputStreamWriter(saveLocations);
        	osw.write(LocationData);
        	osw.flush();
            osw.close();
            
            //file deleting
            //File dir = getFilesDir();
            //File file = new File(dir, "trinityhospicelocations.dat");
            //file.delete();
            //Toast.makeText(TrinityMap.this, "testing - file deleted", 1000000).show();
    	} catch (IOException e) {
	        Toast.makeText(TrinityMap.this, "error saving", 1000000).show();
		}

    }

    /**
     * Required method to indicate whether we display routes
     */
	@Override
	protected boolean isRouteDisplayed() { return false; }
	

	
}
	