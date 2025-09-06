package com.ibradecode.whatscloneweb;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebStorage;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "WhatsCloneWebSettings";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_USER_AGENT = "user_agent";

    private SwitchMaterial switchDarkMode;
    private RadioGroup radioGroupFontSize;
    private Spinner spinnerUserAgent;
    private Button btnClearCache;
    private Button btnClearHistory;
    private TextView tvVersion;
    private TextView tvLicense;
    private TextView tvResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pengaturan");
        }

        // Initialize views
        switchDarkMode = findViewById(R.id.switchDarkMode);
        radioGroupFontSize = findViewById(R.id.radioGroupFontSize);
        spinnerUserAgent = findViewById(R.id.spinnerUserAgent);
        btnClearCache = findViewById(R.id.btnClearCache);
        btnClearHistory = findViewById(R.id.btnClearHistory);
        tvVersion = findViewById(R.id.tvVersion);
        tvLicense = findViewById(R.id.tvLicense);
        tvResources = findViewById(R.id.tvResources);

        // Load settings
        loadSettings();

        // Set listeners
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            applyTheme();
        });

        radioGroupFontSize.setOnCheckedChangeListener((group, checkedId) -> {
            String fontSize = "medium";
            if (checkedId == R.id.radioSmall) {
                fontSize = "small";
            } else if (checkedId == R.id.radioLarge) {
                fontSize = "large";
            }
            sharedPreferences.edit().putString(KEY_FONT_SIZE, fontSize).apply();
            Toast.makeText(this, "Ukuran font diatur ke " + fontSize, Toast.LENGTH_SHORT).show();
            // Note: Applying font size dynamically to WebView is complex and usually requires reloading WebView or injecting CSS.
            // For simplicity, this will just save the preference.
        });

        btnClearCache.setOnClickListener(v -> {
            WebStorage.getInstance().deleteAllData();
            Toast.makeText(this, "Cache WebView dibersihkan", Toast.LENGTH_SHORT).show();
        });

        btnClearHistory.setOnClickListener(v -> {
            SharedPreferences mainPrefs = getSharedPreferences("WhatsCloneWebPrefs", MODE_PRIVATE);
            mainPrefs.edit().remove("chat_history").apply();
            Toast.makeText(this, "Riwayat chat dibersihkan", Toast.LENGTH_SHORT).show();
        });

        // Setup User-Agent Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_agent_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserAgent.setAdapter(adapter);
        // Set initial selection based on saved preference
        String savedUserAgent = sharedPreferences.getString(KEY_USER_AGENT, "Default");
        int spinnerPosition = adapter.getPosition(savedUserAgent);
        spinnerUserAgent.setSelection(spinnerPosition);

        // Set version info
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText("Versi: " + versionName);
        } catch (Exception e) {
            e.printStackTrace();
            tvVersion.setText("Versi: N/A");
        }
    }

    private void loadSettings() {
        boolean darkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        switchDarkMode.setChecked(darkMode);
        applyTheme();

        String fontSize = sharedPreferences.getString(KEY_FONT_SIZE, "medium");
        if (fontSize.equals("small")) {
            radioGroupFontSize.check(R.id.radioSmall);
        } else if (fontSize.equals("large")) {
            radioGroupFontSize.check(R.id.radioLarge);
        } else {
            radioGroupFontSize.check(R.id.radioMedium);
        }
    }

    private void applyTheme() {
        if (switchDarkMode.isChecked()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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


