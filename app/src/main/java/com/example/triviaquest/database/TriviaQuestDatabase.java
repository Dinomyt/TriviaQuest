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


@Database(
        entities = {
                TriviaQuestions.class,
                User.class,
                Category.class
        },
        version = 6,
        exportSchema = false
)
public abstract class TriviaQuestDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "TriviaQuestion_database";
    public static final String TRIVIA_QUESTIONS_TABLE = "triviaQuestionsTable";
    public static final String USER_TABLE             = "usertable";

    private static volatile TriviaQuestDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TriviaQuestDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TriviaQuestDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
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

    private static final RoomDatabase.Callback addDefaultValues =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Log.i(MainActivity.TAG, "DATABASE CREATED — seeding data");
                    databaseWriteExecutor.execute(() -> {
                        // —— USERS ——
                        UserDAO uDao = INSTANCE.userDAO();
                        uDao.deleteAll();
                        User admin = new User("admin", "admin1");
                        admin.setAdmin(true);
                        uDao.insert(admin);
                        uDao.insert(new User("testUser1", "testUser1"));

                        // —— CATEGORIES ——
                        CategoryDAO cDao = INSTANCE.categoryDAO();
                        cDao.deleteAll();
                        long geoId   = cDao.insert(new Category("Geography"));
                        long litId   = cDao.insert(new Category("Literature"));
                        long histId  = cDao.insert(new Category("History"));
                        long sportId = cDao.insert(new Category("Sports"));

                        // —— TRIVIA QUESTIONS ——
                        TriviaQuestionsDAO qDao = INSTANCE.triviaQuestionsDAO();
                        qDao.deleteAll();

                        // — Geography (10) —
                        qDao.insertQuestion(new TriviaQuestions(
                                "What is the largest continent by land area?",
                                "Asia", "Africa", "North America", "Antarctica",
                                "Asia", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which river is the longest in the world?",
                                "Nile", "Amazon", "Yangtze", "Mississippi",
                                "Amazon", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Mount Kilimanjaro is located in which country?",
                                "Kenya", "Tanzania", "Uganda", "Ethiopia",
                                "Tanzania", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which country has the highest population?",
                                "India", "China", "USA", "Indonesia",
                                "China", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "What is the smallest country in the world?",
                                "Monaco", "Nauru", "Vatican City", "San Marino",
                                "Vatican City", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "What is the capital of Canada?",
                                "Toronto", "Vancouver", "Ottawa", "Montreal",
                                "Ottawa", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which desert is the largest hot desert in the world?",
                                "Sahara", "Gobi", "Kalahari", "Arabian",
                                "Sahara", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "In which ocean is the Mariana Trench located?",
                                "Atlantic", "Pacific", "Indian", "Arctic",
                                "Pacific", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "The Ural Mountains divide which two continents?",
                                "Europe and Asia", "Europe and Africa", "North and South America", "Asia and Africa",
                                "Europe and Asia", (int)geoId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which country spans eleven time zones?",
                                "USA", "Canada", "China", "Russia",
                                "Russia", (int)geoId));


                        // — Literature (10) —
                        qDao.insertQuestion(new TriviaQuestions(
                                "Who wrote 'Pride and Prejudice'?",
                                "Jane Austen", "Charlotte Brontë", "Mary Shelley", "Emily Brontë",
                                "Jane Austen", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who is the author of 'To Kill a Mockingbird'?",
                                "Mark Twain", "Harper Lee", "F. Scott Fitzgerald", "Ernest Hemingway",
                                "Harper Lee", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which author wrote '1984'?",
                                "Ray Bradbury", "Kurt Vonnegut", "George Orwell", "Aldous Huxley",
                                "George Orwell", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "'The Odyssey' is attributed to which ancient poet?",
                                "Virgil", "Ovid", "Sappho", "Homer",
                                "Homer", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who penned the play 'Hamlet'?",
                                "Goethe", "Voltaire", "Dante", "William Shakespeare",
                                "William Shakespeare", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who is the author of 'The Great Gatsby'?",
                                "Ernest Hemingway", "John Steinbeck", "Mark Twain", "F. Scott Fitzgerald",
                                "F. Scott Fitzgerald", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which writer created 'Moby-Dick'?",
                                "Herman Melville", "William Faulkner", "Nathaniel Hawthorne", "Walt Whitman",
                                "Herman Melville", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who wrote 'Don Quixote'?",
                                "Miguel de Cervantes", "Gabriel García Márquez", "Julio Cortázar", "Jorge Luis Borges",
                                "Miguel de Cervantes", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who authored 'The Divine Comedy'?",
                                "Geoffrey Chaucer", "John Milton", "William Blake", "Dante Alighieri",
                                "Dante Alighieri", (int)litId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who wrote 'War and Peace'?",
                                "Fyodor Dostoevsky", "Leo Tolstoy", "Anton Chekhov", "Ivan Turgenev",
                                "Leo Tolstoy", (int)litId));


                        // — History (10) —
                        qDao.insertQuestion(new TriviaQuestions(
                                "Who was the first President of the United States?",
                                "George Washington", "John Adams", "Thomas Jefferson", "James Madison",
                                "George Washington", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "In which year did World War II end?",
                                "1944", "1945", "1946", "1939",
                                "1945", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "The Magna Carta was signed in which year?",
                                "1214", "1215", "1216", "1225",
                                "1215", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which civilization built Machu Picchu?",
                                "Aztec", "Olmec", "Inca", "Maya",
                                "Inca", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Who was British PM for most of WWII?",
                                "Neville Chamberlain", "Winston Churchill", "Clement Attlee", "Stanley Baldwin",
                                "Winston Churchill", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "In what year did the Berlin Wall fall?",
                                "1987", "1988", "1989", "1990",
                                "1989", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which Tsar ruled Russia during the Napoleonic Wars?",
                                "Alexander I", "Peter I", "Nicholas I", "Catherine II",
                                "Alexander I", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "The Battle of Hastings took place in which year?",
                                "1065", "1066", "1067", "1070",
                                "1066", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which empire was founded by Genghis Khan?",
                                "Roman Empire", "Ottoman Empire", "Mongol Empire", "Persian Empire",
                                "Mongol Empire", (int)histId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "The Renaissance began in which country?",
                                "France", "England", "Italy", "Spain",
                                "Italy", (int)histId));


                        // — Sports (10) —
                        qDao.insertQuestion(new TriviaQuestions(
                                "How many players are on the field for one team in American football?",
                                "11", "10", "9", "8",
                                "11", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which country has won the most FIFA World Cups?",
                                "Germany", "Brazil", "Italy", "Argentina",
                                "Brazil", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "What fruit tops the Wimbledon men’s singles trophy?",
                                "Grapes", "Apple", "Pineapple", "Cherry",
                                "Pineapple", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "How many points is a free throw in basketball worth?",
                                "2", "3", "1", "0",
                                "1", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which sport is nicknamed the 'King of Sports'?",
                                "Basketball", "Cricket", "Soccer", "Baseball",
                                "Soccer", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "How many Grand Slam tournaments are there in tennis each year?",
                                "2", "3", "4", "1",
                                "4", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Olympic Games are held every how many years?",
                                "2", "3", "5", "4",
                                "4", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "A standard round of golf has how many holes?",
                                "18", "9", "12", "24",
                                "18", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "Which sport uses a puck?",
                                "Basketball", "Ice Hockey", "Soccer", "Baseball",
                                "Ice Hockey", (int)sportId));

                        qDao.insertQuestion(new TriviaQuestions(
                                "What is the official distance of a marathon?",
                                "25 miles", "26.2 miles", "24 miles", "26 miles",
                                "26.2 miles", (int)sportId));
                    });
                }
            };

    public abstract TriviaQuestionsDAO triviaQuestionsDAO();
    public abstract UserDAO        userDAO();
    public abstract CategoryDAO    categoryDAO();
}
