package com.example.triviaquest.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.triviaquest.MainActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {}, version = 1, exportSchema = false)
public abstract class TriviaQuestDatabase {
    //Database name
    private static final String DATABASE_NAME = "triviaQuestDatabase";
    //Table name for trivia questions
    public static final String TRIVIA_TABLE = "triviaTable";
    //User table name
    public static final String USER_TABLE = "userTable";
    //TriviaQuestDatabase instance stored in ram
    private static volatile TriviaQuestDatabase INSTANCE;
    // Number of threads to use for database operations
    private static final int NUMBER_OF_THREADS = 4;

    // ExecutorService with a fixed thread pool to handle database write operations
    // This allows database tasks to run asynchronously without blocking the main thread
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TriviaQuestDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TriviaQuestDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TriviaQuestDatabase.class, DATABASE_NAME)
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
            Log.i(MainActivity.TAG, "Database created");
            // Add default values to the database if needed
            // This is where you can insert initial data into the database
            //databaseWriteExecutor.execute(() -> {
            //                UserDAO dao = INSTANCE.userDAO();
            //                dao.deleteAll();
            //                User admin = new User("admin1", "admin1");
            //                admin.setAdmin(true);
            //                dao.insert(admin);
            //                User testUser1 = new User("testuser1", "testuser1");
            //                dao.insert(testUser1);
            // });
        }
    };

    public abstract TriviaDAO triviaDAO();
    public abstract UserDAO userDAO();

}
