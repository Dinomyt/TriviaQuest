package com.example.triviaquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.entities.Category;

public class DashboardActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "USERNAME_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        invalidateOptionsMenu();

        findViewById(R.id.leaderboardButton)
                .setOnClickListener(v ->
                        startActivity(new Intent(this, LeaderboardActivity.class)));

        String username = getIntent().getStringExtra(USERNAME_KEY);

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
            welcomeTextView.setText(String.format("%s%s!", getString(R.string.DashboardActivityClassWelcome), username));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        item.setTitle("Logout");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                return false;
            }
        });

        MenuItem usernameItem = menu.findItem(R.id.usernameMenuItem);
        int userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (userId != -1) {
            TriviaQuestDatabase db = TriviaQuestDatabase.getDatabase(this);
            db.userDAO().getUserByUserId(userId).observe(this, user -> {
                if (user != null) {
                    usernameItem.setTitle(user.getUsername());
                }
            });
        }
        return true;
    }

    public static Intent dashboardIntentFactory(Context context, String username) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        return intent;
    }

}
