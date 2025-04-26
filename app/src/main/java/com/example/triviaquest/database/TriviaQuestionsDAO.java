package com.example.triviaquest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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


    @Query("SELECT * FROM " + TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE + " WHERE categoryId = :catId")
    List<TriviaQuestions> getByCategory(int catId);

    @Query("DELETE FROM " + TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE)
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(TriviaQuestions triviaQuestion);

    @androidx.room.Update
    void update(TriviaQuestions triviaQuestion);

    @Update
    void updateQuestion(TriviaQuestions question);

    @Query("SELECT * FROM triviaQuestionsTable WHERE questionId = :questionId LIMIT 1")
    TriviaQuestions getQuestionById(int questionId);

    @Query("DELETE FROM category WHERE categoryId = :categoryId")
    void deleteCategoryById(int categoryId);
}
