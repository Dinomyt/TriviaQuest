package com.example.triviaquest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view first
        setContentView(R.layout.activity_admin_landing_page);

        // Check if the user is admin
        SharedPreferences prefs = getSharedPreferences("TriviaPrefs", MODE_PRIVATE);
        boolean isAdmin = prefs.getBoolean("isAdmin", false);

        // If not admin, show a toast and finish the activity
        if (!isAdmin) {
            Toast.makeText(this, "Access Denied. Admins Only.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize buttons
        Button btnAddQuestion = findViewById(R.id.btnAddQuestion);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        Button btnEditQuestion = findViewById(R.id.btnEditQuestion);
        Button btnEditCategory = findViewById(R.id.btnEditCategory);

        // The container for the dynamic sections
        LinearLayout dynamicSectionContainer = findViewById(R.id.dynamicSectionContainer);

        // Helper method to switch visibility of sections
        View[] allLayouts = {
                findViewById(R.id.layoutAddQuestion),
                findViewById(R.id.layoutAddCategory),
                findViewById(R.id.layoutEditQuestions),
                findViewById(R.id.layoutEditCategories)
        };

        // Set listeners for each button to make the corresponding layout visible
        btnAddQuestion.setOnClickListener(v -> setVisibleSection(R.layout.layout_add_question, dynamicSectionContainer, allLayouts));
        btnAddCategory.setOnClickListener(v -> setVisibleSection(R.layout.layout_add_category, dynamicSectionContainer, allLayouts));
        btnEditQuestion.setOnClickListener(v -> setVisibleSection(R.layout.layout_edit_questions, dynamicSectionContainer, allLayouts));
        btnEditCategory.setOnClickListener(v -> setVisibleSection(R.layout.layout_edit_categories, dynamicSectionContainer, allLayouts));
    }

    // Helper method to show the selected layout and hide others
    private void setVisibleSection(int layoutResId, LinearLayout container, View[] allLayouts) {
        // Remove all previous views
        container.removeAllViews();

        // Inflate the selected layout and add it to the container
        View layout = getLayoutInflater().inflate(layoutResId, container, false);
        container.addView(layout);

        // Hide all other layouts (if any)
        for (View v : allLayouts) {
            v.setVisibility(View.GONE);
        }

        // Make the selected layout visible
        layout.setVisibility(View.VISIBLE);
    }
}
