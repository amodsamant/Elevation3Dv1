package com.example.android.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.opengl.utils.AsyncResponse;
import com.example.android.opengl.utils.PlacesAutoCompleteAdapter;

public class MainActivity extends Activity implements AsyncResponse {

	static double latitude1;
	static double longitude1;
	static double latitude2;
	static double longitude2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Typeface myTypeface = Typeface.createFromAsset(
                this.getAssets(),
                "Roboto-Light.ttf");//RobotoCondensed-Light.ttf
        TextView textViewStat1 = (TextView)findViewById(R.id.textView1);
        textViewStat1.setTypeface(myTypeface);
        
        TextView textViewStat2 = (TextView)findViewById(R.id.textView2);
        textViewStat2.setTypeface(myTypeface);
        
        AutoCompleteTextView autoCompView1 = (AutoCompleteTextView) findViewById(R.id.autoLocationA);
        autoCompView1.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        //autoCompView1.setOnItemClickListener(this);
        autoCompView1.setTypeface(myTypeface);
        
        AutoCompleteTextView autoCompView2 = (AutoCompleteTextView) findViewById(R.id.autoLocationB);
        autoCompView2.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        autoCompView2.setTypeface(myTypeface);
        
        
  //      TextView coordinatesView = (TextView)findViewById(R.id.coordinatesView);
  //      coordinatesView.setTypeface(myTypeface);
        
   /*     GeoCodingUtils address = new GeoCodingUtils();
        address.getLatLongFromAddress("waltham");
        coordinatesView.setText("Latitude:  "+ address.getLatitude() 
        		+" \n" + "Longitude: "+ address.getLongitude());
     */
        Button getCoordinates = (Button)findViewById(R.id.getCoordinates);
        getCoordinates.setTypeface(myTypeface);
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void processFinish(String output){
		//this you will received result fired from async class of onPostExecute(result) method.
	}
	
	public void onClick2D(View v) {
		Toast.makeText(this, "2D Chart", Toast.LENGTH_SHORT).show();
		
		Intent openGL2DIntent = new Intent(this,Graph2D.class);
		startActivity(openGL2DIntent);
		
	}
	
	public void onClick3D(View v) {
		Toast.makeText(this, "3D OpenGL", Toast.LENGTH_SHORT).show();
		
		Intent openGL3DIntent = new Intent(this,Elevation3Dv1.class);
		startActivity(openGL3DIntent);
		
	}
	
	public void onClickGetCoordinates(View v) throws IOException {
		
		JsInterface jsInterface = new JsInterface();
		//TextView elevationData = (TextView)findViewById(R.id.coordinatesView);
		//Toast.makeText(this, "Clicked get Coordinates", Toast.LENGTH_SHORT).show();
		
		TextView coordinatesView = (TextView)findViewById(R.id.coordinatesView);
       
		String locationName1 = "1601 Trapelo Road Waltham";
		String locationName2 = "36 Trapelo Road Waltham";
		Geocoder gc = new Geocoder(this);
		List<Address> list = gc.getFromLocationName(locationName1, 1);
		Address add1 = list.get(0);
		String locality1 = add1.getLocality();
		//Toast.makeText(this, locality1, Toast.LENGTH_LONG).show();
		
		latitude1 = add1.getLatitude();
		longitude1 = add1.getLongitude();
		
		list = gc.getFromLocationName(locationName2, 1);
		Address add2 = list.get(0);
		String locality2 = add2.getLocality();
		//Toast.makeText(this, locality2, Toast.LENGTH_LONG).show();
		
		latitude2 = add2.getLatitude();
		longitude2 = add2.getLongitude();
		
		double elev = getElevationFromGoogleMaps(latitude1,longitude1);
		
		
/*		coordinatesView.setText("Latitude:  "+ latitude1+ " \n" + "Longitude: "+ longitude1 +
				"Latitude:  "+ latitude2+ " \n" + "Longitude: "+ longitude2 + "\n" +
				"Elevation: "+ elev);
  */      
		coordinatesView.setText("Elevation: "+ elev);
       WebView webView = (WebView)findViewById(R.id.webView1);
		
       WebSettings settings = webView.getSettings();
       settings.setJavaScriptEnabled(true);
       
       webView.addJavascriptInterface(jsInterface, "android");
       //webView.loadUrl("javascript:setLocations(\""+latitude1+"\")");
       webView.loadUrl("file:///android_asset/ElevationJS.html");
       
       webView.loadUrl("javascript:setLat(" + 10 + ");");
	}
		
	

	public double getElevationFromGoogleMaps(double latitude,double longitude) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	       double result = Double.NaN;
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpContext localContext = new BasicHttpContext();
	        String url = "http://maps.googleapis.com/maps/api/elevation/"
	                + "xml?locations=" + String.valueOf(latitude)
	                + "," + String.valueOf(longitude)
	                + "&sensor=false";
	        HttpGet httpGet = new HttpGet(url);
	        try {
	            HttpResponse response = httpClient.execute(httpGet, localContext);
	            HttpEntity entity = response.getEntity();
	            if (entity != null) {
	                InputStream instream = entity.getContent();
	                int r = -1;
	                StringBuffer respStr = new StringBuffer();
	                while ((r = instream.read()) != -1)
	                    respStr.append((char) r);
	                String tagOpen = "<elevation>";
	                String tagClose = "</elevation>";
	                if (respStr.indexOf(tagOpen) != -1) {
	                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
	                    int end = respStr.indexOf(tagClose);
	                    String value = respStr.substring(start, end);
	                    result = Double.parseDouble(value);
	                    //result = (double)(Double.parseDouble(value)*3.2808399); // convert from meters to feet
	                }
	                instream.close();
	            }
	        } catch (ClientProtocolException e) {} 
	        catch (IOException e) {}

	        return result;
	   
		}
	//javascript interface
    public class JsInterface{
    	@JavascriptInterface
    	//js example: android.log('my message');
    	public void log(String msg){
    		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    		//Elevation3Dv1.triangleCoords;
    		String[] msgSplit = msg.split(",");
    		for(int i=0;i<msgSplit.length;i++) {
    			Elevation3Dv1.triangleCoords[3*i+1]=Float.parseFloat(msgSplit[i])/100;
    		}
    	
    	}
    	
    	public double getLocation() 
    	{	
    		return 10.0;
    		//Toast.makeText(getApplicationContext(), "AMOD", Toast.LENGTH_LONG).show();
    /*		int num=5;
    		if (num==1) 
    			{return MainActivity.latitude1;}
    		if (num==2) 
    		{return MainActivity.longitude1;}
    		if (num==3) 
    		{return MainActivity.latitude2;}
	    	if (num==4) 
			{return MainActivity.longitude2;}
	    	else
	    		return MainActivity.latitude1;
    */	}
    }
}
