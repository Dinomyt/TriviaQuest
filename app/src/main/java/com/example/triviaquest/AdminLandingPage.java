package com.example.triviaquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminLandingPage extends AppCompatActivity {

    private View layoutAddQuestion, layoutAddCategory, layoutEditQuestions, layoutEditCategories;
    private TriviaQuestionsRepository triviaQuestionsRepository;
    private EditText etQuestionText, etAnswer1, etAnswer2, etAnswer3, etAnswer4;
    private Spinner spinnerCorrectAnswer, spinnerCategory, spinnerSelectCategory, spinnerSelectQuestion;
    private int currentQuestionId = -1; // Holds the ID of the question being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing_page);

        // Check if the user is admin
        SharedPreferences prefs = getSharedPreferences("TriviaPrefs", MODE_PRIVATE);
        boolean isAdmin = prefs.getBoolean("isAdmin", false);

        if (!isAdmin) {
            Toast.makeText(this, "Access Denied. Admins Only.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize repository
        triviaQuestionsRepository = TriviaQuestionsRepository.getRepository(getApplication());

        // Initialize buttons
        Button btnAddQuestion = findViewById(R.id.btnAddQuestion);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        Button btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        Button btnEditQuestion = findViewById(R.id.btnEditQuestion);
        Button btnGoHome = findViewById(R.id.btnGoHome);

        // Initialize layouts
        layoutAddQuestion = findViewById(R.id.layoutAddQuestion);
        layoutAddCategory = findViewById(R.id.layoutAddCategory);
        layoutEditQuestions = findViewById(R.id.layoutEditQuestions);
        layoutEditCategories = findViewById(R.id.layoutEditCategories);

        // Initialize input fields
        etQuestionText = findViewById(R.id.editTextQuestion);
        etAnswer1 = findViewById(R.id.editTextAnswer1);
        etAnswer2 = findViewById(R.id.editTextAnswer2);
        etAnswer3 = findViewById(R.id.editTextAnswer3);
        etAnswer4 = findViewById(R.id.editTextAnswer4);
        spinnerCorrectAnswer = findViewById(R.id.spinnerCorrectAnswer);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSelectCategory = findViewById(R.id.spinnerSelectCategory);
        spinnerSelectQuestion = findViewById(R.id.spinnerSelectQuestion);

        loadCorrectAnswers();
        loadCategories();

        // Button click listeners
        btnAddQuestion.setOnClickListener(v -> setVisibleSection(layoutAddQuestion));
        btnAddCategory.setOnClickListener(v -> setVisibleSection(layoutAddCategory));
        btnDeleteCategory.setOnClickListener(v -> setVisibleSection(layoutEditCategories));
        btnEditQuestion.setOnClickListener(v -> {
            setVisibleSection(layoutEditQuestions);
            loadQuestions();
        });


        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLandingPage.this, DashboardActivity.class);
            startActivity(intent);
            finish(); // optional, this will close Admin page so it's not in the backstack
        });
    }

    private void loadCorrectAnswers() {
        // Set the answers for the correct answer spinner
        List<String> answers = new ArrayList<>();
        answers.add("Answer 1");
        answers.add("Answer 2");
        answers.add("Answer 3");
        answers.add("Answer 4");

        // Set the adapter for the spinner
        ArrayAdapter<String> answersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answers);
        answersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCorrectAnswer.setAdapter(answersAdapter);
    }

    private void loadQuestions() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Fetch questions from the database
            List<TriviaQuestions> questions = triviaQuestionsRepository.getAllQuestions();

            // Make sure questions is never null
            if (questions == null) {
                questions = new ArrayList<>();
            }

            // Update the spinner on the main thread
            List<TriviaQuestions> finalQuestions = questions;
            runOnUiThread(() -> {
                ArrayAdapter<TriviaQuestions> adapter = new ArrayAdapter<>(
                        AdminLandingPage.this,
                        android.R.layout.simple_spinner_item,
                        finalQuestions
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSelectQuestion.setAdapter(adapter);
            });
        });
    }

    private void loadCategories() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Category> categories = triviaQuestionsRepository.getAllCategories();

            runOnUiThread(() -> {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(AdminLandingPage.this,
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                if (spinnerCategory != null) {
                    spinnerCategory.setAdapter(adapter);
                }
                if (spinnerSelectCategory != null) {
                    spinnerSelectCategory.setAdapter(adapter);
                }
            });
        });
    }

    public void onDeleteCategory(View view) {
        // Get the selected category
        Category selectedCategory = (Category) spinnerSelectCategory.getSelectedItem();

        if (selectedCategory == null) {
            Toast.makeText(this, "Please select a category to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = selectedCategory.categoryId;

        // Show confirmation dialog before deletion
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    triviaQuestionsRepository.deleteCategoryById(categoryId);
                    Toast.makeText(this, "Category deleted successfully.", Toast.LENGTH_SHORT).show();
                    loadCategories();
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void setVisibleSection(View visibleLayout) {
        layoutAddQuestion.setVisibility(View.GONE);
        layoutAddCategory.setVisibility(View.GONE);
        layoutEditQuestions.setVisibility(View.GONE);
        layoutEditCategories.setVisibility(View.GONE);
        visibleLayout.setVisibility(View.VISIBLE);
    }

    // Existing method for adding a question
    public void onSubmitAddQuestion(View view) {
        String questionText = etQuestionText.getText().toString().trim();
        String answer1 = etAnswer1.getText().toString().trim();
        String answer2 = etAnswer2.getText().toString().trim();
        String answer3 = etAnswer3.getText().toString().trim();
        String answer4 = etAnswer4.getText().toString().trim();
        int correctAnswer = spinnerCorrectAnswer.getSelectedItemPosition();
        int categoryId = (int) spinnerCategory.getSelectedItemId();

        if (questionText.isEmpty() || answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty() || correctAnswer == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        TriviaQuestions triviaQuestion = new TriviaQuestions(
                questionText, answer1, answer2, answer3, answer4, String.valueOf(correctAnswer), categoryId
        );

        triviaQuestionsRepository.insertQuestion(triviaQuestion);
        Toast.makeText(this, "Question added successfully.", Toast.LENGTH_SHORT).show();
    }

    // Existing method for submitting a category
    public void onSubmitAddCategory(View view) {
        EditText editTextCategoryName = findViewById(R.id.editTextCategoryName);
        String categoryName = editTextCategoryName.getText().toString().trim();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name.", Toast.LENGTH_SHORT).show();
            return;
        }

        Category newCategory = new Category(categoryName);
        triviaQuestionsRepository.insertCategory(newCategory);
        Toast.makeText(this, "Category added successfully.", Toast.LENGTH_SHORT).show();
        editTextCategoryName.setText("");
    }
}


