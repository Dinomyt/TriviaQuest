package com.example.triviaquest.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.triviaquest.database.TriviaQuestDatabase;

import java.util.Objects;

@Entity(
        tableName = TriviaQuestDatabase.TRIVIA_QUESTIONS_TABLE,
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE
        )
)
public class TriviaQuestions {

    @PrimaryKey(autoGenerate = true)
    private int questionId;
    private int userId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;

    private int categoryId;

    public TriviaQuestions(String questionText,
                           String optionA, String optionB,
                           String optionC, String optionD,
                           String correctAnswer,
                           int categoryId) {
        this.questionText   = questionText;
        this.optionA        = optionA;
        this.optionB        = optionB;
        this.optionC        = optionC;
        this.optionD        = optionD;
        this.correctAnswer  = correctAnswer;
        this.categoryId     = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TriviaQuestions that = (TriviaQuestions) o;
        return Objects.equals(questionText, that.questionText)
                && Objects.equals(optionA, that.optionA)
                && Objects.equals(optionB, that.optionB)
                && Objects.equals(optionC, that.optionC)
                && Objects.equals(optionD, that.optionD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionText, optionA, optionB, optionC, optionD);
    }

    // … existing getters/setters …
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    // ← new getter/setter for correctAnswer
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    @Override
    public String toString() {
        return this.questionText; // or any other field you'd like to display (e.g., the question itself)
    }
}
