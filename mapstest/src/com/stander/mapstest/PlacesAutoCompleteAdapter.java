package com.stander.mapstest;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable
{
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private static final String API_KEY = "AIzaSyCMd39HjXAesq9Hk4Yo54Vnd5UPLohu5rU";
	private ArrayList<String> resultList;
	
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId)
	{
		super(context, textViewResourceId);
	}
	
	@Override
	public int getCount()
	{
		return resultList.size();
	}
	
	@Override
	public String getItem(int index)
	{
		return resultList.get(index);
	}
	
	@Override
	public Filter getFilter()
	{
		Filter filter = new Filter()
		{
			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				FilterResults filterResults = new FilterResults();
				if(constraint != null)
				{
					resultList = autocomplete(constraint.toString());
					
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}
			
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				if(results != null && results.count > 0)
				{
					notifyDataSetChanged();
				}
				else
				{
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}
	
	private ArrayList<String> autocomplete(String input)
	{
		ArrayList<String> resultList = null;
		
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try
		{
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false&key=" + API_KEY);
			sb.append("&components=country:USA");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));
			
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection)url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			
			int read;
			char[] buff = new char[1024];
			while((read = in.read(buff)) != -1)
			{
				jsonResults.append(buff, 0, read);
			}
		}
		catch(Exception e)
		{
			Log.e("placesauto", "Exception caught: " + e.getMessage());
		}
		finally
		{
			if(conn != null) { conn.disconnect(); }
		}
		
		try
		{
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
			
			resultList = new ArrayList<String>(predsJsonArray.length());
			for(int i = 0; i < predsJsonArray.length(); i++)
			{
				resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
			}
		}
		catch(Exception e)
		{
			Log.e("placesauto", "Exception with JSON: " + e.getMessage());
		}
		
		return resultList;
	}
}
