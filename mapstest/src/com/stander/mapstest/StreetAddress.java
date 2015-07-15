package com.stander.mapstest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class StreetAddress 
{
	private String mAddress;
	private double mLat;
	private double mLon;
	private String mIcon;
	private String mName;
	
	public StreetAddress() 
	{
	}
	
	public static ArrayList<StreetAddress> GetListOfStreetAddresses(String jsonData)
	{
		Log.i("Food", jsonData);
		
		ArrayList<StreetAddress> places = new ArrayList<StreetAddress>();
		
		try
		{
			JSONObject data = new JSONObject(jsonData);
			JSONArray arrayData = data.getJSONArray("results");
			for(int i = 0; i < arrayData.length(); i++)
			{
				JSONObject place = arrayData.getJSONObject(i);
				StreetAddress newPlace = new StreetAddress();
				
				//newPlace.setAddress(place.getString("formatted_address"));
				JSONObject geo = place.getJSONObject("geometry");
				JSONObject latLon = geo.getJSONObject("location");
				newPlace.setLat(latLon.getDouble("lat"));
				newPlace.setLon(latLon.getDouble("lng"));
				newPlace.setIcon(place.getString("icon"));
				newPlace.setName(place.getString("name"));
				
				places.add(newPlace);
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		return places;
	}
	
	public StreetAddress(String jsonData)
	{
		try 
		{
			JSONObject data = new JSONObject(jsonData);
			JSONArray arrayData = data.getJSONArray("results");
			JSONObject first = arrayData.getJSONObject(0);
			mAddress = first.getString("formatted_address");
			JSONObject geo = first.getJSONObject("geometry");
			JSONObject latLon = geo.getJSONObject("location");
			mLat = latLon.getDouble("lat");
			mLon = latLon.getDouble("lng");
			mIcon = first.getString("icon");
			mName = first.getString("name");
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public double getLat() {
		return mLat;
	}

	public void setLat(double mLat) {
		this.mLat = mLat;
	}

	public double getLon() {
		return mLon;
	}

	public void setLon(double mLon) {
		this.mLon = mLon;
	}

	public String getIcon() {
		return mIcon;
	}

	public void setIcon(String mIcon) {
		this.mIcon = mIcon;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
}
