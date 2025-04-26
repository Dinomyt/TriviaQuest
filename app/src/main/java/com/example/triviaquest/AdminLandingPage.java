package com.example.triviaquest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLandingPage extends AppCompatActivity {

    private View layoutAddQuestion, layoutAddCategory, layoutEditQuestions, layoutEditCategories;

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
}

