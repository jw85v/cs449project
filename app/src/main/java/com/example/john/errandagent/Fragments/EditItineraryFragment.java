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

import com.example.john.errandagent.Adapters.ItineraryEditAdapter;
import com.example.john.errandagent.DataPersistence.SaveUserInformation;
import com.example.john.errandagent.Models.ItineraryCollection;
import com.example.john.errandagent.Models.ItineraryDTO;
import com.example.john.errandagent.Models.ShoppingListCollection;
import com.example.john.errandagent.Models.ShoppingListDTO;
import com.example.john.errandagent.Queries.GetUserInformation;
import com.example.john.errandagent.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class EditItineraryFragment extends Fragment implements ItineraryEditAdapter.ItemClickListener {

    private String fileName[];
    private String user = "";
    private EditText location;
    private EditText itineraryName;
    private ItineraryDTO itinerary;
    private ItineraryCollection itineraryCollection;
    private EditText start;
    private EditText end;
    private Map<String, List<ShoppingListDTO>> map = new HashMap<String, List<ShoppingListDTO>>();
    private Bundle args;
    private String shopFileName = "";
    private EditText date;
    private int clickIndex;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fileName = this.getArguments().getString("listKey","").split("ssppaaccee");
        return inflater.inflate(R.layout.fragment_edit_itinerary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        location = getView().findViewById(R.id.itineraryLocation);
        itineraryCollection = new ItineraryCollection();
        itineraryCollection.itineraryList = new ArrayList<ItineraryDTO>();

        clickIndex = 0;


        GetUserInformation getUserInformation = new GetUserInformation();
        user = getUserInformation.getUser(getContext());

        itineraryName = getView().findViewById(R.id.ItineraryName);
        itinerary = new ItineraryDTO();

        date = getView().findViewById(R.id.itineraryDate);

        start = getView().findViewById(R.id.startTime);
        end = getView().findViewById(R.id.endTime);

        if(!fileName.equals("")){
            loadLists();
            setAdapter();
        }

        String files = "";
        for(String file:fileName) {
         files += "ssppaaccee" + file;
        }


        Button addListBtn = getView().findViewById(R.id.editListItineraryBtn);
        args = new Bundle();
        args.putString("listKey",files);
        final ShoppingListModalFragment fragment = new ShoppingListModalFragment();
        fragment.setArguments(args);

        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().remove(new EditItineraryFragment()).add(R.id.fragment_itinerary_slist_container, fragment).commit();
            }
        });

        Button empty = getView().findViewById(R.id.emptyItineraryBtn);
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                        .setTitle("Are you sure you want to empty this list?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itineraryCollection = new ItineraryCollection();
                                itineraryCollection.itineraryList = new ArrayList<>();
                                setAdapter();

                            }
                        }).show();
            }
        });

        Button save = getView().findViewById(R.id.saveItineraryBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItinerary();
                AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                        .setTitle("Itinerary Saved!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                setAdapter();

            }
        });

        Button addStop = getView().findViewById(R.id.addStopBtn);
        addStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItineraryDTO itineraryDTO = new ItineraryDTO();
                itineraryDTO.setStartTime(start.getText().toString());
                itineraryDTO.setEndTime(end.getText().toString());
                itineraryDTO.setName(location.getText().toString());
                List<ShoppingListDTO> groupedByStore = new ArrayList<>();
                ShoppingListDTO shoppingListDTO = new ShoppingListDTO();
                shoppingListDTO.SetStore(location.getText().toString());
                groupedByStore.add(shoppingListDTO);

                ShoppingListCollection collection = new ShoppingListCollection();
                collection.setShoppingList(groupedByStore);
                itineraryDTO.setShoppingList(collection);
                itineraryCollection.itineraryList.add(itineraryDTO);
                setAdapter();

            }
        });

        Button delItem = getView().findViewById(R.id.deleteItineraryItem);
        delItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                        .setTitle("Are you sure you want to this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itineraryCollection.itineraryList.remove(clickIndex);
                                setAdapter();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        Button openList = getView().findViewById(R.id.openShoppingListBtn);
        openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args2 = new Bundle();
                String[] fileNames = shopFileName.split("ssppaaccee");
                args2.putString("listKey",fileNames[0]);
                final NewShoppingListFragment fragment = new NewShoppingListFragment();
                fragment.setArguments(args2);
                getChildFragmentManager().beginTransaction().remove(new EditItineraryFragment()).add(R.id.fragment_itinerary_new_container, fragment).commit();


            }
        });


        super.onActivityCreated(savedInstanceState);
    }

    private void loadItinerary() {
        if(!fileName[fileName.length-1].equals("")) {
            String name[] = fileName[0].split("UUSSEERR");
            ItineraryCollection tempCollection = new ItineraryCollection();
            GetUserInformation getUserInformation = new GetUserInformation();
            tempCollection = getUserInformation.getDetailedItinerary(getContext(), fileName[fileName.length-1]);
            itineraryName.setText(name[0]);
            for (ItineraryDTO i : tempCollection.itineraryList) {
                itineraryCollection.itineraryList.add(i);
            }
        }
    }

    private void saveItinerary() {
        itinerary.setStartTime(start.getText().toString());
        itinerary.setEndTime(end.getText().toString());
        itinerary.setName(itineraryName.getText().toString());
        itinerary.setDate(date.getText().toString());
        itineraryCollection.setDate(date.getText().toString());
        SaveUserInformation saveUserInformation = new SaveUserInformation();
        saveUserInformation.saveItineraryList(getContext(), fileName[0], itineraryCollection, itinerary.getName(), user);
    }

    private void loadLists() {
        List<List<ShoppingListDTO>> s = new ArrayList<>();

        if(!fileName[0].equals("")) {
            for(String file:fileName) {
                file =file.replace("ssppaaccee","");
                String[] listInfo = file.split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS|UUSSEERR");
                GetUserInformation getUserInformation = new GetUserInformation();
                if ((listInfo.length > 2)) {
                    ShoppingListCollection shoppingListCollection = getUserInformation.getDetailedShoppingList(getContext(), file);
                    itinerary.setShoppingList(shoppingListCollection);
                    List<ShoppingListDTO> shoppingListDTOS = shoppingListCollection.getShoppingList();
                    shoppingListDTOS.get(0).SetListName(file);
                    s.add(shoppingListDTOS);
                    itinerary.setFileName(file);
                    //buildItinerary(shoppingListDTOS);
                } else {
                  if(!file.equals("")) {
                        String name[] = file.split("UUSSEERR");
                        ItineraryCollection tempCollection = new ItineraryCollection();
                        tempCollection = getUserInformation.getDetailedItinerary(getContext(), file);
                        date.setText(tempCollection.date);
                        itineraryName.setText(name[0]);
                      itinerary.setFileName("No SFile");
                        for(ItineraryDTO i: tempCollection.itineraryList){
                            itineraryCollection.itineraryList.add(i);
                       }
                    }
                }
                }
                buildItinerary(s);

            }
        }



    private void buildItinerary(@NonNull List<List<ShoppingListDTO>> shoppinglistCollection) {
        //itineraryCollection.itineraryList = new ArrayList<>();

        for (List<ShoppingListDTO> s : shoppinglistCollection) {

            for (ShoppingListDTO shoppingList : s) {
                String key = shoppingList.GetStore();
                if (map.get(key) == null) {
                    map.put(key, new ArrayList<ShoppingListDTO>());
                }
                map.get(key).add(shoppingList);
            }

            List<String> keys = new ArrayList<String>(map.keySet());
            for (String key : keys) {
                ItineraryDTO itineraryDTO = new ItineraryDTO();
                List<ShoppingListDTO> groupedByStore = map.get(key);
                ShoppingListCollection collection = new ShoppingListCollection();
                collection.setShoppingList(groupedByStore);
                itineraryDTO.setShoppingList(collection);
                itineraryDTO.setName(key);
                itineraryDTO.setFileName(s.get(0).GetListName());
                itineraryDTO.setDate(date.getText().toString());
                itineraryCollection.itineraryList.add(itineraryDTO);
            }
        }
    }

    public void setAdapter(){
        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = getView().findViewById(R.id.itineraryItemRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //yourList = new ArrayList<Blog>(new LinkedHashSet<Blog>(yourList));
        //itineraryCollection.itineraryList = new ArrayList<ItineraryDTO>(new LinkedHashSet<ItineraryDTO>(itineraryCollection.itineraryList));
        adapter = new ItineraryEditAdapter(getContext(), itineraryCollection, this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
        itinerary = itineraryCollection.itineraryList.get(position);
        start.setText(itinerary.getStartTime());
        end.setText(itinerary.getEndTime());
        shopFileName = itineraryCollection.itineraryList.get(position).getFileName();
        location.setText(itinerary.getShoppingList().getShoppingList().get(0).GetStore());
        clickIndex = position;
        setAdapter();
    }
}
