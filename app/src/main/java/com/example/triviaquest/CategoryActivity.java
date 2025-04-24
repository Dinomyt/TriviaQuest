package com.example.triviaquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.triviaquest.database.TriviaQuestDatabase;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);

        Button goBackButton = findViewById(R.id.backButtonTextView);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        item.setVisible(true);
        item.setTitle("Logout");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                return false;
            }
        });

        MenuItem usernameItem = menu.findItem(R.id.usernameMenuItem);
        int userId = getSharedPreferences("TriviaPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (userId != -1) {
            TriviaQuestDatabase db = TriviaQuestDatabase.getDatabase(this);
            db.userDAO().getUserByUserId(userId).observe(this, user -> {
                if (user != null) {
                    usernameItem.setTitle(user.getUsername());
                }
            });
        }
        return true;
    }


}
