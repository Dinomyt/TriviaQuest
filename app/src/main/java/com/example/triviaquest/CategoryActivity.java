package com.example.triviaquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.databinding.CategoryActivityBinding;
import com.example.triviaquest.viewHolders.CategoryAdapter;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private CategoryAdapter adapter;
    private CategoryActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CategoryActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.backButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        adapter = new CategoryAdapter(new ArrayList<>(), new CategoryAdapter.onCategoryClickListener() {
            @Override
            public void onCategoryClick(Category categories) {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                intent.putExtra("categoryId", categories.getCategoryId());
                startActivity(intent);
            }
        });
        binding.categoryRecyclerView.setAdapter(adapter);
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        TriviaQuestionsRepository repository =
                TriviaQuestionsRepository.getRepository(getApplication());
        assert repository != null;
        repository.getAllCategories().observe(this, categories ->
                adapter.updateList(categories));
        };



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


}
