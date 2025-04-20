package com.example.triviaquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.triviaquest.MAIN_ACTIVITY_USER_ID";
    private ActivityMainBinding binding;

    private TriviaQuestionsRepository repository;
    public static final String TAG = "TRIVIAQUEST_GYMLOG";

    String mOptionA;
    String mOptionB;
    String mOptionC;
    String mOptionD;
    boolean checkBox = false;

    public static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
            }
        });
    }

    private void getInformationFromDisplay(){

    }
}