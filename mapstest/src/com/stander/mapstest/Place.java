package com.stander.mapstest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Place 
{
	private double mLatitude;
	private double mLongitude;
	private String mIcon;
	private String mId;
	private String mName;
	private boolean mOpenNow;
	private int mPhotoHeight;
	private String mPhotoRef;
	private int mPhotoWidth;
	private int mPriceLevel;
	private String mReference;
	private String mVicinity;
	
	public Place()
	{
		
	}
	
	public Place(String jsonData)
	{
		try
		{
			JSONObject data = new JSONObject(jsonData);
			
			JSONObject geo = data.getJSONObject("geometry");
			JSONObject latlon = geo.getJSONObject("location");
			mLatitude = latlon.getDouble("lat");
			mLongitude = latlon.getDouble("lng");
			
			mIcon = data.getString("icon");
			mId = data.getString("id");
			mName = data.getString("name");
			
			JSONObject hours = data.getJSONObject("opening_hours");
			mOpenNow = (hours.getString("open_now") == "true");
			
			JSONArray photo = data.getJSONArray("photos");
			JSONObject p = photo.getJSONObject(0);
			mPhotoHeight = p.getInt("height");
			mPhotoRef = p.getString("photo_reference");
			mPhotoWidth = p.getInt("width");
			
			mPriceLevel = data.getInt("price_level");
			mReference = data.getString("reference");
			mVicinity = data.getString("vicinity");
		}
		catch(JSONException e)
		{
			
		}
	}
	
	public double getmLatitude() {
		return mLatitude;
	}
	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}
	public double getmLongitude() {
		return mLongitude;
	}
	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}
	public String getmIcon() {
		return mIcon;
	}
	public void setmIcon(String mIcon) {
		this.mIcon = mIcon;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public boolean ismOpenNow() {
		return mOpenNow;
	}
	public void setmOpenNow(boolean mOpenNow) {
		this.mOpenNow = mOpenNow;
	}
	public int getmPhotoHeight() {
		return mPhotoHeight;
	}
	public void setmPhotoHeight(int mPhotoHeight) {
		this.mPhotoHeight = mPhotoHeight;
	}
	public String getmPhotoRef() {
		return mPhotoRef;
	}
	public void setmPhotoRef(String mPhotoRef) {
		this.mPhotoRef = mPhotoRef;
	}
	public int getmPhotoWidth() {
		return mPhotoWidth;
	}
	public void setmPhotoWidth(int mPhotoWidth) {
		this.mPhotoWidth = mPhotoWidth;
	}
	public int getmPriceLevel() {
		return mPriceLevel;
	}
	public void setmPriceLevel(int mPriceLevel) {
		this.mPriceLevel = mPriceLevel;
	}
	public String getmReference() {
		return mReference;
	}
	public void setmReference(String mReference) {
		this.mReference = mReference;
	}
	public String getmVicinity() {
		return mVicinity;
	}
	public void setmVicinity(String mVicinity) {
		this.mVicinity = mVicinity;
	}
}
