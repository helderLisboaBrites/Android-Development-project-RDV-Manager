package com.example.android_development_project_rdv_manager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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

        getListView().setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                return onListItemLongClick(adapterView, view, position, id);
            }
        });
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View view, int position, long id) {
        super.onListItemClick(l, view, position, id);

        Rdv rdv = database.getRdv(rdvIds.get(position));

        RdvManagerDetailsFragment fragment = (RdvManagerDetailsFragment)getFragmentManager().findFragmentById(R.id.detailFragment);

        if(fragment != null && fragment.isInLayout()) {
            Toast.makeText(getActivity(), "fragment " + rdv.getTitle(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "new activity " + rdv.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onListItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Rdv rdv = database.getRdv(rdvIds.get(position));
        Toast.makeText(getActivity(), rdv.getTitle() + " (delete share)", Toast.LENGTH_SHORT).show();

        return true;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] from = new String[]{
            DatabaseHelper.TITLE,
            DatabaseHelper.DESCRIPTION,
            DatabaseHelper.DATE,
            DatabaseHelper.DONE,
        };

        final int[] to = new int[]{
            R.id.tv_titre,
            R.id.tv_description,
            R.id.tv_date,
            R.id.tv_done,
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
}
