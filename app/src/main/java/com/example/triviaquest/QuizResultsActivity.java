package com.example.triviaquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizResultsActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "EXTRA_SCORE";
    public static final String EXTRA_TOTAL = "EXTRA_TOTAL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        // Grab score data from intent
        int score = getIntent().getIntExtra(EXTRA_SCORE, 0);
        int total = getIntent().getIntExtra(EXTRA_TOTAL, 0);

        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText(String.format("Your score: %d/%d", score, total));

        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);
        btnLeaderboard.setOnClickListener(v -> {
            startActivity(new Intent(this, LeaderboardActivity.class));
            finish();
        });
    }
}
