package com.example.triviaquest.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.triviaquest.database.TriviaQuestionsDatabase;

import java.util.Objects;

@Entity(tableName = TriviaQuestionsDatabase.TRIVIA_QUESTIONS_TABLE)
public class TriviaQuestions {

    @PrimaryKey(autoGenerate = true)
    private String triviaQuestion;

    private String triviaQuestionOptionA;
    private String triviaQuestionOptionB;
    private String triviaQuestionOptionC;
    private String triviaQuestionOptionD;

    public TriviaQuestions(String triviaQuestion, String triviaQuestionOptionA, String triviaQuestionOptionB, String triviaQuestionOptionC, String triviaQuestionOptionD) {
        this.triviaQuestion = triviaQuestion;
        this.triviaQuestionOptionA = triviaQuestionOptionA;
        this.triviaQuestionOptionB = triviaQuestionOptionB;
        this.triviaQuestionOptionC = triviaQuestionOptionC;
        this.triviaQuestionOptionD = triviaQuestionOptionD;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TriviaQuestions that = (TriviaQuestions) o;
        return Objects.equals(triviaQuestion, that.triviaQuestion) && Objects.equals(triviaQuestionOptionA, that.triviaQuestionOptionA) && Objects.equals(triviaQuestionOptionB, that.triviaQuestionOptionB) && Objects.equals(triviaQuestionOptionC, that.triviaQuestionOptionC) && Objects.equals(triviaQuestionOptionD, that.triviaQuestionOptionD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(triviaQuestion, triviaQuestionOptionA, triviaQuestionOptionB, triviaQuestionOptionC, triviaQuestionOptionD);
    }

    public String getTriviaQuestion() {
        return triviaQuestion;
    }

    public void setTriviaQuestion(String triviaQuestion) {
        this.triviaQuestion = triviaQuestion;
    }

    public String getTriviaQuestionOptionA() {
        return triviaQuestionOptionA;
    }

    public void setTriviaQuestionOptionA(String triviaQuestionOptionA) {
        this.triviaQuestionOptionA = triviaQuestionOptionA;
    }

    public String getTriviaQuestionOptionB() {
        return triviaQuestionOptionB;
    }

    public void setTriviaQuestionOptionB(String triviaQuestionOptionB) {
        this.triviaQuestionOptionB = triviaQuestionOptionB;
    }

    public String getTriviaQuestionOptionC() {
        return triviaQuestionOptionC;
    }

    public void setTriviaQuestionOptionC(String triviaQuestionOptionC) {
        this.triviaQuestionOptionC = triviaQuestionOptionC;
    }

    public String getTriviaQuestionOptionD() {
        return triviaQuestionOptionD;
    }

    public void setTriviaQuestionOptionD(String triviaQuestionOptionD) {
        this.triviaQuestionOptionD = triviaQuestionOptionD;
    }
}
