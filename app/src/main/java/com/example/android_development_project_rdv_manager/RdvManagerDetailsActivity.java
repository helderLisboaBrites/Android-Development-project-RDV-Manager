package com.example.android_development_project_rdv_manager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

class RdvManagerDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rdv_manager_details_activity);

        EditText etTitre =     (EditText) findViewById(R.id.etTitre);
        EditText etDate =      (EditText) findViewById(R.id.etDate);
        EditText etTime =      (EditText) findViewById(R.id.etTime);
        EditText etContact =   (EditText) findViewById(R.id.etContact);
        EditText etAddress =   (EditText) findViewById(R.id.etAddress);
        EditText etPhoneNum =  (EditText) findViewById(R.id.etPhoneNum);
        Switch switchState =    (Switch)    findViewById(R.id.switchState);

    }


    public void onSaveRdv(View view) {
    }

    public void onCancelClick(View view) {
        finish();
    }
}
