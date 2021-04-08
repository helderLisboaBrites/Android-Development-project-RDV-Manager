package com.example.android_development_project_rdv_manager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {

	int hour, minute;
	TimePickerDialog.OnTimeSetListener timeSet;

	public TimePickerFragment() {

	}

	public void setCallBack(TimePickerDialog.OnTimeSetListener onTime){
		timeSet = onTime;
	}

	@Override
	public void setArguments(@Nullable Bundle args) {
		super.setArguments(args);
		hour = args.getInt("hour");
		minute = args.getInt("minute");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		return new TimePickerDialog(getActivity(), timeSet, hour, minute, true);
	}
}