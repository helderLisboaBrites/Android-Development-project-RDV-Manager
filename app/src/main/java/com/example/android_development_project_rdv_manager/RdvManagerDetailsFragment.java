package com.example.android_development_project_rdv_manager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class RdvManagerDetailsFragment extends Fragment  {

    private Rdv currentRdv;
    private EditText etTitre ;
    private Button btDate ;
    private Button btTime ;
    private EditText etContact ;
    private EditText etAddress ;
    private EditText etPhoneNum ;
    private EditText etDescription;
    private Switch switchState ;
    private Button btSave;
    private Button btCancel;
    private boolean fromAdd;
    private DatabaseHelper database;

    private static final String TAG = "RdvManagerDetailsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseHelper(getActivity());
        database.open();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTitre =     (EditText) view.findViewById(R.id.etTitre);
        btDate =      (Button) view.findViewById(R.id.btDate);
        btTime =      (Button) view.findViewById(R.id.btTime);
        etContact =   (EditText) view.findViewById(R.id.etContact);
        etAddress =   (EditText) view.findViewById(R.id.etAddress);
        etPhoneNum =  (EditText) view.findViewById(R.id.etPhoneNum);
        etDescription =  (EditText) view.findViewById(R.id.etDescription);
        switchState =    (Switch)  view.findViewById(R.id.switchState);
        btSave = (Button) view.findViewById(R.id.button_save);
        btCancel = (Button) view.findViewById(R.id.button_cancel);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--------------------------------------------------------------");
                Log.d(TAG,"onSaveRdv");
                onSaveRdv();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btCancel");
                onCancelRdv();
            }
        });

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(view);
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(view);
            }
        });

        //switchState.setOnCheckedChangeListener((buttonView, isChecked) -> );

        Intent intent = getActivity().getIntent();
        fromAdd = intent.getBooleanExtra("fromAdd",false);
        Bundle extras = intent.getExtras();
        if (!fromAdd && extras != null) {
            Rdv rdv_saved = (Rdv)extras.getParcelable("rdv_saved");
            setRdv(rdv_saved);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rdv_manager_details, container, false);

        return view;

    }

    public void setRdv(Rdv rdv_saved) {
        this.currentRdv = rdv_saved;
        etTitre.setText(rdv_saved.getTitle());
        btDate.setText(rdv_saved.getDate());
        btTime.setText(rdv_saved.getTime());
        etDescription.setText(rdv_saved.getDescription());

        etContact.setText(rdv_saved.getContact());
        etAddress.setText(rdv_saved.getAddress());
        etPhoneNum.setText(rdv_saved.getPhone());
        switchState.isChecked();


    }

    private void onSaveRdv() {
        Log.d(TAG,"onSaveRdv");
        String id;
        String title = etTitre.getText().toString();
        String description = etDescription.getText().toString();
        String date = btDate.getText().toString() + " " + btTime.getText().toString();
        String contact = etContact.getText().toString();
        String address = etAddress.getText().toString();
        String phoneNum = etPhoneNum.getText().toString();
        boolean done = switchState.isChecked();

        if(fromAdd){ // pour ajouter un rdv
                currentRdv = new Rdv(-1, title,description,date,done,contact,address,phoneNum);

            database.addRdv(currentRdv);
            Intent main = new Intent(this.getActivity(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);

        }else{// pour modifier un rdv
            currentRdv = new Rdv(currentRdv.getId(), title,description,date,done,contact,address,phoneNum);
            database.updateRdv(currentRdv);
            RdvManagerDetailsFragment fragment = (RdvManagerDetailsFragment)getFragmentManager().findFragmentById(R.id.activity_details_fragment);
            if(fragment != null && fragment.isInLayout()) {// si on est en portrait
                Intent main = new Intent(this.getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
            }else {
                RdvListFragment fragment1 = (RdvListFragment)getFragmentManager().findFragmentById(R.id.listFragment);
                fragment1.loadData();
            }


        }

    }

    private void onCancelRdv() {

    }

    public void pickDate(View view){
        showDatePicker();
    }

    private void showDatePicker() {

        DatePickerFragment date = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putInt("year", currentRdv.getDateYear());
        args.putInt("month", currentRdv.getDateMonth() - 1);
        args.putInt("day", currentRdv.getDateDay());

        date.setArguments(args);
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                RdvManagerDetailsFragment.this.onDateSet(datePicker, year, month, day);
            }
        });

        date.show(getActivity().getSupportFragmentManager(),"Date Picker");
    }

    public void onDateSet(DatePicker view, int hour, int minute, int second) {
        currentRdv.setDate(hour, minute, second);
        btDate.setText(currentRdv.getDate());
    }

    public void pickTime(View view){
        showTimePicker();
    }

    private void showTimePicker() {

        TimePickerFragment time = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putInt("hour", currentRdv.getTimeHour());
        args.putInt("minute", currentRdv.getTimeMinute());

        time.setArguments(args);
        time.setCallBack(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                RdvManagerDetailsFragment.this.onTimeSet(timePicker, hour, minute);
            }
        });

        time.show(getActivity().getSupportFragmentManager(),"Time Picker");
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        currentRdv.setTime(hour, minute);
        btTime.setText(currentRdv.getTime());
    }


}
