package com.example.triviaquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "USERNAME_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        String username = getIntent().getStringExtra(USERNAME_KEY);

        TextView welcomeTextView = findViewById(R.id.dashWelcomeTextView);
        welcomeTextView.setText(String.format("%s%s!", getString(R.string.DashboardActivityClassWelcome), username));
    }

    public static Intent dashboardIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        return intent;
    }

}
