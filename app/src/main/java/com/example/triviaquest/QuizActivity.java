package com.example.triviaquest;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.api.OpenTdbService;
import com.example.triviaquest.api.TriviaApiResponse;
import com.example.triviaquest.database.TriviaQuestDatabase;
import com.example.triviaquest.database.UserDAO;
import com.example.triviaquest.database.entities.TriviaQuestions;
import com.example.triviaquest.database.entities.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizActivity extends AppCompatActivity {
    private TextView tvQuestion;
    private RadioGroup rgOptions;
    private Button btnNext;

    private List<TriviaQuestions> questions = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;

    private int localCategoryId;
    private int apiCategoryCode;

    private OpenTdbService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion  = findViewById(R.id.tvQuestion);
        rgOptions   = findViewById(R.id.rgOptions);
        btnNext     = findViewById(R.id.btnNext);

        localCategoryId = getIntent().getIntExtra("categoryId",-1);
        apiCategoryCode = mapToApiCategory(localCategoryId);

        // set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(OpenTdbService.class);

        // fetch fresh questions
        fetchQuestions();

        btnNext.setOnClickListener(v -> onNext());
    }

    private void fetchQuestions() {
        new Thread(() -> {
            try {
                Response<TriviaApiResponse> resp = api
                        .getQuestions(10, apiCategoryCode, "medium", "multiple")
                        .execute();

                if (resp.isSuccessful() && resp.body() != null) {
                    List<TriviaApiResponse.Result> results = resp.body().results;
                    List<TriviaQuestions> list = new ArrayList<>();

                    for (TriviaApiResponse.Result r : results) {
                        List<String> opts = new ArrayList<>(r.incorrectAnswers);
                        opts.add(r.correctAnswer);
                        Collections.shuffle(opts);

                        String qText   = Html.fromHtml(r.question,      Html.FROM_HTML_MODE_LEGACY).toString();
                        String a       = Html.fromHtml(opts.get(0),     Html.FROM_HTML_MODE_LEGACY).toString();
                        String b       = Html.fromHtml(opts.get(1),     Html.FROM_HTML_MODE_LEGACY).toString();
                        String c       = Html.fromHtml(opts.get(2),     Html.FROM_HTML_MODE_LEGACY).toString();
                        String d       = Html.fromHtml(opts.get(3),     Html.FROM_HTML_MODE_LEGACY).toString();
                        String correct = Html.fromHtml(r.correctAnswer, Html.FROM_HTML_MODE_LEGACY).toString();

                        list.add(new TriviaQuestions(
                                qText, a, b, c, d, correct, localCategoryId
                        ));
                    }

                    runOnUiThread(() -> {
                        questions.clear();
                        questions.addAll(list);
                        currentIndex = 0;
                        score = 0;
                        showQuestion();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Failed to load questions", Toast.LENGTH_LONG).show()
                    );
                }
            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void onNext() {
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selected = findViewById(checkedId);
        String answer = selected.getText().toString();
        TriviaQuestions current = questions.get(currentIndex);

        if (answer.equals(current.getCorrectAnswer())) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            // update user score
            int userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE)
                    .getInt("userId", -1);
            if (userId != -1) {
                new Thread(() -> {
                    UserDAO dao = TriviaQuestDatabase.getDatabase(this).userDAO();
                    User u = dao.getUserByUserIdSync(userId);
                    if (u != null) {
                        u.setScore(u.getScore() + 1);
                        dao.insert(u);
                    }
                }).start();
            }
        } else {
            Toast.makeText(this,
                    "Oops! Right answer: " + current.getCorrectAnswer(),
                    Toast.LENGTH_SHORT).show();
        }

        currentIndex++;
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            Intent intent = new Intent(this, QuizResultsActivity.class);
            intent.putExtra(QuizResultsActivity.EXTRA_SCORE, score);
            intent.putExtra(QuizResultsActivity.EXTRA_TOTAL, questions.size());
            startActivity(intent);
            finish();
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

    /**
     * Map your local category IDs (1=Geography,2=Literature,3=History,4=Sports)
     * to OpenTDB API codes (22,10,23,21 respectively).
     */
    private int mapToApiCategory(int localCatId) {
        switch (localCatId) {
            case 1: return 22;
            case 2: return 10;
            case 3: return 23;
            case 4: return 21;
            default: return 0; // fallback: mixed or any
        }
    }
}
