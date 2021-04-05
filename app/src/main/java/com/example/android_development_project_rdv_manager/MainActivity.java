package com.example.android_development_project_rdv_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        database.open();


        feedDatabase();
    }

    private void feedDatabase() {
        database.reset();
        database.removeAllRdvs();

        Rdv rdv1 = new Rdv("banque", "récupérer chéquier", "2021-03-29 18:30:00", "formosa");
        Rdv rdv2 = new Rdv("éducatrice", "agility", "2021-03-27 10:00:00", "séverine");
        Rdv rdv3 = new Rdv("dentiste", "dents de sagesses", "2021-05-14 09:30:00", "lemesre");

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
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}