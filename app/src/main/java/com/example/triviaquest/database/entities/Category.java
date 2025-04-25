package com.example.triviaquest.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.triviaquest.database.TriviaQuestDatabase;

import java.util.Objects;

@Entity(tableName = "category")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;

    @NonNull
    public String name;

    public Category(@NonNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryId == category.categoryId && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, name);
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    @NonNull public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }

    @Override
    public String toString() {
        return name;
    }
}