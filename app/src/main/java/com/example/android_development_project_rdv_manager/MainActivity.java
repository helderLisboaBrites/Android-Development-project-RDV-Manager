package com.example.android_development_project_rdv_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView lvRDV = (ListView) findViewById(R.id.RDVManagerListView);
        lvRDV.setEmptyView(findViewById(R.id.tvEmpty));
        registerForContextMenu(lvRDV);
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