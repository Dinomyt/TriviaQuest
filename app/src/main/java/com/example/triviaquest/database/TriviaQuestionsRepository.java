package com.example.triviaquest.database;

import static com.example.triviaquest.database.TriviaQuestDatabase.databaseWriteExecutor;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.triviaquest.MainActivity;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;
import com.example.triviaquest.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TriviaQuestionsRepository {
    private final TriviaQuestionsDAO triviaQuestionsDAO;
    private final UserDAO userDAO;
    private final CategoryDAO categoryDAO;
    private final ArrayList<TriviaQuestions> allQuestions;
    private static TriviaQuestionsRepository repository;


    public TriviaQuestionsRepository(Application application) {
        TriviaQuestDatabase db = TriviaQuestDatabase.getDatabase(application);
        this.triviaQuestionsDAO = db.triviaQuestionsDAO();
        this.allQuestions = (ArrayList<TriviaQuestions>) this.triviaQuestionsDAO.getAllQuestions();
        this.userDAO = db.userDAO();
        this.categoryDAO = db.categoryDAO();
    }
    /**
     * TriviaQuestion methods
     */
    public static TriviaQuestionsRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<TriviaQuestionsRepository> future = databaseWriteExecutor.submit(
                new Callable<TriviaQuestionsRepository>() {
                    @Override
                    public TriviaQuestionsRepository call() throws Exception {
                        return new TriviaQuestionsRepository(application);
                    }
                }
        );
        try {
            repository = future.get();
            return repository;
        } catch (InterruptedException | ExecutionException e) {
            Log.e(MainActivity.TAG, "Problem getting GymLogRepository, thread error.", e);
        }
        return null;
    }

    public ArrayList<TriviaQuestions> getAllQuestions() {
        if (repository != null) {
            return null;
        }
        Future<ArrayList<TriviaQuestions>> future = databaseWriteExecutor.submit(
                new Callable<ArrayList<TriviaQuestions>>() {
                    @Override
                    public ArrayList<TriviaQuestions> call() throws Exception {
                        return (ArrayList<TriviaQuestions>) triviaQuestionsDAO.getAllQuestions();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i("SOMETHING REPOSITORY", "Problem when getting all GymLogs in the repository");
        }
        return null;
    }

    public void insertQuestion(TriviaQuestions triviaQuestion) {
        databaseWriteExecutor.execute(() ->
        {
            triviaQuestionsDAO.insert(triviaQuestion);
        });
    }


     /**
      * User methods
      */
     public User findUserByUsernameSync(String username) {
         Future<User> future = databaseWriteExecutor.submit(
                 new Callable<User>() {
                     @Override
                     public User call() throws Exception {
                         return userDAO.getUserByUsernameSync(username);
                     }
                 }
         );
         try {
             return future.get();
         } catch (InterruptedException | ExecutionException e) {
             Log.e(MainActivity.TAG, "Problem getting user by username", e);
         }
         return null;
     }
    public void insertUser(User user) {
        databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }
    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<User>> getLeaderboard() {
        return userDAO.getUsersByScore();
    }

    public LiveData<List<TriviaQuestions>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return triviaQuestionsDAO.getAllRecordsByUserIdLiveData(loggedInUserId);
    }

    @Deprecated
    public ArrayList<TriviaQuestions> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<TriviaQuestions>> future = databaseWriteExecutor.submit(
                new Callable<ArrayList<TriviaQuestions>>() {
                    @Override
                    public ArrayList<TriviaQuestions> call() throws Exception {
                        return (ArrayList<TriviaQuestions>) triviaQuestionsDAO.getAllRecordsByUserId(loggedInUserId);
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(MainActivity.TAG, "Problem when getting all GymLogs in the repository", e);
        }
        return null;
    }

     public List<Category> getAllCategories() {
         return categoryDAO.getAll();
     }

    public LiveData<List<Category>> getTwoMostRecentCategories() {
         return categoryDAO.getTwoMostRecent();
    }

}
