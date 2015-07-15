package com.stander.mapstest;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class StepData 
{
	private LatLng mEndLocation;
	private LatLng mStartLocation;
	
	public LatLng getEndLocation() {
		return mEndLocation;
	}
	public void setEndLocation(LatLng mEndLocation) {
		this.mEndLocation = mEndLocation;
	}
	public LatLng getStartLocation() {
		return mStartLocation;
	}
	public void setStartLocation(LatLng mStartLocation) {
		this.mStartLocation = mStartLocation;
	}
	
	public float getDistance()
	{
		Location s = new Location("");
		s.setLatitude(mStartLocation.latitude);
		s.setLongitude(mStartLocation.longitude);
		Location e = new Location("");
		e.setLatitude(mEndLocation.latitude);
		e.setLongitude(mEndLocation.longitude);
		//distance is in meters
		float distance = s.distanceTo(e);
		return distance;
	}
}
