package com.cs4279.buckit;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yunhuazhao on 10/30/16.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private ArrayList<Item> itemsList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemsAdapter(ArrayList<Item> itemsList) {
        this.itemsList = itemsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cards, parent, false);
        ItemsViewHolder vh = new ItemsViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Item item = itemsList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.creator.setText("Created by: " + item.getCreator());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // QuestionViewHolder
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView description;
        protected TextView creator;
        public ItemsViewHolder(View v) { // expects CardView?
            super(v);
            name = v.findViewById(R.id.name);
            description = v.findViewById(R.id.description);
            creator = v.findViewById(R.id.creator);
        }
    }
}