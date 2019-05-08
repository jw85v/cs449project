package com.example.john.errandagent.Queries;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.john.errandagent.Models.ItineraryCollection;
import com.example.john.errandagent.Models.ShoppingListCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetUserInformation {

    private SharedPreferences sharedPref;
    private Map<String, ?> jsonPreferences;
    private Gson gson = new Gson();

    public String getUser(Context context){
        sharedPref = context.getSharedPreferences("AccountPref", Context.MODE_PRIVATE);
        return sharedPref.getString("CurrentUser","");
    }

    public Map<String, ?> getUserShoppingLists(Context context, String user){
        sharedPref = context.getSharedPreferences("ShoppingListPref", Context.MODE_PRIVATE);
        jsonPreferences = sharedPref.getAll();
        List keys = new ArrayList(jsonPreferences.keySet());
        List<Integer> deleteKeys = new ArrayList<Integer>();
        for (int i = 0; i < jsonPreferences.size(); i++) {
            String[] listInfo = keys.get(i).toString().split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS|UUSSEERR");
            if (listInfo.length >= 5) {
                if (!listInfo[4].equals(user)) {
                    deleteKeys.add(i);
                }

            }
            else
                deleteKeys.add(i);

        }
        for(Integer index: deleteKeys){
            jsonPreferences.remove(keys.get(index));
        }

        return jsonPreferences;
    }

    public ShoppingListCollection getDetailedShoppingList(Context context, String fileName){
        sharedPref = context.getSharedPreferences("ShoppingListPref", Context.MODE_PRIVATE);
        String json = sharedPref.getString(fileName, "");
        Type type = new TypeToken<ShoppingListCollection>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ItineraryCollection getDetailedItinerary(Context context, String fileName){
        String name[] = fileName.split("UUSSEERR");
        sharedPref = context.getSharedPreferences("ItineraryListPref", Context.MODE_PRIVATE);
        String json = sharedPref.getString(fileName, "");
        Type type = new TypeToken<ItineraryCollection>() {
        }.getType();
        return gson.fromJson(json, type);
    }


}
