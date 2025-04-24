package com.example.triviaquest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.databinding.ActivityLeaderboardBinding;
import com.example.triviaquest.viewHolders.LeaderboardAdapter;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    private ActivityLeaderboardBinding binding;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new LeaderboardAdapter(new ArrayList<>());
        binding.leaderboardRecycler.setAdapter(adapter);
        binding.leaderboardRecycler.setLayoutManager(new LinearLayoutManager(this));

        loadLeaderboardData();
    }

    private void loadLeaderboardData() {
        TriviaQuestionsRepository repository =
                TriviaQuestionsRepository.getRepository(getApplication());
        if (repository == null) {
            return;
        }
        repository.getLeaderboard().observe(this, users -> {
            adapter.updateList(users);
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

}
