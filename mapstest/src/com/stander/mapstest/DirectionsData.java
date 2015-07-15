package com.stander.mapstest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

public class DirectionsData 
{
	ArrayList<StepData> mSteps;
	
	public DirectionsData()
	{
		mSteps = new ArrayList<StepData>();
	}
	public DirectionsData(String jsonData)
	{
		mSteps = new ArrayList<StepData>();
		try 
		{
			JSONObject data = new JSONObject(jsonData);
			JSONArray routesArray = data.getJSONArray("routes");
			JSONObject route = routesArray.getJSONObject(0);
			
			JSONArray legsArray = route.getJSONArray("legs");
			JSONObject leg = legsArray.getJSONObject(0);
			
			JSONArray steps = leg.getJSONArray("steps");
			for(int i = 0; i < steps.length(); i++)
			{
				JSONObject step = steps.getJSONObject(i);
				JSONObject endLoc = step.getJSONObject("end_location");
				LatLng endLL = new LatLng(endLoc.getDouble("lat"), endLoc.getDouble("lng"));
				JSONObject startLoc = step.getJSONObject("start_location");
				LatLng startLL = new LatLng(startLoc.getDouble("lat"), startLoc.getDouble("lng"));
				StepData currentStep = new StepData();
				currentStep.setStartLocation(startLL);
				currentStep.setEndLocation(endLL);
				mSteps.add(currentStep);
			}
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<StepData> getStepData()
	{
		return mSteps;
	}
}
