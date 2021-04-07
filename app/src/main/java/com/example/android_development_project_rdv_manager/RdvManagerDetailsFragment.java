package com.example.android_development_project_rdv_manager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.Calendar;

public class RdvManagerDetailsFragment extends Fragment{

    public static final int FLAG_DATETIME_RDV = 0;
    public static final int FLAG_DATETIME_NOTIFICATION = 1;

    private Rdv currentRdv;
    private EditText etTitre ;
    private Button btDate ;
    private Button btTime ;
    private EditText etContact ;
    private EditText etAddress ;
    private EditText etPhoneNum ;
    private EditText etDescription;
    private Switch switchState ;
    private Button btNotificationDate ;
    private Button btNotificationTime ;
    private Button btSave;
    private Button btCancel;
    private ImageView btPhone;
    private boolean fromAdd;
    private DatabaseHelper database;

    SimpleCursorAdapter adapter;
    static final int LOADER_ID =1976;
    private static final int PICK_CONTACT=122;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    final String[] from =   new String[]{ContactsContract.Contacts.DISPLAY_NAME};

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
        btPhone = (ImageButton) view.findViewById(R.id.button_phone);
        btNotificationDate =      (Button) view.findViewById(R.id.btNotificationDate);
        btNotificationTime =      (Button) view.findViewById(R.id.btNotificationTime);
        btPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });



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
                pickDate(view, FLAG_DATETIME_RDV);
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(view, FLAG_DATETIME_RDV);
            }
        });

        btNotificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate(view, FLAG_DATETIME_NOTIFICATION);
            }
        });

        btNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(view, FLAG_DATETIME_NOTIFICATION);
            }
        });


        //switchState.setOnCheckedChangeListener((buttonView, isChecked) -> );

        Intent intent = getActivity().getIntent();
        fromAdd = intent.getBooleanExtra("fromAdd",false);
        Bundle extras = intent.getExtras();
        if (!fromAdd && extras != null) {
            Rdv rdv_saved = (Rdv)extras.getParcelable("rdv_saved");
            setRdv(rdv_saved);
        }else{
            setRdv(new Rdv());
        }

        requestPermissions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rdv_manager_details, container, false);
        requestPermissions();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        requestPermissions();
    }

    public void setRdv(Rdv rdv_saved) {
        this.currentRdv = rdv_saved;
        etTitre.setText(rdv_saved.getTitle());
        btDate.setText(rdv_saved.getDateString());
        btTime.setText(rdv_saved.getTimeString());
        etDescription.setText(rdv_saved.getDescription());

        etContact.setText(rdv_saved.getContact());
        etAddress.setText(rdv_saved.getAddress());
        etPhoneNum.setText(rdv_saved.getPhone());
        switchState.isChecked();
        btNotificationDate.setText(rdv_saved.getNotificationDateString());
        btNotificationTime.setText(rdv_saved.getNotificationTimeString());
    }

    private void onSaveRdv() {
        Log.d(TAG,"onSaveRdv");
        String title = etTitre.getText().toString();
        String description = etDescription.getText().toString();
        String contact = etContact.getText().toString();
        String address = etAddress.getText().toString();
        String phoneNum = etPhoneNum.getText().toString();
        boolean done = switchState.isChecked();

        currentRdv.setTitle(title);
        currentRdv.setDescription(description);
        // date auto
        currentRdv.setContact(contact);
        currentRdv.setAddress(address);
        currentRdv.setPhone(phoneNum);
        currentRdv.setDone(done);

        if(fromAdd){ // pour ajouter un rdv
            database.addRdv(currentRdv);
            Intent main = new Intent(this.getActivity(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);

        }else{// pour modifier un rdv
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

        NotificationHelper.notify(getContext(), getView(), currentRdv);
    }

    private void onCancelRdv() {
        getActivity().finish();

    }

    public void pickDate(View view, int flag){
        showDatePicker(flag);
    }

    private void showDatePicker(int flag) {

        DatePickerFragment date = new DatePickerFragment();

        Bundle args = new Bundle();
        switch (flag) {
            case RdvManagerDetailsFragment.FLAG_DATETIME_RDV :
                args.putInt("year", currentRdv.getDatetime().getYear());
                args.putInt("month", currentRdv.getDatetime().getMonthValue() - 1);
                args.putInt("day", currentRdv.getDatetime().getDayOfMonth());
                break;
            case RdvManagerDetailsFragment.FLAG_DATETIME_NOTIFICATION :
                args.putInt("year", currentRdv.getNotification().getYear());
                args.putInt("month", currentRdv.getNotification().getMonthValue() - 1);
                args.putInt("day", currentRdv.getNotification().getDayOfMonth());
                break;
        }
        args.putInt("flag", flag);

        date.setArguments(args);
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                switch (flag) {
                    case RdvManagerDetailsFragment.FLAG_DATETIME_RDV :
                        RdvManagerDetailsFragment.this.onDateSet(datePicker, year, month, day);
                        break;
                    case RdvManagerDetailsFragment.FLAG_DATETIME_NOTIFICATION :
                        RdvManagerDetailsFragment.this.onNotificationDateSet(datePicker, year, month, day);
                        break;
                }
            }
        });

        date.show(getActivity().getSupportFragmentManager(),"Date Picker");
    }

    public void onDateSet(DatePicker view, int hour, int minute, int second) {
        currentRdv.setDate(hour, minute, second);
        btDate.setText(currentRdv.getDateString());
    }

    public void onNotificationDateSet(DatePicker view, int hour, int minute, int second) {
        currentRdv.setNotificationDate(hour, minute, second);
        btNotificationDate.setText(currentRdv.getNotificationDateString());
    }

    public void pickTime(View view, int flag){
        showTimePicker(flag);
    }

    private void showTimePicker(int flag) {

        TimePickerFragment time = new TimePickerFragment();

        Bundle args = new Bundle();
        switch (flag) {
            case RdvManagerDetailsFragment.FLAG_DATETIME_RDV :
                args.putInt("hour", currentRdv.getDatetime().getHour());
                args.putInt("minute", currentRdv.getDatetime().getMinute());
                break;
            case RdvManagerDetailsFragment.FLAG_DATETIME_NOTIFICATION :
                args.putInt("hour", currentRdv.getNotification().getHour());
                args.putInt("minute", currentRdv.getNotification().getMinute());
                break;
        }

        time.setArguments(args);
        time.setCallBack(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                switch (flag) {
                    case RdvManagerDetailsFragment.FLAG_DATETIME_RDV :
                        RdvManagerDetailsFragment.this.onTimeSet(timePicker, hour, minute);
                        break;
                    case RdvManagerDetailsFragment.FLAG_DATETIME_NOTIFICATION :
                        RdvManagerDetailsFragment.this.onNotificationTimeSet(timePicker, hour, minute);
                        break;
                }
            }
        });

        time.show(getActivity().getSupportFragmentManager(),"Time Picker");
    }

    public void onTimeSet(TimePicker view, int hour, int minute) {
        currentRdv.setTime(hour, minute);
        btTime.setText(currentRdv.getTimeString());
    }

    public void onNotificationTimeSet(TimePicker view, int hour, int minute) {
        currentRdv.setNotificationTime(hour, minute);
        btNotificationTime.setText(currentRdv.getNotificationTimeString());
    }


    public void requestPermissions()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED)
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
        {
           // getActivity().getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // Permission is granted
                //getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            } else {
                Toast.makeText(getActivity(), "You must grant permission to display contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (PICK_CONTACT):
                if (resultCode == MainActivity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String hasPhone =
                      c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                    if (hasPhone.equalsIgnoreCase("1"))
                    {
                      Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                      phones.moveToFirst();
                      String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                      etPhoneNum.setText(cNumber);
                      etContact.setText(name);
                    }
                    }

             }
        }
    }
}
