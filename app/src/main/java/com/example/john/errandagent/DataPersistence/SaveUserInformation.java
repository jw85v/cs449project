package com.example.john.errandagent.DataPersistence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.john.errandagent.Activities.CreateAccountActivity;
import com.example.john.errandagent.Activities.LoginActivity;
import com.example.john.errandagent.Models.ItineraryCollection;
import com.example.john.errandagent.Models.ShoppingListCollection;
import com.google.gson.Gson;

public class SaveUserInformation {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();
    private String jsonCurProduct;

    public boolean createUser(Context context, String accountName, String accountPass){
        sharedPref = context.getSharedPreferences("AccountPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String nameTaken = sharedPref.getString(accountName, "New");
        if(nameTaken.equals("New")){
            editor.putString(accountName, accountPass);
            editor.apply();
            return true;
        }
        else{
            return false;
        }
    }

    public void saveShoppingList(Context context, String fileName, ShoppingListCollection shoppingList, String stops, String size, String calculatedTotals, String user){

        sharedPref = context.getSharedPreferences("ShoppingListPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (!fileName.equals("")) {
            editor.remove(fileName);
        }

        jsonCurProduct = gson.toJson(shoppingList);
        editor.putString(shoppingList.getName() + "SSTTOOPPSS" + stops + "IITTEEMMSS" + size + "TTOOTTAALLSS" + calculatedTotals + "UUSSEERR" + user, jsonCurProduct);
        editor.apply();

    }

    public void saveItineraryList(Context context, String fileName, ItineraryCollection itineraryCollection, String name, String user){

        sharedPref = context.getSharedPreferences("ItineraryListPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if(!fileName.equals("")){
            editor.remove(fileName);
        }
        String jsonCurProduct = gson.toJson(itineraryCollection);
        editor.putString(name + "UUSSEERR" +user, jsonCurProduct);
        editor.apply();
    }

}
