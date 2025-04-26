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
    private EditText etQuestionText, etAnswer1, etAnswer2, etAnswer3, etAnswer4;
    private Spinner spinnerCorrectAnswer, spinnerCategory;
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
        Button btnEditQuestion = findViewById(R.id.btnEditQuestion);
        Button btnEditCategory = findViewById(R.id.btnEditCategory);

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

        // Button click listeners
        btnAddQuestion.setOnClickListener(v -> setVisibleSection(layoutAddQuestion));
        btnAddCategory.setOnClickListener(v -> setVisibleSection(layoutAddCategory));
        btnEditQuestion.setOnClickListener(v -> setVisibleSection(layoutEditQuestions));
        btnEditCategory.setOnClickListener(v -> setVisibleSection(layoutEditCategories));
    }

    private void setVisibleSection(View visibleLayout) {
        layoutAddQuestion.setVisibility(View.GONE);
        layoutAddCategory.setVisibility(View.GONE);
        layoutEditQuestions.setVisibility(View.GONE);
        layoutEditCategories.setVisibility(View.GONE);
        visibleLayout.setVisibility(View.VISIBLE);
    }

    public void onSubmitAddQuestion(View view) {
        // Get references to the input fields
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
    }

    public void onSubmitEditQuestion(View view) {
        if (currentQuestionId == -1) {
            Toast.makeText(this, "No question selected for editing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get references to the input fields
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
        TriviaQuestions updatedQuestion = new TriviaQuestions(
                questionText, answer1, answer2, answer3, answer4, String.valueOf(correctAnswer), categoryId
        );
        updatedQuestion.setQuestionId(currentQuestionId); // Set the ID for the question we are editing

        // Update the existing question using the repository
        triviaQuestionsRepository.updateQuestion(updatedQuestion);

        // Notify the user
        Toast.makeText(this, "Question updated successfully.", Toast.LENGTH_SHORT).show();

        // Optionally, clear the fields or navigate to another screen
        clearFields();
    }

    private void clearFields() {
        etQuestionText.setText("");
        etAnswer1.setText("");
        etAnswer2.setText("");
        etAnswer3.setText("");
        etAnswer4.setText("");
        spinnerCorrectAnswer.setSelection(0);
        spinnerCategory.setSelection(0);
        currentQuestionId = -1;  // Reset the current question ID
    }

    public void onSelectQuestionForEditing(int questionId) {
        // Load the question data based on the selected questionId
        TriviaQuestions question = triviaQuestionsRepository.getQuestionById(questionId);

        if (question != null) {
            // Set the UI fields with the existing question data
            etQuestionText.setText(question.getQuestionText());
            etAnswer1.setText(question.getOptionA());
            etAnswer2.setText(question.getOptionB());
            etAnswer3.setText(question.getOptionC());
            etAnswer4.setText(question.getOptionD());
            // Set correct answer and category spinner accordingly
            spinnerCorrectAnswer.setSelection(Integer.parseInt(question.getCorrectAnswer()));
            spinnerCategory.setSelection(question.getCategoryId());
            currentQuestionId = question.getQuestionId();
        } else {
            Toast.makeText(this, "Question not found.", Toast.LENGTH_SHORT).show();
        }
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
        triviaQuestionsRepository.insertCategory(newCategory);

        // Provide feedback to the user
        Toast.makeText(this, "Category added successfully.", Toast.LENGTH_SHORT).show();

        // Optionally, clear the field or navigate away
        editTextCategoryName.setText("");
    }
}


