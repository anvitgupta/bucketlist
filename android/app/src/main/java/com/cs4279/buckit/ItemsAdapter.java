package com.cs4279.buckit;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by yunhuazhao on 10/30/16.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private ArrayList<Item> itemsList;
    private ArrayList<CardClickListener> cardClickListeners;

    // Constructor for purely item cards
    public ItemsAdapter(ArrayList<Item> itemsList, ArrayList<CardClickListener> cardClickListeners) {
        this.itemsList = itemsList;
        this.cardClickListeners = cardClickListeners;
    }

    // Constructor for public feed with item cards and activity cards
    public ItemsAdapter(ArrayList<Item> itemsList, ArrayList<Item> completedItems, ArrayList<CardClickListener> cardClickListeners) {
        this.itemsList = itemsList;
        this.cardClickListeners = cardClickListeners;
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
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.creator.setText("Created by: " + item.getCreator());
        if (item.getCompleted()) {
            holder.markAsDoneButton.setEnabled(false);
            holder.markAsDoneButton.setAlpha(0.5f);
        }
        if (item.isInPersonalList()) {
            holder.markAsDoneButton.setVisibility(View.VISIBLE);
            holder.addToBuckItButton.setVisibility(View.GONE);
            cardClickListeners.get(position).setCardID(item.getKey());
            cardClickListeners.get(position).setButtonType(CardClickListener.MARK_AS_DONE);
            holder.markAsDoneButton.setOnClickListener(cardClickListeners.get(position));
        } else {
            holder.addToBuckItButton.setVisibility(View.VISIBLE);
            holder.markAsDoneButton.setVisibility(View.GONE);
            cardClickListeners.get(position).setCardID(item.getKey());
            cardClickListeners.get(position).setButtonType(CardClickListener.ADD_TO_BUCKIT);
            holder.addToBuckItButton.setOnClickListener(cardClickListeners.get(position));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // QuestionViewHolder
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView description;
        protected TextView creator;
        protected Button addToBuckItButton;
        protected Button markAsDoneButton;
        public ItemsViewHolder(View v) { // expects CardView?
            super(v);
            title = v.findViewById(R.id.title);
            description = v.findViewById(R.id.description);
            creator = v.findViewById(R.id.creator);
            addToBuckItButton = v.findViewById(R.id.addToBuckItButton);
            markAsDoneButton = v.findViewById(R.id.markAsDoneButton);
        }
    }
}