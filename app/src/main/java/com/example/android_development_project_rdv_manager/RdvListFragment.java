package com.example.android_development_project_rdv_manager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

public class RdvListFragment extends ListFragment {

    DatabaseHelper database;

    ArrayList<Integer> rdvIds = new ArrayList<Integer>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new DatabaseHelper(getActivity());
        database.open();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View view, int position, long id) {
        super.onListItemClick(l, view, position, id);

        Rdv rdv = database.getRdv(rdvIds.get(position));

        RdvManagerDetailsFragment fragment = (RdvManagerDetailsFragment)getFragmentManager().findFragmentById(R.id.detailFragment);

        if(fragment != null && fragment.isInLayout()) {
             fragment.setRdv(rdv);
        }
        else {
            Intent vIntent = new Intent(getActivity(), RdvManagerDetailsActivity.class );
            vIntent.putExtra("fromAdd",false);
            vIntent.putExtra("rdv_saved",rdv);
            this.startActivity(vIntent);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // loadData();
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
         super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if (item.getItemId()==R.id.delete){
            database.removeRdv(info.id);
            loadData();
            return true;
        }
        else if (item.getItemId()==R.id.itemShare){
            Rdv rdv = database.getRdv(info.id);
            shareMethod(rdv);
            return true;
        }
        else if (item.getItemId()==R.id.itemGoogleMaps){
            TextView tv = (TextView)info.targetView.findViewById(R.id.tv_address);
            launchMaps(tv.getText().toString());
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void loadData() {
        final String[] from = new String[]{
            DatabaseHelper.TITLE,
            DatabaseHelper.DESCRIPTION,
            DatabaseHelper.DATE,
            DatabaseHelper.DONE,
            DatabaseHelper.CONTACT,
            DatabaseHelper.ADDRESS,
            DatabaseHelper.PHONE,
        };

        final int[] to = new int[]{
            R.id.tv_titre,
            R.id.tv_description,
            R.id.tv_date,
            R.id.tv_done,
            R.id.tv_contact,
            R.id.tv_address,
            R.id.tv_phone,
        };

        Cursor cursor = database.getAllRdvs();

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            rdvIds.add(cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID))); //add the item
            cursor.moveToNext();
        }

        SimpleCursorAdapter adapter= new SimpleCursorAdapter(getActivity(), R.layout.custom_list_rdv, cursor, from, to, 0);

        adapter.notifyDataSetChanged();

        setListAdapter(adapter);
    }

    public void launchMaps(String address) {
        String map = "http://maps.google.co.in/maps?q=" +address ;
        Uri gmmIntentUri = Uri.parse(map);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }


    public void shareMethod(Rdv rdv) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, rdv.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share App")) ;
    }

}
