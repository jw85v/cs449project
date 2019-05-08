package com.example.john.errandagent.Models;

import com.example.john.errandagent.Models.ShoppingListDTO;

import java.util.List;

public class ShoppingListCollection {

    private List<ShoppingListDTO> shoppingListDTOS;
    private Boolean Deleted = false;
    private String Name;

    public String getName(){
        return Name;
    }

    public void setName(String name){
        Name = name;
    }

    public List<ShoppingListDTO> getShoppingList(){
        return shoppingListDTOS;
    }

    public void setShoppingList(List<ShoppingListDTO> shoppingList){
        shoppingListDTOS = shoppingList;
    }

    public Boolean getDeleted(){
        return Deleted;
    }

    public void setDeleted(Boolean deleted){
        Deleted = deleted;
    }
}
