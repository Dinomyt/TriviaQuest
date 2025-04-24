package com.example.triviaquest.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.triviaquest.MainActivity;
import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;
import com.example.triviaquest.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TriviaQuestions.class, User.class, Category.class}, version = 3, exportSchema = false)
public abstract class TriviaQuestDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "TriviaQuestion_database";

    public static final String TRIVIA_QUESTIONS_TABLE = "triviaQuestionsTable";

    public static final String USER_TABLE = "usertable";

    private static volatile TriviaQuestDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TriviaQuestDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TriviaQuestDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TriviaQuestDatabase.class,
                            DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);
                User testUser1 = new User("testUser1", "testUser1");
                dao.insert(testUser1);

                CategoryDAO cDao = INSTANCE.categoryDAO();
                cDao.deleteAll();
                Category geography = new Category("Geography");
                Category literature = new Category("Literature");
                long geographyId = cDao.insert(geography);
                long literatureId = cDao.insert(literature);

                TriviaQuestionsDAO qDao = INSTANCE.triviaQuestionsDAO();
                qDao.deleteAll();
                qDao.insertQuestion(new TriviaQuestions(
                        "What is the capital of France?", "Paris",
                        "London", "Berlin", "Madrid", (int) geographyId));
                qDao.insertQuestion(new TriviaQuestions(
                        "Who wrote 'Hamlet'?", "Shakespeare",
                        "Mozart", "Beethoven", "Chopin", (int) literatureId));
            });
        }
    };

    public abstract TriviaQuestionsDAO triviaQuestionsDAO();

    public abstract UserDAO userDAO();

    public abstract CategoryDAO categoryDAO();

}
