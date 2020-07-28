package com.example.hp_awareness_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;
import java.util.Objects;

public class PhoneActivity extends AppCompatActivity {
    private Spinner spinner;
    private TextInputEditText editText;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_bar));
        }

      //  spinner = findViewById(R.id.spinnerCountries);
     //  spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        editText = findViewById(R.id.et_phone_number);

        findViewById(R.id.button4).setOnClickListener(v -> {
            Intent intent = new Intent(PhoneActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.button3).setOnClickListener(v -> {

            String number = editText.getText().toString().trim();

            if (number.isEmpty() || number.length() < 10) {
                editText.setError("Valid number is required");
                editText.requestFocus();
                return;
            }

            String phoneNumber = "+" + 91 + number;

            preferences = getSharedPreferences("App", MODE_PRIVATE);
            editor = preferences.edit();
            editor.putString("Contact", phoneNumber);
            editor.commit();

            Intent intent = new Intent(PhoneActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("phonenumber", phoneNumber);
            startActivity(intent);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            List<? extends UserInfo> pd = user.getProviderData();
            UserInfo providerData = pd.get(1);
            String pid = providerData.getProviderId();
            if (Objects.equals(pid, "password")) {
                Intent intent = new Intent(this, NewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type", "Admin");
                startActivity(intent);
            } else if (Objects.equals(pid, "phone")) {
                Intent intent = new Intent(this, NewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type", "User");
                startActivity(intent);
            }
        }
    }
}

