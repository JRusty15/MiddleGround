package com.stander.mapstest;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class OptionsDialogFragment extends DialogFragment
{
	private static ArrayList<String> mTravelModes = new ArrayList<String>()
			{{ 
				add("driving"); 
				add("walking"); 
				add("bicycling");
				add("transit");
			}};
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Boolean open = getArguments().getBoolean("open");
		String travelmode = getArguments().getString("travelmode");
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.options_menu, null);
		final Spinner travelModes = (Spinner)v.findViewById(R.id.spinnerTravelMode);
		
		ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mTravelModes.toArray());
		travelModes.setAdapter(adapter);
		
		if(travelmode != null)
		{
			if(travelmode.toLowerCase() == "driving")
			{
				travelModes.setSelection(0); 
			}
			else if(travelmode.toLowerCase() == "walking")
			{
				travelModes.setSelection(1); 
			}
			else if(travelmode.toLowerCase() == "bicycling")
			{
				travelModes.setSelection(2); 
			}
			else if(travelmode.toLowerCase() == "transit")
			{
				travelModes.setSelection(3); 
			}
		}
		
		final CheckBox opencheck = (CheckBox)v.findViewById(R.id.cbOpenNow);
		if(open != null && open == true)
		{
			opencheck.setChecked(true); 
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(v)
		.setTitle("Select Options")
		.setPositiveButton("Save", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				((MapActivity)getActivity()).ReCalculateMiddlePoints(opencheck.isChecked(), travelModes.getSelectedItem().toString());
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		return builder.create();
	}
}
