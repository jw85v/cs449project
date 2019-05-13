package com.example.john.errandagent.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.john.errandagent.DataPersistence.SaveUserInformation;
import com.example.john.errandagent.Models.ShoppingListCollection;
import com.example.john.errandagent.Models.ShoppingListDTO;
import com.example.john.errandagent.Adapters.ShoppingListAdapter;
import com.example.john.errandagent.Queries.GetUserInformation;
import com.example.john.errandagent.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewShoppingListFragment extends Fragment implements ShoppingListAdapter.ItemClickListener {

    private EditText listName;
    private EditText item;
    private EditText price;
    private EditText store;
    private EditText qty;
    private TextView subTotal;
    private TextView taxTotal;
    private TextView grandTotal;
    private int listIndex;
    private String filename = "";
    private List<ShoppingListDTO> shoppingListDTOS = new ArrayList<>();
    private ShoppingListCollection shoppingList = new ShoppingListCollection();
    private String user;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        filename = this.getArguments().getString("listKey");
        return inflater.inflate(R.layout.fragment_new_shopping_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listName = getView().findViewById(R.id.listShoppingNameFrag);
        item = getView().findViewById(R.id.item);
        price = getView().findViewById(R.id.price);
        store = getView().findViewById(R.id.store);
        qty = getView().findViewById(R.id.qty);
        subTotal = getView().findViewById(R.id.subTotal);
        taxTotal = getView().findViewById(R.id.taxTotal);
        grandTotal = getView().findViewById(R.id.grandTotal);
        subTotal.append(" $0.00");
        taxTotal.append(" $0.00");
        grandTotal.append(" $0.00");
        listIndex = 999999;
        listName.requestFocus();

        if (!filename.equals("")) {
            loadFile();
        }

        GetUserInformation getUserInformation = new GetUserInformation();
        user = getUserInformation.getUser(getContext());

        setAdapter();

        BootstrapButton newItemBtn = getView().findViewById(R.id.newItemBtn);
        newItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.getText()) && !TextUtils.isEmpty(price.getText()) && !TextUtils.isEmpty(qty.getText()) && !TextUtils.isEmpty(store.getText())) {
                    createItem();
                    setAdapter();
                    clearFields();
                    calculateTotals();
                } else {
                    AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                            .setTitle("Please fill out all fields!")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });

        BootstrapButton editItemBtn = getView().findViewById(R.id.editItemBtn);
        editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.getText()) && !TextUtils.isEmpty(price.getText()) && !TextUtils.isEmpty(qty.getText()) && !TextUtils.isEmpty(store.getText())) {
                    ShoppingListDTO shoppingListDTO = shoppingListDTOS.get(listIndex);
                    shoppingListDTO.SetItem(item.getText().toString());
                    shoppingListDTO.SetPrice(Double.parseDouble(price.getText().toString()) * Integer.parseInt(qty.getText().toString()));
                    shoppingListDTO.SetQty(Integer.parseInt(qty.getText().toString()));
                    shoppingListDTO.SetStore(store.getText().toString());
                    setAdapter();
                    calculateTotals();
                } else {
                    AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                            .setTitle("Please fill out all fields!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });

        BootstrapButton clearFieldsBtn = getView().findViewById(R.id.clearFieldsBtn);
        clearFieldsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });

        BootstrapButton deleteItemBtn = getView().findViewById(R.id.deleteItemBtn);
        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listIndex != 999999)
                    shoppingListDTOS.remove(listIndex);
                listIndex = 999999;
                setAdapter();
                calculateTotals();
            }
        });

        BootstrapButton saveListBtn = getView().findViewById(R.id.saveListBtn);
        saveListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(listName.getText())) {
                    AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                            .setTitle("List saved!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    shoppingList.setDeleted(false);
                    saveShoppingList();
                } else {
                    AlertDialog show = new AlertDialog.Builder(getContext(), R.style.AlertThemeCustom)
                            .setTitle("You forgot to give your shopping list a name!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });

        BootstrapButton deleteBtn = getView().findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
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
                                shoppingListDTOS.clear();
                                clearFields();
                                calculateTotals();
                                setAdapter();

                            }
                        }).show();
            }
        });

    }

    private void loadFile() {
        GetUserInformation getUserInformation = new GetUserInformation();
        shoppingList = getUserInformation.getDetailedShoppingList(getContext(),filename);
        shoppingListDTOS = shoppingList.getShoppingList();
        String[] listInfo = filename.split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS");
        listName.setText(listInfo[0]);
    }
    //add integer to each interary dto item that will be associated with a list

    private void saveShoppingList() {
        shoppingList.setName(listName.getText().toString());
        shoppingList.setShoppingList(shoppingListDTOS);
        SaveUserInformation saveUserInformation = new SaveUserInformation();
        saveUserInformation.saveShoppingList(getContext(),filename,shoppingList,String.valueOf(getStops()), String.valueOf(shoppingListDTOS.size()), String.valueOf(calculateTotals()), user);

    }

    private int getStops() {
        String[] stores = new String[shoppingListDTOS.size()];
        for (int i = 0; i < shoppingListDTOS.size(); i++) {
            stores[i] = shoppingListDTOS.get(i).GetStore();
        }
        Set<String> uniqueValue = new HashSet<>(Arrays.asList(stores));
        return uniqueValue.size();
    }

    private void createItem() {
        ShoppingListDTO shoppingListDTO = new ShoppingListDTO();
        shoppingListDTO.SetItem(item.getText().toString());
        shoppingListDTO.SetPrice(Math.floor(Double.parseDouble(price.getText().toString()) * Integer.parseInt(qty.getText().toString()) * 100) / 100);
        shoppingListDTO.SetQty(Integer.parseInt(qty.getText().toString()));
        shoppingListDTO.SetStore(store.getText().toString());
        shoppingListDTO.SetListName(listName.getText().toString());
        shoppingListDTOS.add(shoppingListDTO);
    }

    private double calculateTotals() {
        double sub = 0;
        double tax = 0;
        double grand = 0;
        for (ShoppingListDTO items : shoppingListDTOS) {
            sub += items.GetPrice();
        }
        sub = Math.floor(sub * 100) / 100;
        tax = Math.floor(sub * .085 * 100) / 100;
        grand = Math.floor((tax + sub) * 100) / 100;
        if (sub == 0.0) {
            subTotal.setText("SubTotal: $0.00");
            taxTotal.setText("Estimated Tax: $0.00");
            grandTotal.setText("Total: $0.00");
        } else {
            subTotal.setText("SubTotal: $" + String.valueOf(sub));
            taxTotal.setText("Estimated Tax: $" + String.valueOf(tax));
            grandTotal.setText("Total: $" + String.valueOf(grand));
        }
        return grand;
    }

    private void clearFields() {
        item.setText("");
        price.setText("");
        qty.setText("");
        store.setText("");
    }

    public void setAdapter() {
        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = getView().findViewById(R.id.shoppingItemRecycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShoppingListAdapter(getContext(), shoppingListDTOS, this);
        recyclerView.setAdapter(adapter);
        calculateTotals();
    }

    @Override
    public void onItemClick(View view, int position) {
        ShoppingListDTO shoppingListDTO = shoppingListDTOS.get(position);
        item.setText(shoppingListDTO.GetItem());
        price.setText(String.valueOf(shoppingListDTO.GetPrice() / shoppingListDTO.GetQty()));
        qty.setText(String.valueOf(shoppingListDTO.GetQty()));
        store.setText(String.valueOf(shoppingListDTO.GetStore()));
        listIndex = position;
        setAdapter();
    }
}
