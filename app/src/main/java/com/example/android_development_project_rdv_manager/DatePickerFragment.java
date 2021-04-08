package com.example.android_development_project_rdv_manager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

	int day, month, year;
	DatePickerDialog.OnDateSetListener dateSet;

	public DatePickerFragment() {

	}

	public void setCallBack(DatePickerDialog.OnDateSetListener onDate){
		dateSet = onDate;
	}

	@Override
	public void setArguments(@Nullable Bundle args) {
		super.setArguments(args);
		year = args.getInt("year");
		month = args.getInt("month");
		day = args.getInt("day");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		return new DatePickerDialog(getActivity(), dateSet, year, month, day);
	}
}