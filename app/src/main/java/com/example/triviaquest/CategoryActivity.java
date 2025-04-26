package com.example.triviaquest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.databinding.CategoryActivityBinding;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "CategoryActivity";
    private CategoryActivityBinding binding;
    private boolean hasLaidOut = false;  // skip initial callback

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CategoryActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Back button
        binding.backButtonTextView.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Observe categories LiveData
        TriviaQuestDatabase
                .getDatabase(this)
                .categoryDAO()
                .getAllCategoriesLiveData()
                .observe(this, new Observer<List<Category>>() {
                    @Override
                    public void onChanged(List<Category> categories) {
                        // Console and logcat output
                        String msg = "Loaded " + categories.size() + " categories";
                        Log.i(TAG, msg);
                        System.out.println(msg);

                        // On-screen check
                        Toast.makeText(CategoryActivity.this, msg, Toast.LENGTH_LONG).show();

                        // Populate spinner
                        ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                                CategoryActivity.this,
                                android.R.layout.simple_spinner_item,
                                categories
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerCategories.setAdapter(adapter);

                        // Only launch quiz when user actually selects
                        binding.spinnerCategories.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent,
                                                               View view,
                                                               int position,
                                                               long id) {
                                        if (!hasLaidOut) {
                                            hasLaidOut = true;
                                            return;
                                        }
                                        int catId = categories.get(position).getCategoryId();
                                        startActivity(new Intent(CategoryActivity.this, QuizActivity.class)
                                                .putExtra("categoryId", catId));
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) { }
                                }
                        );
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Logout
        MenuItem logout = menu.findItem(R.id.logoutMenuItem);
        logout.setVisible(true);
        logout.setTitle("Logout");
        logout.setOnMenuItemClickListener(item -> {
            startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            return false;
        });

        // Username display
        MenuItem userItem = menu.findItem(R.id.usernameMenuItem);
        int userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE)
                .getInt("userId", -1);
        if (userId != -1) {
            TriviaQuestDatabase
                    .getDatabase(this)
                    .userDAO()
                    .getUserByUserId(userId)
                    .observe(this, user -> {
                        if (user != null) userItem.setTitle(user.getUsername());
                    });
        }
        return true;
    }
}
