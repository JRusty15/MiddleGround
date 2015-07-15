package com.stander.mapstest;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class MyLocationActivity extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener
{
	private LocationClient mLocationClient;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);
        
        //Get location data
        mLocationClient = new LocationClient(this, this, this);
        
        AutoCompleteTextView start = (AutoCompleteTextView)findViewById(R.id.txtStartLocation);
        start.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        
        CheckBox gps = (CheckBox)findViewById(R.id.cbUseGPS);
        gps.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) 
			{
				AutoCompleteTextView start = (AutoCompleteTextView)findViewById(R.id.txtStartLocation);
				if(isChecked)
				{
					start.setEnabled(false);
				}
				else
				{
					start.setEnabled(true);
				}
			}
        	
        });
        
        Button goButton = (Button)findViewById(R.id.btnNext);
        goButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				AutoCompleteTextView start = (AutoCompleteTextView)findViewById(R.id.txtStartLocation);
				CheckBox gps = (CheckBox)findViewById(R.id.cbUseGPS);
				Intent i = new Intent(getApplicationContext(), SecondLocationActivity.class);
				if(gps.isChecked())
				{
					Location curLoc = mLocationClient.getLastLocation();
					i.putExtra("start", curLoc.getLatitude() + "," + curLoc.getLongitude());
				}
				else
				{
					i.putExtra("start", start.getText().toString());
				}
				startActivity(i);
			}
        	
        });
	 }


	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        9000);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
        	Toast.makeText(this, "Error: connectionResult.getErrorCode()", Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public void onConnected(Bundle arg0) {	

	}
	
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
    	super.onStop();
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
    }
}
