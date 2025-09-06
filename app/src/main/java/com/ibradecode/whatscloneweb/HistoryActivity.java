package com.ibradecode.whatscloneweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryItemClickListener {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryItem> historyList;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "WhatsCloneWebPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Riwayat Chat");
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load history data
        loadHistoryData();

        // Setup adapter
        adapter = new HistoryAdapter(historyList, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadHistoryData() {
        historyList = new ArrayList<>();
        String historyString = sharedPreferences.getString("chat_history", "");

        if (!historyString.isEmpty()) {
            String[] entries = historyString.split(";");
            for (String entry : entries) {
                String[] parts = entry.split("\\|");
                if (parts.length == 3) {
                    String phone = parts[0];
                    String message = parts[1];
                    String timestamp = parts[2];
                    historyList.add(new HistoryItem(phone, message, timestamp));
                }
            }
            // Reverse the list to show newest first
            Collections.reverse(historyList);
        }
    }

    @Override
    public void onItemClick(HistoryItem item) {
        // Return to MainActivity and open chat
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("phone", item.getPhone());
        intent.putExtra("message", item.getMessage());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemLongClick(HistoryItem item) {
        // Remove item from history
        removeFromHistory(item);
    }

    private void removeFromHistory(HistoryItem itemToRemove) {
        // Remove from list
        historyList.remove(itemToRemove);
        adapter.notifyDataSetChanged();

        // Update SharedPreferences
        StringBuilder newHistoryString = new StringBuilder();
        for (int i = historyList.size() - 1; i >= 0; i--) { // Reverse back for storage
            HistoryItem item = historyList.get(i);
            if (newHistoryString.length() > 0) {
                newHistoryString.append(";");
            }
            newHistoryString.append(item.getPhone())
                    .append("|")
                    .append(item.getMessage())
                    .append("|")
                    .append(item.getTimestamp());
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("chat_history", newHistoryString.toString());
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

