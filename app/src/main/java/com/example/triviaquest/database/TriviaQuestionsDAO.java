package com.example.triviaquest.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.triviaquest.database.entities.TriviaQuestions;

import java.util.ArrayList;
@Dao
public interface TriviaQuestionsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TriviaQuestions triviaQuestions);
    @Query("SELECT * FROM " + TriviaQuestionsDatabase.TRIVIA_QUESTIONS_TABLE)
    ArrayList<TriviaQuestions> getAllQuestions();

}
