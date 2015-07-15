package com.stander.mapstest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TypesItemsActivity extends Activity 
{
	private static ArrayList<String> savedTypes;
	private static ArrayList<String> googleAllowedTypes = new ArrayList<String>()
			{{ 
				add("bar"); 
				add("cafe"); 
				add("food");
				add("gas_station");
				add("gym");
				add("library");
				add("lodging");
				add("movie_theater");
				add("night_club");
				add("park");
				add("shopping_mall");
				add("spa");
				
			}};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.types_items);
		
		Bundle extras = getIntent().getExtras();
		savedTypes = extras.getStringArrayList("types");
		
		Button saveButton = (Button)findViewById(R.id.btnSaveTypes);
		saveButton.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
			}
		});
		
		LinearLayout typesLayout = (LinearLayout)findViewById(R.id.typesLayout);
		
		for(String s : googleAllowedTypes)
		{
			CheckBox cb = new CheckBox(this);
			cb.setText(s);
			cb.setOnClickListener(checkBoxListener(cb));
			typesLayout.addView(cb);
		}
	}
	
	View.OnClickListener checkBoxListener(final Button button)
	{
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), "You clicked " + button.getText(), Toast.LENGTH_LONG).show();
			}
		};
	}
}
