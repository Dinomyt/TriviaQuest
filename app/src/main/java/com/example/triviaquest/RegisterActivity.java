package com.example.triviaquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.database.entities.User;
import com.example.triviaquest.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    
    private ActivityRegisterBinding binding;
    private TriviaQuestionsRepository repository;

    public static Intent registerActivityIntentFactory(Context context) {
        return new Intent(context, RegisterActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        repository = TriviaQuestionsRepository.getRepository(getApplication());
        
        binding.registerButton.setOnClickListener(v -> registerNewUser());
    }

    private void registerNewUser() {
        String username = binding.userNameRegisterEditText.getText().toString();
        String password = binding.passwordRegisterEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            toastMaker("Username and Password cannot be empty");
            return;
        }

        User existing = repository.findUserByUsernameSync(username);
        if (existing != null) {
            toastMaker("Username already exists");
            return;
        }

        repository.insertUser(new User(username, password));
        toastMaker("User registered successfully");
        finish();
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
