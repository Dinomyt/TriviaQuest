package com.example.triviaquest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestionsRepository;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;

public class AdminLandingPage extends AppCompatActivity {

    private View layoutAddQuestion, layoutAddCategory, layoutEditQuestions, layoutEditCategories;
    private TriviaQuestionsRepository triviaQuestionsRepository;

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

        //initialize repository
        triviaQuestionsRepository = TriviaQuestionsRepository.getRepository(getApplication());

        // Initialize buttons
        Button btnAddQuestion = findViewById(R.id.btnAddQuestion);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        Button btnEditQuestion = findViewById(R.id.btnEditQuestion);
        Button btnEditCategory = findViewById(R.id.btnEditCategory);

        // Initialize layouts
        layoutAddQuestion = findViewById(R.id.layoutAddQuestion);
        layoutAddCategory = findViewById(R.id.layoutAddCategory);
        layoutEditQuestions = findViewById(R.id.layoutEditQuestions);
        layoutEditCategories = findViewById(R.id.layoutEditCategories);

        View[] allLayouts = {layoutAddQuestion, layoutAddCategory, layoutEditQuestions, layoutEditCategories};

        // Button click listeners
        btnAddQuestion.setOnClickListener(v -> setVisibleSection(layoutAddQuestion, allLayouts));
        btnAddCategory.setOnClickListener(v -> setVisibleSection(layoutAddCategory, allLayouts));
        btnEditQuestion.setOnClickListener(v -> setVisibleSection(layoutEditQuestions, allLayouts));
        btnEditCategory.setOnClickListener(v -> setVisibleSection(layoutEditCategories, allLayouts));
    }

    private void setVisibleSection(View visibleLayout, View[] allLayouts) {
        for (View layout : allLayouts) {
            layout.setVisibility(View.GONE);
        }
        visibleLayout.setVisibility(View.VISIBLE);
    }

    private void showAddQuestionLayout() {
        // Make the Add Question layout visible and hide others
        // Implement the setVisibleSection() to display the Add Question form (if not done yet)
    }

    public void onSubmitAddQuestion(View view) {
        // Get references to the input fields
        EditText etQuestionText = findViewById(R.id.editTextQuestion);
        EditText etAnswer1 = findViewById(R.id.editTextAnswer1);
        EditText etAnswer2 = findViewById(R.id.editTextAnswer2);
        EditText etAnswer3 = findViewById(R.id.editTextAnswer3);
        EditText etAnswer4 = findViewById(R.id.editTextAnswer4);
        Spinner spinnerCorrectAnswer = findViewById(R.id.spinnerCorrectAnswer);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);

        // Retrieve the input text and selected category
        String questionText = etQuestionText.getText().toString().trim();
        String answer1 = etAnswer1.getText().toString().trim();
        String answer2 = etAnswer2.getText().toString().trim();
        String answer3 = etAnswer3.getText().toString().trim();
        String answer4 = etAnswer4.getText().toString().trim();
        int correctAnswer = spinnerCorrectAnswer.getSelectedItemPosition(); // Index of correct answer
        int categoryId = (int) spinnerCategory.getSelectedItemId();  // Assuming category spinner itemId is categoryId

        // Check if any required fields are empty
        if (questionText.isEmpty() || answer1.isEmpty() || answer2.isEmpty() ||
                answer3.isEmpty() || answer4.isEmpty() || correctAnswer == Spinner.INVALID_POSITION) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new TriviaQuestions object
        TriviaQuestions triviaQuestion = new TriviaQuestions(
                questionText, answer1, answer2, answer3, answer4, String.valueOf(correctAnswer), categoryId
        );

        // Insert the new question using the repository
        triviaQuestionsRepository.insertQuestion(triviaQuestion);

        // Notify the user
        Toast.makeText(this, "Question added successfully.", Toast.LENGTH_SHORT).show();

        // Optionally, clear the fields or navigate to another screen
    }

    // Function to handle submitting a category
    public void onSubmitAddCategory(View view) {
        // Get the category name from the EditText field
        EditText editTextCategoryName = findViewById(R.id.editTextCategoryName);
        String categoryName = editTextCategoryName.getText().toString().trim();

        // Validate the input
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Please enter a category name.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Category object
        Category newCategory = new Category(categoryName);

        // Insert the new category into the database using the repository
        TriviaQuestionsRepository repository = TriviaQuestionsRepository.getRepository(getApplication());
        repository.insertCategory(newCategory);

        // Provide feedback to the user
        Toast.makeText(this, "Category added successfully.", Toast.LENGTH_SHORT).show();

        // Optionally, clear the field or navigate away
        editTextCategoryName.setText("");
    }
}

