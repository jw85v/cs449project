package com.example.john.errandagent.Models;

import java.util.ArrayList;
import java.util.List;

public class ItineraryCollection {

    public List<ItineraryDTO> itineraryList;
    public String Name;
    public String date;

    public List<ItineraryDTO> getItineraryList(){
        return itineraryList;
    }

    public void setItineraryList(List<ItineraryDTO> itineraryDTOS){
        itineraryList = itineraryDTOS;
    }

    public void setName(String name){
        Name = name;
    }

    public String getName(){
        return Name;
    }

    public void setDate(String d){date = d;}

    public String getDate(){return date;}
}
