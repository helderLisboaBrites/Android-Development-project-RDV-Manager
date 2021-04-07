package com.example.android_development_project_rdv_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        database.open();

        // database.reset();
        // feedDatabase();

        NotificationHelper.initialize(this);
    }

    private void feedDatabase() {
        database.reset();
        database.removeAllRdvs();

        Rdv rdv1 = new Rdv(-1, "banque", "récupérer chéquier", "2021-03-28 20:00",
            false, "formosa", "", "", "2021-03-29 18:30");
        Rdv rdv2 = new Rdv(-1, "éducatrice", "agility", "2021-03-27 10:00",
            false, "séverine", "", "", "2021-03-27 09:30");
        Rdv rdv3 = new Rdv(-1, "dentiste", "dents de sagesses", "2021-05-14 09:30",
            false, "lemesre", "", "", "2021-05-13 19:30");

        database.addRdv(rdv1);
        database.addRdv(rdv2);
        database.addRdv(rdv3);

        database.removeRdv(rdv3.getId());

        rdv1.setDone(true);
        database.updateRdv(rdv1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rdv_manager_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_celebrity:{
                Intent vIntent = new Intent(this, RdvManagerDetailsActivity.class );
                vIntent.putExtra("fromAdd",true);
                this.startActivity(vIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}