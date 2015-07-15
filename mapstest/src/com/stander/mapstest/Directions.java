package com.stander.mapstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class Directions extends AsyncTask<String, Integer, DirectionsData> 
{
	private StreetAddress mStart;
	private StreetAddress mEnd;
	private String mTravelMode;
	
	public Directions(StreetAddress start, StreetAddress end)
	{
		mStart = start;
		mEnd = end;
	}
	
	@Override
	protected DirectionsData doInBackground(String... params) 
	{
		DirectionsData results = null;
		
		results = GetDirections();
		
		return results;
	}

	public void setTravelMode(String travelMode)
	{
		mTravelMode = travelMode;
	}
	
	private DirectionsData GetDirections() 
	{
		String output = "";
		
		String requestUrl = "https://maps.googleapis.com/maps/api/directions/json?";
		requestUrl += "origin=" + mStart.getLat() + "," + mStart.getLon();
		requestUrl += "&destination=" + mEnd.getLat() + "," + mEnd.getLon();
		requestUrl += "&sensor=false";
		requestUrl += "&key=" + "AIzaSyCMd39HjXAesq9Hk4Yo54Vnd5UPLohu5rU";
		if(mTravelMode != null)
		{
			requestUrl += "&mode=" + mTravelMode;
		}
		
		HttpResponse response = null;
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(0));
		httpClient.getParams().setParameter("http.connection.stalecheck", Boolean.valueOf(true));	

		HttpGet httpget = new HttpGet(requestUrl);

		try
		{
			Log.i("direction", "about to execute...");
			
			response = httpClient.execute(httpget);
			
			if(response == null || response.getStatusLine().getStatusCode() != 200)
			{
				Log.i("direction", "Error connecting to url");
				if(response != null)
				{
					Log.i("direction", "Status: " + response.getStatusLine());
				}
			}
			else
			{
				Log.i("direction", response.getStatusLine().toString());
				
				HttpEntity entity = response.getEntity();
				if(entity != null)
				{
					InputStream instream = entity.getContent();
					output = convertStreamToString(instream);
					instream.close();
					
					Log.i("direction", output);
					
				}
			}
		}
		catch (Exception ex) 
		{
			Log.i("HTTP", "Exception caught: " + ex.getMessage());
		}
		
		if(output != "")
		{
			return new DirectionsData(output);
		}
		else
		{
			return null;
		}
	}
	
	public void getDirections()
	{
		this.execute();
	}
	
	private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
}
