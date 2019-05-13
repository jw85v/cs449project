package com.example.john.errandagent.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.john.errandagent.Adapters.ItineraryListAdapter;
import com.example.john.errandagent.Adapters.ShoppingListItemAdapter;
import com.example.john.errandagent.Queries.GetUserInformation;
import com.example.john.errandagent.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItineraryFragment extends Fragment implements ItineraryListAdapter.ItemClickListener {

    private Bundle args;
    private Map<String, ?> jsonPreferences;
    private String user;
    private String filename = "";
    private EditText listName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itinerary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        args = new Bundle();
        listName = getView().findViewById(R.id.itineraryListName);
        listName.requestFocus();


        GetUserInformation getUserInformation = new GetUserInformation();
        user = getUserInformation.getUser(getContext());
        setAdapter();

        BootstrapButton deleteBtn = getView().findViewById(R.id.deleteItineraryBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                        .setTitle("Are you sure you want to delete this list?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPref = getContext().getSharedPreferences("ItineraryListPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove(filename);
                                editor.commit();
                                setAdapter();
                                listName.setText("");

                            }
                        }).show();
            }
        });

        BootstrapButton openOldBtn = getView().findViewById(R.id.openItineraryBtn);
        openOldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditItineraryFragment fragment = new EditItineraryFragment();
                fragment.setArguments(args);
                getChildFragmentManager().beginTransaction().remove(new ItineraryFragment()).add(R.id.fragment_itinerary_container, fragment).commit();

            }
        });

        BootstrapButton openItineraryBtn = getView().findViewById(R.id.newItineraryBtn);
        openItineraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("listKey","");
                final EditItineraryFragment fragment = new EditItineraryFragment();
                fragment.setArguments(args);
                getChildFragmentManager().beginTransaction().remove(new ItineraryFragment()).add(R.id.fragment_itinerary_container, fragment).commit();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    public void setAdapter() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("ItineraryListPref", Context.MODE_PRIVATE);
        jsonPreferences = sharedPref.getAll();
        List keys = new ArrayList(jsonPreferences.keySet());
        List<Integer> deleteKeys = new ArrayList<Integer>();
        for (int i = 0; i < jsonPreferences.size(); i++) {
            String[] listInfo = keys.get(i).toString().split("UUSSEERR");
            if (listInfo.length >= 2) {
                if (!listInfo[1].equals(user)) {
                    deleteKeys.add(i);
                }

            }
            else
                deleteKeys.add(i);

        }
        for(Integer index: deleteKeys){
            jsonPreferences.remove(keys.get(index));
        }

        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = getView().findViewById(R.id.itineraryRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItineraryListAdapter(getContext(), this, jsonPreferences);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
        List keys = new ArrayList(jsonPreferences.keySet());
        String[] listInfo = keys.get(position).toString().split("UUSSEERR");
        filename = keys.get(position).toString();
        listName.setText(listInfo[0]);
        args.putString("listKey", keys.get(position).toString());
    }
}
