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
import android.widget.TextView;

import com.example.john.errandagent.Models.ShoppingListDTO;
import com.example.john.errandagent.Queries.GetUserInformation;
import com.example.john.errandagent.R;
import com.example.john.errandagent.Adapters.ShoppingListItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingListFragment extends Fragment implements ShoppingListItemAdapter.ItemClickListener {

     private TextView stops;
     private TextView items;
     private TextView total;
     private Map<String, ?> jsonPreferences;
     private EditText listName;
     private Bundle args;
     private String filename = "";
     private String user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        listName = getView().findViewById(R.id.shoppingListName);
        stops = getView().findViewById(R.id.stops);
        items = getView().findViewById(R.id.numItems);
        total = getView().findViewById(R.id.totalTxtView);

        GetUserInformation getUserInformation = new GetUserInformation();
        user = getUserInformation.getUser(getContext());

        setAdapter();

        args = new Bundle();
        args.putString("listKey","");
        final NewShoppingListFragment fragment = new NewShoppingListFragment();
        fragment.setArguments(args);

        Button newListBtn = getView().findViewById(R.id.newListBtn);
        newListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("listKey","");
                getChildFragmentManager().beginTransaction().remove(new ShoppingListFragment()).add(R.id.fragment_shopping_container, fragment).commit();
            }
        });

        Button openListBtn = getView().findViewById(R.id.openBtn);
        openListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().remove(new ShoppingListFragment()).add(R.id.fragment_shopping_container,fragment).commit();
            }
        });

        Button deleteListBtn = getView().findViewById(R.id.deleteListBtn);
        deleteListBtn.setOnClickListener(new View.OnClickListener() {
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
                                SharedPreferences sharedPref = getContext().getSharedPreferences("ShoppingListPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.remove(filename);
                                editor.commit();
                                setAdapter();
                                listName.setText("");
                                stops.setText("# Stops:");
                                items.setText("# Items:");
                                total.setText("Total:");

                            }
                        }).show();
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    public void setAdapter() {
        GetUserInformation getUserInformation = new GetUserInformation();
        jsonPreferences = getUserInformation.getUserShoppingLists(getContext(), user);

        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = getView().findViewById(R.id.shoppingListRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShoppingListItemAdapter(getContext(), this, jsonPreferences, user);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        List keys = new ArrayList(jsonPreferences.keySet());
        String[] listInfo = keys.get(position).toString().split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS|UUSSEERR");

        filename = keys.get(position).toString();
        listName.setText(listInfo[0]);
        stops.setText("# Stops: " + listInfo[1]);
        items.setText("# Items: " + listInfo[2]);
        total.setText("Total: $" + listInfo[3]);
        args.putString("listKey", keys.get(position).toString());

    }

}
