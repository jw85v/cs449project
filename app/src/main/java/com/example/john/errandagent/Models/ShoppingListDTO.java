package com.example.john.errandagent.Models;

import android.media.SubtitleData;

public class ShoppingListDTO {

    private String Item;
    private double Price;
    private int Qty;
    private String Store;
    private String ListName;

    public String GetItem(){
        return Item;
    }

    public void SetItem(String item){
        Item = item;
    }

    public double GetPrice(){
        return Price;
    }

    public void SetPrice(double price){
        Price = price;
    }


    public int GetQty(){
        return Qty;
    }

    public void SetQty(int qty){
        Qty = qty;
    }

    public String GetStore(){
        return Store;
    }

    public void SetStore(String store){
        Store = store;
    }

    public void SetListName(String name){
        ListName = name;
    }

    public String GetListName(){
        return ListName;
    }



}
