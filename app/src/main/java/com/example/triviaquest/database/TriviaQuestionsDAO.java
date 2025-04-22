package com.example.triviaquest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.triviaquest.database.entities.TriviaQuestions;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TriviaQuestionsDAO {
    @Query("SELECT * FROM " + TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE +
            " WHERE userId = :loggedInUserId  ORDER BY questionId DESC")
    List<TriviaQuestions> getAllRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE +
            " WHERE userId = :loggedInUserId  ORDER BY questionId DESC")
    LiveData<List<TriviaQuestions>> getAllRecordsByUserIdLiveData(int loggedInUserId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TriviaQuestions triviaQuestion);
    @Query("SELECT * FROM " + TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE)
    List<TriviaQuestions> getAllQuestions();

    @Query("DELETE FROM " + TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE)
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(TriviaQuestions triviaQuestion);

}
