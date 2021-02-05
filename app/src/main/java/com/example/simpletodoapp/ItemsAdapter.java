package com.example.simpletodoapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// The adapter will populate each row layout (defined in XML) in the MainActivity with
// our data (model classes: Here Todo items)
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnClickListener clickListener;
    OnLongClickListener longClickListener;

    public ItemsAdapter(List<String> items, OnClickListener clickListener,
                                                OnLongClickListener longClickListener) {
        this.items = items;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    // Container to provide direct reference to views that represent each row of the list.
    // In other words, views that represent each Todo items.
    // Used to cache the views within the item layout for fast access.
    class ViewHolder extends RecyclerView.ViewHolder {

        // In Android, the view occupies a rectangular area on the screen and is
        // responsible for drawing and event handling
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // android.R.id.text1 is an identifier of a text view
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // Update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);

            tvItem.setOnClickListener(new View.OnClickListener() {
                // Notify the listener which item position was clicked on
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                // Notify the listener which item position was long clicked on
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    // Create a new view (Use layout inflater to inflate the view).
    // Wrap the view inside a ViewHolder and return it.
    // We are essentially telling the RecyclerView what kind of
    // layout we want to use for our data item (Todo items)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext()).inflate
                                                (android.R.layout.simple_list_item_1, parent,
                                                                            false);
        return new ViewHolder(todoView);
    }


    // Grab item at the specified position
    // and bind it to the specified view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.bind(item);
    }

    // GETTERS AND SETTERS

    // Tells the recycler view how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }
}
