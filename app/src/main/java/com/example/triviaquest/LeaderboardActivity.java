package com.example.triviaquest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

}
