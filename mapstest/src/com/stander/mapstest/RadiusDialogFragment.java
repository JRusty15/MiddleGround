package com.stander.mapstest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class RadiusDialogFragment extends DialogFragment
{

	@Override 
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Integer radius = getArguments().getInt("radius");
		
		//radius comes in as meters, so we change to miles for this dialog
		Double radiusInMiles = (double) (Math.round((radius / 1609.0) * 10.0) / 10.0);
		
		final EditText input = new EditText(getActivity().getApplicationContext());
		input.setSelectAllOnFocus(true);
		input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		input.setText(radiusInMiles.toString());
		input.setTextColor(Color.BLACK);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Radius in miles to search")
		.setView(input)
		.setPositiveButton("Save", new DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				Double value = 4000.0;
				try
				{
					value = Double.parseDouble(input.getText().toString());
				}
				catch (Exception ex) {}
				
				//value here is in miles, so change back to meters
				Integer meterValue = (int) (value * 1609);
				
				((MapActivity)getActivity()).ReCalculateMiddlePoints(meterValue);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{

			}
		});
		
		return builder.create();
	}
}
