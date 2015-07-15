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

import android.os.AsyncTask;
import android.util.Log;

public class AddressSearch extends AsyncTask<String, Integer, StreetAddress>
{
	public AddressSearch()
	{
	}
	
	@Override
	protected StreetAddress doInBackground(String... params)
	{
		if(params.length != 1)
		{
			return null;
		}
		

		return SearchAddress(params[0]);
	}
	

	private StreetAddress SearchAddress(String address) {
		String results = "";

		String requestUrl = "";
		requestUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
		requestUrl += "key=" + "AIzaSyCMd39HjXAesq9Hk4Yo54Vnd5UPLohu5rU";
		requestUrl += "&query=" + address.replace(" ", "%20");
		requestUrl += "&sensor=false";

		Log.i("address", requestUrl);
		
		HttpResponse response = null;
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(0));
		httpClient.getParams().setParameter("http.connection.stalecheck", Boolean.valueOf(true));	

		HttpGet httpget = new HttpGet(requestUrl);

		try
		{
			Log.i("address", "about to execute...");
			
			response = httpClient.execute(httpget);
			
			if(response == null || response.getStatusLine().getStatusCode() != 200)
			{
				Log.i("address", "Error connecting to url");
				if(response != null)
				{
					Log.i("address", "Status: " + response.getStatusLine());
				}
			}
			else
			{
				Log.i("address", response.getStatusLine().toString());
				
				HttpEntity entity = response.getEntity();
				if(entity != null)
				{
					InputStream instream = entity.getContent();
					results = convertStreamToString(instream);
					instream.close();
					
					Log.i("address", results);
					
				}
			}
		}
		catch (Exception ex) 
		{
			Log.i("address", "Exception caught: " + ex.getMessage());
		}
		
		if(results != "")
		{
			return new StreetAddress(results);
		}
		else
		{
			return null;
		}
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
	 
	 public void lookupAddress(String input)
	 {
		 this.execute(input);
	 }
}
