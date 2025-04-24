package com.example.triviaquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.database.entities.User;
import com.example.triviaquest.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private TriviaQuestionsRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = TriviaQuestionsRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.registerActivityIntentFactory(getApplicationContext()));
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString();


        if(username.isEmpty()) {
            toastMaker("Username cannot be empty");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if (password.equals(user.getPassword())) {
                    getSharedPreferences("TriviaPrefs", MODE_PRIVATE)
                            .edit()
                            .putInt("userId", user.getId())
                            .apply();
                    startActivity(DashboardActivity.dashboardIntentFactory(getApplicationContext()));
                } else {
                    toastMaker("Invalid Password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker(String.format("%s not a valid username", username));
                binding.userNameLoginEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}