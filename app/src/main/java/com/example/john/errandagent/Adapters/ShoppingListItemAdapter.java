package com.example.john.errandagent.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.john.errandagent.Models.ShoppingListCollection;
import com.example.john.errandagent.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingListItemAdapter extends RecyclerView.Adapter<ShoppingListItemAdapter.ViewHolder> {

    private ShoppingListCollection mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private Map<String, ?> jsonPreferences;
    private String currentUser;


    // data is passed into the constructor
    public ShoppingListItemAdapter(Context context, ItemClickListener mClickListener, Map<String, ?> jsonPref, String user) {
        this.mInflater = LayoutInflater.from(context);
        this.jsonPreferences = jsonPref;
        this.mClickListener = mClickListener;
        this.context = context;
        this.currentUser = user;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List keys = new ArrayList(jsonPreferences.keySet());
        String[] listInfo = keys.get(position).toString().split("SSTTOOPPSS|IITTEEMMSS|TTOOTTAALLSS|UUSSEERR");
        if(listInfo.length >= 5){
        if(listInfo[4].equals(currentUser)) {
            holder.myTextView.setText(" ✓ "+listInfo[0]);
        }
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return jsonPreferences.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ItemClickListener itemClickListener;

        ViewHolder(View itemView, ItemClickListener itemClickListener) {
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
    private Object getItem(int id) {
        return jsonPreferences.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}