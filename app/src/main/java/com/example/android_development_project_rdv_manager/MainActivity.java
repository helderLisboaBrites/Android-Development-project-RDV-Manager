package com.example.android_development_project_rdv_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        database.open();

        database.removeAllRdvs();

        Rdv rdv1 = new Rdv("banque", "récupérer chéquier", "2021-03-29 18:30:00");
        Rdv rdv2 = new Rdv("éducatrice", "agility", "2021-03-27 10:00:00");
        Rdv rdv3 = new Rdv("dentiste", "dents de sagesses", "2021-05-14 09:30:00");

        database.addRdv(rdv1);
        database.addRdv(rdv2);
        database.addRdv(rdv3);

        database.removeRdv(rdv1.getId());

        rdv3.setDone(true);
        database.updateRdv(rdv3);
    }
}