package com.example.simpletodoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

// NOTE: Items in the context of this project mean individual ToDo items
// Eg: {(Item 1: "Cook Food"), (Item 2: "Go tot the gym")} and so on

// You can consider activity as your mobile screen that you're currently on
public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    // VIEW
    RecyclerView rvItems;
    EditText etItem;
    Button btnAdd;

    // DATA
    List<String> items;

    // CONTROLLER ? (Confirm to check if true)
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Link the activity (screen) to the view (XML file) for this activity
        setContentView(R.layout.activity_main);

        // Set each variables by finding the corresponding views in the specified XML file
        rvItems = findViewById(R.id.rvItems);
        etItem = findViewById(R.id.etItem);
        btnAdd = findViewById(R.id.btnAdd);

        // Load list of Todo items from file
        loadItems();

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            // Create a new edit activity.
            // Pass the data being edited.
            // Display the activity
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single Click at position " + position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            // Remove the item.
            // Notify the adapter that the item was removed
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                saveItems();
                Toast.makeText(getApplicationContext(),
                                    "Item removed successfully", Toast.LENGTH_SHORT).show();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onClickListener, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        // LayoutManager tells the RecyclerView how to position items
        // and when to reuse item views
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Add user supplied item to list of items.
        // Clear the input text area.
        // Notify the adapter that the item has been added.
        // Display a popup (Toast) message letting the user
        // know that the item was successfully added
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.add(etItem.getText().toString());
                etItem.setText("");
                itemsAdapter.notifyItemInserted(items.size() - 1);
                saveItems();
                Toast.makeText(getApplicationContext(),
                                 "Item added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retrieve updated text value.
    // Retrieve position of the edited item.
    // Update the model (items) at the right position with the new item text.
    // Notify the adapter.
    // Persist the changes to the local filesystem.
    // Display message to user
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(),
                    "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // Return the file in which we will store our list of items
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // Load items by reading the data file line by line
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items");
            items = new ArrayList<>();
        }
    }

    // Save items by writing them to the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items");
        }
    }
}