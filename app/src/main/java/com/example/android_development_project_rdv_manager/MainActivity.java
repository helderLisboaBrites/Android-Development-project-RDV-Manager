package com.example.android_development_project_rdv_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper database;
    private ListView lvRdvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvRdvs = (ListView)findViewById(R.id.RDVManagerListView) ;

        database = new DatabaseHelper(this);
        database.open();

        database.removeAllRdvs();

        Rdv rdv1 = new Rdv("banque", "récupérer chéquier", "2021-03-29 18:30:00");
        Rdv rdv2 = new Rdv("éducatrice", "agility", "2021-03-27 10:00:00");
        Rdv rdv3 = new Rdv("dentiste", "dents de sagesses", "2021-05-14 09:30:00");

        database.addRdv(rdv1);
        database.addRdv(rdv2);
        database.addRdv(rdv3);

        database.removeRdv(rdv3.getId());

        rdv1.setDone(true);
        database.updateRdv(rdv1);

        loadData();
    }

    public void loadData() {
        final String[] from = new String[]{
            DatabaseHelper._ID,
            DatabaseHelper.TITLE,
            DatabaseHelper.DESCRIPTION,
            DatabaseHelper.DATE,
            DatabaseHelper.DONE,
        };

        final int[] to = new int[]{
            R.id.tv_id,
            R.id.tv_titre,
            R.id.tv_description,
            R.id.tv_date,
            R.id.tv_done,
        };

        Cursor cursor = database.getAllRdvs();

        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this, R.layout.custom_list_rdv, cursor, from, to, 0);

        adapter.notifyDataSetChanged();

        lvRdvs.setAdapter(adapter);
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
                startActivity(vIntent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}