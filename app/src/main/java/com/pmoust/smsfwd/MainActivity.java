package com.pmoust.smsfwd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.content.pm.PackageManager;
import android.Manifest;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private static final String SHARED_PREFS = "SMSForwarderPrefs";
    private static final String PHONE_NUMBER_KEY = "destinationPhoneNumber";
    private EditText phoneNumberInput;
    private Button savePhoneNumberButton;
    private Switch smsForwardingToggle;

    private void requestSmsPermissions() {
        String[] permissions = {Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, permissions, SMS_PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean smsForwardingEnabled = sharedPreferences.getBoolean("sms_forwarding_enabled", false);

        setContentView(R.layout.activity_main);
        smsForwardingToggle = findViewById(R.id.sms_forwarding_toggle);
        smsForwardingToggle.setChecked(smsForwardingEnabled);

        requestSmsPermissions();

        phoneNumberInput = findViewById(R.id.phone_number_input);
        savePhoneNumberButton = findViewById(R.id.save_phone_number_button);

        loadPhoneNumber();

        savePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoneNumber();
            }
        });

        smsForwardingToggle.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("sms_forwarding_enabled", smsForwardingToggle.isChecked());
                editor.apply();

                if (smsForwardingToggle.isChecked()) {
                    Toast.makeText(MainActivity.this, "SMS Forwarding enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "SMS Forwarding disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPhoneNumber() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER_KEY, "");
        phoneNumberInput.setText(phoneNumber);
    }

    private void savePhoneNumber() {
        String phoneNumber = phoneNumberInput.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_NUMBER_KEY, phoneNumber);
        editor.apply();
        Toast.makeText(this, "Phone number saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS permissions were granted
                Toast.makeText(this, "SMS permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                // SMS permissions were denied
                Toast.makeText(this, "SMS permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}