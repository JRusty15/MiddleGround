package com.stander.mapstest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartupActivity extends Activity 
{
	 @Override
	 protected void onCreate(Bundle savedInstanceState) 
	 {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        
        Button start = (Button)findViewById(R.id.btnStart);
        start.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(getApplicationContext(), MyLocationActivity.class);
				startActivity(i);
			}
        });
	 }
}
