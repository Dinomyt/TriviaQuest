package com.example.triviaquest;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.UserDAO;
import com.example.triviaquest.database.entities.TriviaQuestions;
import com.example.triviaquest.database.entities.User;

import java.util.List;
import java.util.concurrent.Executors;

public class Quiz extends AppCompatActivity {
    private TextView tvQuestion;
    private RadioGroup rgOptions;
    private List<TriviaQuestions> questions;
    private int currentIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions   = findViewById(R.id.rgOptions);
        findViewById(R.id.btnNext).setOnClickListener(v -> onNext());

        int categoryId = getIntent().getIntExtra("categoryId", -1);
        Executors.newSingleThreadExecutor().execute(() -> {
            questions = TriviaQuestDatabase
                    .getDatabase(this)
                    .triviaQuestionsDAO()
                    .getByCategory(categoryId);
            runOnUiThread(this::showQuestion);
        });
    }

    private void onNext() {
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId < 0) { Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show(); return; }
        RadioButton selected = findViewById(checkedId);
        String answer = selected.getText().toString();
        TriviaQuestions current = questions.get(currentIndex);

        if (answer.equals(current.getCorrectAnswer())) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            // update user score
            int userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE).getInt("userId", -1);
            if (userId != -1) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    UserDAO dao = TriviaQuestDatabase.getDatabase(this).userDAO();
                    User u = dao.getUserByUserIdSync(userId);
                    if (u != null) {
                        u.setScore(u.getScore() + 1);
                        dao.insert(u);
                    }
                });
            }
        } else {
            Toast.makeText(this, "Oops! Right answer: " + current.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
        }
        currentIndex++; showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            new AlertDialog.Builder(this)
                    .setTitle("Quiz complete!")
                    .setMessage("Your score: " + score + "/" + questions.size())
                    .setPositiveButton("OK", (d,w) -> finish())
                    .show();
            return;
        }
        TriviaQuestions q = questions.get(currentIndex);
        tvQuestion.setText(q.getQuestionText());
        ((RadioButton)findViewById(R.id.rbA)).setText(q.getOptionA());
        ((RadioButton)findViewById(R.id.rbB)).setText(q.getOptionB());
        ((RadioButton)findViewById(R.id.rbC)).setText(q.getOptionC());
        ((RadioButton)findViewById(R.id.rbD)).setText(q.getOptionD());
        rgOptions.clearCheck();
    }
}
