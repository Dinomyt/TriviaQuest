package com.example.triviaquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.entities.Category;

public class DashboardActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "USERNAME_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        int userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE).getInt("userId", -1);
        TextView welcomeTextView = findViewById(R.id.dashWelcomeTextView);

        if (userId != -1) {
            TriviaQuestDatabase db = TriviaQuestDatabase.getDatabase(this);
            db.userDAO().getUserByUserId(userId).observe(this, user -> {
                if (user != null) {
                    welcomeTextView.setText(String.format("%s%s!", getString(R.string.DashboardActivityClassWelcome), user.getUsername()));
                } else {
                    welcomeTextView.setText("Welcome!");
                }
            });
        } else {
            welcomeTextView.setText("Welcome!");
        }

        Button readyButton = findViewById(R.id.readyTextView);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

    }

    public static Intent dashboardIntentFactory(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        return intent;
    }

}
