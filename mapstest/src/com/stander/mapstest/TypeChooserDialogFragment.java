package com.stander.mapstest;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class TypeChooserDialogFragment extends DialogFragment 
{
	private ArrayList mSelectedItems;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		mSelectedItems = new ArrayList();
		
		for(Object s : mSelectedItems)
		{
			Toast.makeText(getActivity().getBaseContext(), s.toString(), Toast.LENGTH_LONG).show();
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Types of Places")
		.setPositiveButton("Save", new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				((MapActivity)getActivity()).ReCalculateMiddlePoints(mSelectedItems);
				//Toast.makeText(getActivity().getBaseContext(), "You clicked save", Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
			
			}
		})
		.setMultiChoiceItems(R.array.place_names, null, new DialogInterface.OnMultiChoiceClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) 
			{
				if(isChecked)
				{
					mSelectedItems.add(which);
				}
				else if(mSelectedItems.contains(which))
				{
					mSelectedItems.remove(Integer.valueOf(which));
				}
			}
		});
		return builder.create();
	}
}
