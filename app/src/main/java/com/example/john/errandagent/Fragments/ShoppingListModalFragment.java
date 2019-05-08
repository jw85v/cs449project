package com.example.john.errandagent.Fragments;

import android.content.Context;
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

import com.example.john.errandagent.Adapters.ModalListAdapter;
import com.example.john.errandagent.Adapters.ShoppingListItemAdapter;
import com.example.john.errandagent.Queries.GetUserInformation;
import com.example.john.errandagent.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingListModalFragment extends Fragment implements ModalListAdapter.ItemClickListener {

    private Map<String, ?> jsonPreferences;
    private String fileName = "";
    private String fullFileName = "";
    private String user;
    private Bundle args;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fullFileName = this.getArguments().getString("listKey");
        return inflater.inflate(R.layout.fragment_shopping_list_modal, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        GetUserInformation getUserInformation = new GetUserInformation();
        user = getUserInformation.getUser(getContext());


        setAdapter();

        args = new Bundle();
        args.putString("listKey","");
        final EditItineraryFragment fragment = new EditItineraryFragment();
        fragment.setArguments(args);


        Button closeModalBtn = getView().findViewById(R.id.closeModalBtn);
        closeModalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragment().getChildFragmentManager().beginTransaction().remove(new ShoppingListModalFragment()).add(R.id.fragment_itinerary_new_container, fragment).commit();
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
        adapter = new ModalListAdapter(getContext(), this, jsonPreferences);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

        List keys = new ArrayList(jsonPreferences.keySet());
        String[] listInfo = keys.get(position).toString().split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS|UUSSEERR");
        fileName = keys.get(position).toString();
        args.putString("listKey", fileName + "ssppaaccee" + fullFileName);
        final EditItineraryFragment fragment = new EditItineraryFragment();
        fragment.setArguments(args);
        getParentFragment().getChildFragmentManager().beginTransaction().remove(new ShoppingListModalFragment()).add(R.id.fragment_itinerary_new_container, fragment).commit();

    }
}

