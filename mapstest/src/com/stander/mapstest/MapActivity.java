package com.stander.mapstest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener
{

	private final static int
    CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	LocationClient mLocationClient;
	Location mLocation;
	LocationRequest mLocationRequest;
	
	private StreetAddress mStart;
	private StreetAddress mEnd;
	private String mStartString;
	private String mEndString;
	private Integer mRadius = 4000;
	private String mTravelMode;
	private Boolean mOpenNow = false;
	
	private static ArrayList<String> mPlaceTypes;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.places_menu, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menu_types:
			{
				TypeChooserDialogFragment chooserFragment = new TypeChooserDialogFragment();
				chooserFragment.show(getFragmentManager(), "type");
				return true;
			}
			case R.id.menu_radius:
			{
				RadiusDialogFragment radius = new RadiusDialogFragment();
				Bundle data = new Bundle();
				data.putInt("radius", mRadius);
				radius.setArguments(data);
				radius.show(getFragmentManager(), "radius");
				return true;
			}
			case R.id.menu_options:
			{
				OptionsDialogFragment options = new OptionsDialogFragment();
				Bundle data = new Bundle();
				data.putBoolean("open", mOpenNow);
				data.putString("travelmode", mTravelMode);
				options.setArguments(data);
				options.show(getFragmentManager(), "options");
				return true;
			}
			default: return true;
		}
	}
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Get start and end points and get directions
        Bundle extras = getIntent().getExtras();
        
        mPlaceTypes = null;
        mPlaceTypes = extras.getStringArrayList("types");  

        mStartString = extras.getString("start");
        mEndString = extras.getString("end");
        StartGetDirections(mStartString, mEndString);
        
        //Set up Map
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
        {
			@Override
			public void onInfoWindowClick(Marker arg0) {
				Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("google.navigation:q=" + arg0.getPosition().latitude + "," + arg0.getPosition().longitude));
				startActivity(mapIntent);
			}
        	
        });	      
        map.setMyLocationEnabled(true);
        
        //Get location data
        mLocationClient = new LocationClient(this, this, this);
    }
	 
	 public void ReCalculateMiddlePoints(ArrayList types)
	 {
		 //Clear the map of old markers
		 GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		 map.clear();
		 
		 //Get array list of types
		 mPlaceTypes = new ArrayList<String>();
		 String[] typesList = getResources().getStringArray(R.array.place_types);
		 for(Object index : types)
		 {
			 Integer i = (Integer)index;
			 mPlaceTypes.add(typesList[i]);
		 }
		 
		 StartGetDirections(mStartString, mEndString);
	 }
	 public void ReCalculateMiddlePoints(Integer radius)
	 {
		//Clear the map of old markers
		 GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		 map.clear();
		 
		 mRadius = radius;
		 
		 StartGetDirections(mStartString, mEndString);
	 }
	 public void ReCalculateMiddlePoints(Boolean openNow, String travelMode)
	 {
		 mTravelMode = travelMode;
		 mOpenNow = openNow;
		 
		//Clear the map of old markers
		 GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		 map.clear();
		 
		 StartGetDirections(mStartString, mEndString);
	 }
	 
	 private void StartGetDirections(String start, final String end)
	 {
		AddressSearch startAddress = new AddressSearch()
		{
        	@Override
        	public void onPostExecute(StreetAddress results)
        	{
        		mStart = results;
        		AddressSearch endAddress = new AddressSearch()
        		{
        			@Override
        			public void onPostExecute(StreetAddress results)
        			{
        				mEnd = results;
        				Directions myDirections = new Directions(mStart, mEnd)
        				{
        					@Override
        					public void onPostExecute(DirectionsData results)
        					{
        						DrawRoute(results);
        					}
        				};
        				if(mTravelMode != null)
        				{
        					myDirections.setTravelMode(mTravelMode);
        				}
        				myDirections.getDirections();
        			}
        		};
        		endAddress.lookupAddress(end);
        	}
        };
        startAddress.lookupAddress(start);
	 }
	 
	private void DrawRoute(DirectionsData results) 
	{
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(results.getStepData().get(0).getStartLocation(),  13));
        
		PolylineOptions route = new PolylineOptions();
		for(StepData sd : results.getStepData())
		{
			route.add(sd.getStartLocation());
			route.add(sd.getEndLocation());
		}

		map.addPolyline(route);
		map.addMarker(new MarkerOptions().title("My Location")
        		.position(new LatLng(mStart.getLat(), mStart.getLon())));
		map.addMarker(new MarkerOptions().title("Their Location")
        		.position(new LatLng(mEnd.getLat(), mEnd.getLon())));
		
		LatLng middlePoint = FindMiddleOfRoute(results);
		map.addMarker(new MarkerOptions().title("Middle").position(middlePoint));
		
		Places findPlaces = new Places()
		{
			@Override
        	public void onPostExecute(ArrayList<Place> results)
        	{
				for(Place sa : results)
				{
					GoogleMap gm = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
					gm.addMarker(new MarkerOptions().title(sa.getmName())
							.position(new LatLng(sa.getmLatitude(), sa.getmLongitude())));
				}
        	}
		};
		if(mRadius != null) { findPlaces.setRadius(mRadius); }
		findPlaces.getPlaces(middlePoint, mPlaceTypes);
	
		builder.include(new LatLng(mStart.getLat(), mStart.getLon()));
		builder.include(new LatLng(mEnd.getLat(), mEnd.getLon()));
		LatLngBounds bounds = builder.build();
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
		map.animateCamera(cu);
	}

	private LatLng FindMiddleOfRoute(DirectionsData results) 
	{
		//Step 1 - find total distance of all the legs;
		float totalDistance = 0;
		float distanceToGo = 0;
		LatLng midPoint = null;
		for(StepData sd : results.getStepData())
		{
			totalDistance += sd.getDistance();
		}
		distanceToGo = totalDistance / 2;
		
		//Step through "legs" and find middle...
		for(StepData sd : results.getStepData())
		{
			if(sd.getDistance() < distanceToGo)
			{
				distanceToGo -= sd.getDistance();
			}
			else	//else - midpoint is on this leg
			{
				//use ratio to determine to lat/lon
				double totalSegment = sd.getDistance();
				
				//difference in lat/lon along this line
				double latDiff = sd.getEndLocation().latitude - sd.getStartLocation().latitude;
				double longDiff = sd.getEndLocation().longitude - sd.getStartLocation().longitude;
				
				//difference in lat/lon from start point to midpoint on the line
				double newLat = (distanceToGo * latDiff) / totalSegment;
				double newLong = (distanceToGo * longDiff) / totalSegment;
				
				//add difference in lat/lon to start point to get new mid point location
				midPoint = new LatLng(sd.getStartLocation().latitude + newLat, sd.getStartLocation().longitude + newLong);

				distanceToGo = -1;
				break;
			}
		}
		
		return midPoint;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
        	Toast.makeText(this, "Error: connectionResult.getErrorCode()", Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public void onConnected(Bundle arg0) {	
		/*
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        mLocation = mLocationClient.getLastLocation();
        LatLng cur = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cur,  13));
        */
		
        /*
        //Call ASYNC TASK to get list of locations and populate map
        Places gPlaces = new Places()
        {
        	@Override
        	public void onPostExecute(String results)
        	{
        		Log.i("output", results);
        		ProcessLocationResults(results);
        	}
        };
        gPlaces.getPlaces(mLocation);
        */
	}

	//Handle the results from getting nearby locations
	protected void ProcessLocationResults(String results) {
		JSONObject jObject;
		ArrayList<LatLng> myPlaces = new ArrayList<LatLng>();
		ArrayList<String> myNames = new ArrayList<String>();
		
		try 
		{
			jObject = new JSONObject(results);
			//Get array of places
			JSONArray allResults = jObject.getJSONArray("results");
			//Parse array of each location
			for(int i = 0; i < allResults.length(); i++)
			{
				JSONObject location = allResults.getJSONObject(i);
				//Get location
				JSONObject geometry = location.getJSONObject("geometry");
				JSONObject loc = geometry.getJSONObject("location");
				double lat = loc.getDouble("lat");
				double lng = loc.getDouble("lng");
				LatLng newPlace = new LatLng(lat, lng);
				myPlaces.add(newPlace);

				//Get name
				String name = location.getString("name");
				myNames.add(name);
			}
			
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < myPlaces.size(); i++)
		{
			GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			map.addMarker(new MarkerOptions().title(myNames.get(i))
		        		.position(myPlaces.get(i)));
		}

	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
	}
	
	
	/*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
}
