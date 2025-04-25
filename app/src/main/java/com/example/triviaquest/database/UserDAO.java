package com.example.triviaquest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.triviaquest.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + TriviaQuestDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM " + TriviaQuestDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * from " + TriviaQuestDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * from " + TriviaQuestDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
    @Query("SELECT * FROM " + TriviaQuestDatabase.USER_TABLE +
            " WHERE username = :username LIMIT 1")
    User getUserByUsernameSync(String username);

    @Query("SELECT * FROM " + TriviaQuestDatabase.USER_TABLE + " ORDER BY score DESC")
    LiveData<List<User>> getUsersByScore();

    @Query("SELECT * FROM " + TriviaQuestDatabase.USER_TABLE + " WHERE id == :userId LIMIT 1")
    User getUserByUserIdSync(int userId);
}