package com.ibradecode.whatscloneweb;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.RenderProcessGoneDetail;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.ibradecode.whatscloneweb.AccountManager;
import com.ibradecode.whatscloneweb.NotificationHelper;
import java.util.Set;
import java.io.InputStream;
import java.io.IOException;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.provider.MediaStore;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    private AccountManager accountManager;
    private static final String PREFS_NAME = "WhatsCloneWebPrefs";
    private ValueCallback<Uri[]> mFilePathCallback;
    private static final int FILE_CHOOSER_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 2;
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize AccountManager
        accountManager = new AccountManager(this);

        // Create notification channel
        NotificationHelper.createNotificationChannel(this);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("WhatsCloneWeb");
        }

        // Initialize WebView
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);
        setupWebView();

        // Initialize FAB
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showSendMessageDialog());

        // Load WhatsApp Web
        // Check for active account and load its data
        String activeAccount = accountManager.getActiveAccount();
        if (activeAccount != null) {
            accountManager.loadAccountData(activeAccount);
            webView.loadUrl("https://web.whatsapp.com/");
        } else {
            // If no active account, load WhatsApp Web normally and prompt user to save account later
            webView.loadUrl("https://web.whatsapp.com/");
        }

        // Example: Save current account data (you\'d trigger this on logout or app exit)
        // For demonstration, let\'s assume a fixed account ID for now.
        // In a real app, you\'d have a mechanism for users to define/select account IDs.
        // accountManager.saveAccountData("default_account");
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        
        // Enable JavaScript
        webSettings.setJavaScriptEnabled(true);
        
        // Enable DOM storage
        webSettings.setDomStorageEnabled(true);
        
        // Enable file access
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        
        // Enable wide viewport
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        
        // Apply User-Agent from settings
        SharedPreferences settingsPrefs = getSharedPreferences("WhatsCloneWebSettings", MODE_PRIVATE);
        String userAgent = settingsPrefs.getString("user_agent", "Default");
        if (userAgent.equals("Desktop (Windows)")) {
            webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        } else if (userAgent.equals("Mobile (Android)")) {
            webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36");
        } else if (userAgent.equals("Mobile (iOS)")) {
            webSettings.setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 14_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1");
        } else {
            // Default or unknown, let WebView decide
            webSettings.setUserAgentString(null);
        }

        // Apply Font Size from settings
        String fontSize = settingsPrefs.getString("font_size", "medium");
        if (fontSize.equals("small")) {
            webSettings.setTextZoom(80);
        } else if (fontSize.equals("large")) {
            webSettings.setTextZoom(120);
        } else {
            webSettings.setTextZoom(100);
        }
        
        // Enable zoom controls
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        
        // Enable mixed content
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        
        // Enable hardware acceleration
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Set WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                injectCustomCSS();
                injectMessageMonitorScript();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(MainActivity.this, "Error loading page: " + description, Toast.LENGTH_LONG).show();
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                if (detail.didCrash()) {
                    Toast.makeText(MainActivity.this, "WebView crashed, reloading...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "WebView killed by OS, reloading...", Toast.LENGTH_SHORT).show();
                }
                webView.reload();
                return true;
            }
        });
        
        // Set WebChromeClient for better compatibility and native feature integration
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            // For file upload (e.g., camera, gallery)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("*/*");

                Intent[] intentArray;
                if (fileChooserParams.getAcceptTypes() != null && fileChooserParams.getAcceptTypes().length > 0) {
                    // Handle camera/video capture if requested
                    if (fileChooserParams.isCaptureEnabled()) {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // You might want to add a video capture intent here as well
                        intentArray = new Intent[]{captureIntent};
                    } else {
                        intentArray = new Intent[0];
                    }
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Pilih Gambar/Video");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, FILE_CHOOSER_REQUEST_CODE);
                return true;
            }

            // For geolocation permissions
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            // For camera and microphone permissions
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String[] requestedResources = request.getResources();
                    for (String r : requestedResources) {
                        if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                            } else {
                                request.grant(requestedResources);
                            }
                        } else if (r.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_REQUEST_CODE);
                            } else {
                                request.grant(requestedResources);
                            }
                        }
                    }
                }
            }
        });
    }

    private void injectCustomCSS() {
        String css = readScriptFromFile(R.raw.custom_css);
        if (css != null) {
            webView.evaluateJavascript(css, null);
        }
    }

    private void injectMessageMonitorScript() {
        String script = readScriptFromFile(R.raw.message_monitor);
        if (script != null) {
            webView.evaluateJavascript(script, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (mFilePathCallback == null) return;
            Uri[] results = null;
            if (resultCode == RESULT_OK) {
                if (intent == null || intent.getData() == null) {
                    // If no data, then we may have taken a photo
                    if (cameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(cameraPhotoPath)};
                    }
                } else {
                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now proceed with camera operations
                    Toast.makeText(this, "Izin kamera diberikan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show();
                }
                break;
            case RECORD_AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now proceed with audio recording
                    Toast.makeText(this, "Izin mikrofon diberikan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Izin mikrofon ditolak", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String cameraPhotoPath;

    private void showSendMessageDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_send_message, null);
        TextInputEditText etPhone = dialogView.findViewById(R.id.etPhone);
        TextInputEditText etMessage = dialogView.findViewById(R.id.etMessage);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btnSend).setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String message = etMessage.getText().toString().trim();

            if (phone.isEmpty()) {
                etPhone.setError("Nomor telepon tidak boleh kosong");
                return;
            }

            if (message.isEmpty()) {
                etMessage.setError("Pesan tidak boleh kosong");
                return;
            }

            // Save to history
            saveToHistory(phone, message);

            // Open WhatsApp Web with message
            String url = "https://web.whatsapp.com/send?phone=" + phone + "&text=" + message;
            webView.loadUrl(url);

            dialog.dismiss();
            Toast.makeText(this, "Membuka chat dengan " + phone, Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void saveToHistory(String phone, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String historyEntry = phone + "|" + message + "|" + timestamp;
        
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String existingHistory = sharedPreferences.getString("chat_history", "");
        
        if (existingHistory.isEmpty()) {
            editor.putString("chat_history", historyEntry);
        } else {
            editor.putString("chat_history", existingHistory + ";" + historyEntry);
        }
        
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reload) {
            webView.reload();
            return true;
        } else if (id == R.id.action_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_exit) {
            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            // Save current account data before destroying WebView
            String activeAccount = accountManager.getActiveAccount();
            if (activeAccount != null) {
                accountManager.saveAccountData(activeAccount);
            }
            webView.destroy();
        }
        super.onDestroy();
    }
}



    private String readScriptFromFile(int resourceId) {
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


