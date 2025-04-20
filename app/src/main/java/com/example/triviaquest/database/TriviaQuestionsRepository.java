package com.example.triviaquest.database;

import android.app.Application;
import android.util.Log;

import com.example.triviaquest.database.entities.TriviaQuestions;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TriviaQuestionsRepository {

    private TriviaQuestionsDAO triviaQuestionsDAO;

    private ArrayList<TriviaQuestions> allQuestions;

    public TriviaQuestionsRepository(Application application) {
        TriviaQuestDatabase db = TriviaQuestDatabase.getDatabase(application);
        this.triviaQuestionsDAO = db.TriviaQuestionsDAO();
        this.allQuestions = this.triviaQuestionsDAO.getAllQuestions();
    }

    public ArrayList<TriviaQuestions> getAllQuestions() {
        Future<ArrayList<TriviaQuestions>> future = TriviaQuestDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<TriviaQuestions>>() {
                    @Override
                    public ArrayList<TriviaQuestions> call() throws Exception {
                        return triviaQuestionsDAO.getAllQuestions();
                    }
                }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e) {
            Log.i("SOMETHING REPOSITORY", "Problem when getting all GymLogs in the repository");
        }
        return null;
    }

    public void insertTriviaQuestions(TriviaQuestions triviaQuestions) {
        TriviaQuestDatabase.databaseWriteExecutor.execute(() ->
        {
            triviaQuestionsDAO.insert(triviaQuestions);
        });
    }
}
