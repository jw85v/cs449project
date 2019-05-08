package com.example.john.errandagent.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.errandagent.Models.ItineraryCollection;
import com.example.john.errandagent.Models.ItineraryDTO;
import com.example.john.errandagent.Models.ShoppingListDTO;
import com.example.john.errandagent.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.reverseOrder;

public class ItineraryEditAdapter extends RecyclerView.Adapter<ItineraryEditAdapter.ViewHolder> {
    private ItineraryCollection mData;
    private LayoutInflater mInflater;
    private ItineraryEditAdapter.ItemClickListener mClickListener;


    // data is passed into the constructor
    public ItineraryEditAdapter(Context context, ItineraryCollection data, ItemClickListener mClickListener) {
        this.mInflater = LayoutInflater.from(context);
        ArrayList<ItineraryDTO> Data = new ArrayList<>();
        mData = Duplicates(data);
        Collections.sort(data.itineraryList, new Sortbytime());
        this.mClickListener = mClickListener;
    }

    public ItineraryCollection Duplicates(ItineraryCollection mData){
        List<ItineraryDTO> removeList = new ArrayList<>();
//        for(ItineraryDTO dto: mData.itineraryList){
//            for(int i = 0; i <  mData.itineraryList.size(); i++){
//                if(dto.getName() == mData.itineraryList.get(i).getName()){
//                    removeList.add(i);
//                }
//
//            }
//        }

        for (int i = 0; i < mData.itineraryList.size(); i++) {
            for (int j = i+1; j <mData.itineraryList.size() ; j++) {
                if(mData.itineraryList.get(i).getName().equals(mData.itineraryList.get(j).getName()) && mData.itineraryList.get(i).getStartTime().equals(mData.itineraryList.get(j).getStartTime())){
                    removeList.add(mData.itineraryList.get(j));
                }
            }
        }

        for(ItineraryDTO dto:removeList){
            mData.itineraryList.remove(dto);
        }





        return mData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ItineraryEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ItineraryEditAdapter.ViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ItineraryEditAdapter.ViewHolder holder, int position) {
        ItineraryDTO shoppingList = mData.itineraryList.get(position);
        if(!shoppingList.getStartTime().equals("???") || !shoppingList.getEndTime().equals("???")) {
            holder.myTextView.setText(" - "+shoppingList.getStartTime() + "-" + shoppingList.getEndTime() + " Location: " + shoppingList.getShoppingList().getShoppingList().get(0).GetStore());
        }
        else
            holder.myTextView.setText(" - Set a time for this stop! " + " Location: " + shoppingList.getShoppingList().getShoppingList().get(0).GetStore());

    }
    // return c1.getName().compare(c2.getName());

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.itineraryList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ItineraryEditAdapter.ItemClickListener itemClickListener;

        ViewHolder(View itemView, ItineraryEditAdapter.ItemClickListener itemClickListener) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.shoppingItemView);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ItineraryDTO getItem(int id) {
        return mData.itineraryList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItineraryEditAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
class Sortbytime implements Comparator<ItineraryDTO>
{
    //-1 : o1 < o2
    //0 : o1 == o2
    //+1 : o1 > o2
    Collator collator = Collator.getInstance(Locale.US);
    // Used for sorting in ascending order of
    // roll number
    public int compare(ItineraryDTO a, ItineraryDTO b)
    {
        if(a.getStartTime().contains("???") || b.getStartTime().contains("???"))
            return 0;
        if(!a.getStartTime().contains(":") || !b.getStartTime().contains(":"))
            return Integer.parseInt(a.getStartTime().split(":")[0].split("am")[0].split("pm")[0]) - Integer.parseInt(b.getStartTime().split(":")[0].split("am")[0].split("pm")[0]);
        if(a.getStartTime().contains("am") && !b.getStartTime().contains("am"))
            return -1;
        if(!a.getStartTime().contains("am") && b.getStartTime().contains("am"))
            return 1;
        //return collator.compare(a.getStartTime().split(":")[0],b.getStartTime().split(":")[0]);
        return Integer.parseInt(a.getStartTime().split(":")[0]) - Integer.parseInt(b.getStartTime().split(":")[0]);
    }
}