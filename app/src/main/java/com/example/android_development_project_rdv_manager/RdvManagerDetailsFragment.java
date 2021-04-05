package com.example.android_development_project_rdv_manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RdvManagerDetailsFragment extends Fragment {

    private Rdv currentRdv;
    private EditText etTitre ;
    private EditText etDate ;
    private EditText etTime ;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rdv_manager_details, container, false);

        etTitre =     (EditText) view.findViewById(R.id.etTitre);
        etDate =      (EditText) view.findViewById(R.id.etDate);
        etTime =      (EditText) view.findViewById(R.id.etTime);
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

        Intent intent = getActivity().getIntent();
        fromAdd = intent.getBooleanExtra("fromAdd",false);
        Bundle extras = intent.getExtras();
        if (!fromAdd && extras != null) {
            Rdv rdv_saved = (Rdv)extras.getParcelable("rdv_saved");
            this.currentRdv = rdv_saved;
            setRdv(rdv_saved);

            //etTime.setText(rdv_saved.t);
            //etContact.setText(rdv_saved.c);
            //etAddress.setText(rdv_saved.);
            //etPhoneNum.setText(rdv_saved.);
        }
        return view;

    }

    private void setRdv(Rdv rdv_saved) {
        etTitre.setText(rdv_saved.getTitle());
        etDate.setText(rdv_saved.getDate());
        etDescription.setText(rdv_saved.getDescription());
    }



    private void onSaveRdv() {
        Log.d(TAG,"onSaveRdv");
        String id;
        String title = etTitre.getText().toString();
        String description = etDescription.getText().toString();
        String date = etDate.getText().toString();
        boolean done;
        if(fromAdd){
            currentRdv = new Rdv(title,
                                description,
                                date   /*, etTime, etContact, etAddress, etPhoneNum, etDescriptio, switchState*/);

            database.addRdv(currentRdv);

        }else{
            currentRdv.setTitle(title);
            currentRdv.setDescription(description);
            currentRdv.setDate(date);
            database.updateRdv(currentRdv);

        }
        Intent main = new Intent(this.getActivity(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }

    private void onCancelRdv() {

    }
}
