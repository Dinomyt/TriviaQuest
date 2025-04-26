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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triviaquest.database.CategoryDAO;
import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.databinding.CategoryActivityBinding;
import com.example.triviaquest.databinding.DashboardActivityBinding;
import com.example.triviaquest.viewHolders.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    private DashboardActivityBinding binding;
    private List<Category> recentCategories = new ArrayList<>();
    private CategoryDAO cDao;
    private static final String USERNAME_KEY = "USERNAME_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DashboardActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        adapter = new CategoryAdapter(new ArrayList<>(), null);

        binding.categoryRecyclerView.setAdapter(adapter);
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TriviaQuestionsRepository repository = TriviaQuestionsRepository.getRepository(getApplication());
        repository.getTwoMostRecentCategories().observe(this, categories -> {
            if (categories != null) {
                adapter.updateList(categories);
            }
        });


        Button readyButton = findViewById(R.id.readyTextView);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        Button adminButton = findViewById(R.id.adminLandingPageButton);

// Get user info
        userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE).getInt("userId", -1);

        if (userId != -1) {
            TriviaQuestDatabase db = TriviaQuestDatabase.getDatabase(this);
            db.userDAO().getUserByUserId(userId).observe(this, user -> {
                if (user != null && user.isAdmin()) { // Assuming you have a isAdmin() field
                    adminButton.setVisibility(View.VISIBLE); // Show only if admin
                }
            });
        }

// Set what happens when admin clicks the button
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AdminLandingPage.class);
            startActivity(intent);
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
