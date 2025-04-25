package com.example.triviaquest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.triviaquest.database.entities.Category;


import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert
    long insert(Category category);

    @Query("SELECT * FROM category ORDER BY name")
    List<Category> getAll();

    @Query("SELECT * FROM category ORDER BY name")
    LiveData<List<Category>> getAllCategoriesLiveData();


    @Delete
    void delete(Category category);

    @Query("DELETE FROM Category")
    void deleteAll();

    @Update
    void update(Category category);

}
