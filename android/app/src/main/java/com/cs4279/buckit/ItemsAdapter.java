package com.cs4279.buckit;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by yunhuazhao on 10/30/16.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private ArrayList<Item> itemsList;
    private ArrayList<CardClickListener> cardClickListeners;

    private ArrayList<Item> mergedList;
    private HashMap<String, String> uidToUsernameMap;

    private StorageReference storageReference;

    // Constructor for purely item cards
    public ItemsAdapter(ArrayList<Item> itemsList, ArrayList<CardClickListener> cardClickListeners) {
        this.itemsList = itemsList;
        this.mergedList = itemsList;
        this.cardClickListeners = cardClickListeners;

        this.storageReference = FirebaseStorage.getInstance().getReference();
    }

    // Constructor for public feed with item cards and activity cards
    public ItemsAdapter(ArrayList<Item> itemsList, ArrayList<Item> mergedList, HashMap<String, String> uidToUsernameMap, ArrayList<CardClickListener> cardClickListeners) {
        this.itemsList = itemsList;
        this.mergedList = mergedList;
        this.uidToUsernameMap = uidToUsernameMap;
        this.cardClickListeners = cardClickListeners;

        this.storageReference = FirebaseStorage.getInstance().getReference();
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
    public void onBindViewHolder(final ItemsViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item item = mergedList.get(position);

        if (item.getHasPhoto()) {
            holder.imageView.setVisibility(View.VISIBLE);
            GlideApp.with(holder.imageView.getContext())
                    .load(storageReference.child("AnvitGupta/" + item.getKey() + ".jpg"))
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ViewImageActivity.class);
                    i.putExtra("key", item.getKey());
                    v.getContext().startActivity(i);
                }
            });
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        holder.creator.setText("Created by: " + item.getOriginalCreator());
        if (item.isActivity()) {
            holder.poster.setVisibility(View.VISIBLE);
            holder.poster.setText(uidToUsernameMap.get(item.getPostCreator()) + " completed a BuckIt item!");
            holder.title.setText(item.getTitle());
            // TODO: Add the ability to add a photo to these posts
            holder.markAsDoneButton.setVisibility(View.GONE);
            holder.addToBuckItButton.setVisibility(View.GONE);

            holder.likeButton.setVisibility(View.VISIBLE);
            if (item.isLiked()) {
                holder.likeButton.setAlpha(0.5f);
                holder.likeButton.setText("Unlike");
            } else {
                holder.likeButton.setAlpha(1.0f);
                holder.likeButton.setText("Like");
            }
            cardClickListeners.get(2 * position).setCardID(item.getKey());
            cardClickListeners.get(2 * position).setButtonType(CardClickListener.LIKE);
            holder.likeButton.setOnClickListener(cardClickListeners.get(2 * position));

            //holder.likeButton.setOnClickListener(cardClickListeners.get(2 * position));
        } else {
            holder.title.setText(item.getTitle());
            holder.description.setText(item.getDescription());
            if (item.getCompleted()) {
                holder.markAsDoneButton.setEnabled(false);
                holder.markAsDoneButton.setAlpha(0.5f);
            }
            if (item.isInPersonalList()) {
                holder.poster.setVisibility(View.GONE);
                holder.markAsDoneButton.setVisibility(View.VISIBLE);
                holder.addToBuckItButton.setVisibility(View.GONE);
                holder.likeButton.setVisibility(View.GONE);
                cardClickListeners.get(2 * position).setCardID(item.getKey());
                cardClickListeners.get(2 * position).setButtonType(CardClickListener.MARK_AS_DONE);
                holder.markAsDoneButton.setOnClickListener(cardClickListeners.get(2 * position));
            } else {
                holder.poster.setVisibility(View.VISIBLE);
                holder.poster.setText(uidToUsernameMap.get(item.getPostCreator()) + " added a new BuckIt item!");
                holder.addToBuckItButton.setVisibility(View.VISIBLE);

                holder.likeButton.setVisibility(View.VISIBLE);
                if (item.isLiked()) {
                    holder.likeButton.setAlpha(0.5f);
                    holder.likeButton.setText("Unlike");
                } else {
                    holder.likeButton.setAlpha(1.0f);
                    holder.likeButton.setText("Like");
                }
                cardClickListeners.get(2 * position + 1).setCardID(item.getKey());
                cardClickListeners.get(2 * position + 1).setButtonType(CardClickListener.LIKE);
                holder.likeButton.setOnClickListener(cardClickListeners.get(2 * position + 1));

                holder.markAsDoneButton.setVisibility(View.GONE);
                cardClickListeners.get(2 * position).setCardID(item.getKey());
                cardClickListeners.get(2 * position).setButtonType(CardClickListener.ADD_TO_BUCKIT);
                holder.addToBuckItButton.setOnClickListener(cardClickListeners.get(2 * position));
            }
        }

        holder.score.setText("Score: " + item.getScore());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mergedList.size();
    }

    // QuestionViewHolder
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        protected TextView poster;
        protected TextView title;
        protected TextView description;
        protected TextView creator;
        protected TextView score;
        protected Button addToBuckItButton;
        protected Button markAsDoneButton;
        protected Button likeButton;
        protected ImageView imageView;
        public ItemsViewHolder(View v) { // expects CardView?
            super(v);
            poster = v.findViewById(R.id.poster);
            title = v.findViewById(R.id.title);
            description = v.findViewById(R.id.description);
            creator = v.findViewById(R.id.creator);
            score = v.findViewById(R.id.score);
            addToBuckItButton = v.findViewById(R.id.addToBuckItButton);
            markAsDoneButton = v.findViewById(R.id.markAsDoneButton);
            likeButton = v.findViewById(R.id.likeButton);
            imageView = v.findViewById(R.id.imageView);
        }
    }
}