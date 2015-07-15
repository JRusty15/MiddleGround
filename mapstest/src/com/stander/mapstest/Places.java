package com.stander.mapstest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class Places extends AsyncTask<LatLng, Integer, ArrayList<Place>>
{
	private LatLng mLocation;
	private ArrayList<String> mTypes;
	private Integer mRadius;
	private Boolean mIsOpenNow;
	
	public Places(LatLng tLocation)
	{
		mLocation = tLocation;
		mTypes = null;
		mRadius = 4000;
	}
	public Places()
	{
		mTypes = null;
		mRadius = 4000;
	}
	
	public void setIsOpenNow(Boolean open)
	{
		mIsOpenNow = open;
	}
	
	@Override
	protected ArrayList<Place> doInBackground(LatLng... params)
	{
		if(params.length != 1)
		{
			return null;
		}
		
		mLocation = params[0];
		return GetNearbyPlaces();
	}
	
	private String runQuery(String requestUrl)
	{
		String output = "";
		
		//requestUrl = URLEncoder.encode(requestUrl);
		
		HttpResponse response = null;
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(0));
		httpClient.getParams().setParameter("http.connection.stalecheck", Boolean.valueOf(true));	

		HttpGet httpget = new HttpGet(requestUrl);

		try
		{
			response = httpClient.execute(httpget);
			
			if(response == null || response.getStatusLine().getStatusCode() != 200)
			{
				if(response != null)
				{
					Log.i("near", "Status: " + response.getStatusLine());
				}
			}
			else
			{
				HttpEntity entity = response.getEntity();
				if(entity != null)
				{
					InputStream instream = entity.getContent();
					output = convertStreamToString(instream);
					instream.close();	
				}
			}
		}
		catch (Exception ex) 
		{
			Log.i("HTTP", "Exception caught: " + ex.getMessage());
		}
		
		return output;
	}
	

	private ArrayList<Place> GetNearbyPlaces() {
		String results = "";
		String tempOutput = "";
		
		Log.i("near", "Getting nearby places from " + mLocation.latitude + ", " + mLocation.longitude);
		
		String requestUrl = "";
		requestUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
		requestUrl += "key=" + "AIzaSyCMd39HjXAesq9Hk4Yo54Vnd5UPLohu5rU";
		requestUrl += "&location=" + mLocation.latitude + "," + mLocation.longitude;
		requestUrl += "&radius=" + mRadius.toString();
		requestUrl += "&sensor=true";
		if(mIsOpenNow != null)
		{
			requestUrl += "&opennow=" + (mIsOpenNow ? "true" : "false");
		}
		if(mTypes != null)
		{
			requestUrl += "&types=";
			for(String s : mTypes)
			{
				requestUrl += s + URLEncoder.encode("|");
			}
			//remove the last pipe
			requestUrl = requestUrl.substring(0, requestUrl.length()-3);
		}
		else
		{
			requestUrl += "&types=food";
		}
		
		Log.i("near", requestUrl);
		
		results = runQuery(requestUrl);
		tempOutput = results;
		
		String nextPage = "start";
		while(nextPage != "")
		{
			try
			{
				nextPage = "";
				JSONObject data = new JSONObject(tempOutput);
				nextPage = data.getString("next_page_token");
			}
			catch(JSONException e)
			{
				nextPage = "";
				Log.e("ERROR", "Error caught getting next page token: " + e.getMessage());
			}
			
			if(nextPage != "")
			{
				String newRequest = requestUrl += "&pagetoken=" + nextPage;
				tempOutput = runQuery(newRequest);
				results += tempOutput;
			}
		}
		
		ArrayList<Place> resultPlaces = new ArrayList<Place>();
		
		try
		{
			JSONObject data = new JSONObject(results);
			JSONArray resultsArray = data.getJSONArray("results");
			for(int i = 0; i < resultsArray.length(); i++)
			{
				Place p = new Place(resultsArray.getJSONObject(i).toString());
				resultPlaces.add(p);
			}
		}
		catch(JSONException e)
		{
			
		}
		
		return resultPlaces;
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
	 
	 public void setRadius(Integer radius)
	 {
		 mRadius = radius;
	 }
	 
	 public void getPlaces(LatLng input, ArrayList<String> types)
	 {
		 mTypes = types;
		 this.execute(input);
	 }
}
