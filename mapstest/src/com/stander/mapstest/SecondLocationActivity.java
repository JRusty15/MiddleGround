package com.stander.mapstest;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

public class SecondLocationActivity extends Activity
{
	private String m_Start = "";
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondlocation);
        
        //get data from location input
        Bundle extras = getIntent().getExtras();
        m_Start = extras.getString("start");
        
        //Map UI elements
        AutoCompleteTextView end = (AutoCompleteTextView)findViewById(R.id.txtEndLocation);
        end.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        
        Button go = (Button)findViewById(R.id.btnGo);
        go.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) 
			{
				//get second point location from gui
				AutoCompleteTextView end = (AutoCompleteTextView)findViewById(R.id.txtEndLocation);
				Intent i = new Intent(getApplicationContext(), MapActivity.class);
				i.putExtra("start", m_Start);	//this comes from previous screen
				i.putExtra("end", end.getText().toString());
				startActivity(i);
			}
        	
        });
	 }
}
