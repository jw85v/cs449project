package com.example.john.errandagent.Models;

import java.util.Date;

public class ItineraryDTO {
    private Date itineraryDate;
    private String startTime = "???";
    private String endTime = "???";
    private ShoppingListCollection ShoppingList;
    private String name;
    private String fileName;
    private String date;

    public void setItineraryDate(Date date){
        itineraryDate =date;
    }

    public Date getItineraryDate(){
        return itineraryDate;
    }

    public void setStartTime(String startTime){
        this.startTime = startTime;
    }

    public String getStartTime(){
        return startTime;
    }

    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public void setShoppingList(ShoppingListCollection shoppingList) {
        ShoppingList = shoppingList;
    }

    public ShoppingListCollection getShoppingList(){
        return ShoppingList;
    }

    public void setFileName(String name){
        this.fileName =name;
    }

    public String getFileName(){
        return fileName;
    }

    public void setName(String name){
        this.name =name;
    }

    public String getName(){
        return name;
    }

    public void setDate(String d){date = d;}

    public String getDate(){return date;}


 }
