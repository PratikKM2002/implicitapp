package com.example.implicit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText phoneInput, webInput;
    private static final int PERMISSION_REQUEST_PHONE_CALL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Updated layout file name to implicit_activity.xml
        setContentView(R.layout.activity_main);

        // Initialize UI elements with new IDs
        phoneInput = findViewById(R.id.phoneInput);
        Button callButton = findViewById(R.id.callButton);

        webInput = findViewById(R.id.webInput);
        Button browseButton = findViewById(R.id.browseButton);

        Button exitButton = findViewById(R.id.exitButton);

        // Set up listeners for button clicks
        callButton.setOnClickListener(v -> initiateCall());
        browseButton.setOnClickListener(v -> launchWebsite());
        exitButton.setOnClickListener(v -> exitApplication());
    }

    // Initiates a phone call after validating the phone number
    private void initiateCall() {
        String number = phoneInput.getText().toString().trim();

        // Validate that the number is exactly 10 digits
        if (number.isEmpty() || !number.matches("^[0-9]{10}$")) {
            Toast.makeText(this, "Please provide a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_PHONE_CALL);
        }
    }

    // Opens the URL entered by the user in the default browser
    private void launchWebsite() {
        String link = webInput.getText().toString().trim();
        if (!link.isEmpty()) {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "https://" + link;
            }
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(webIntent);
        } else {
            Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
        }
    }

    // Closes the application completely
    private void exitApplication() {
        finishAffinity();
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateCall();
            } else {
                Toast.makeText(this, "Call permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
