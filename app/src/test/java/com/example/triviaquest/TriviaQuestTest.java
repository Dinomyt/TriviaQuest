package com.example.triviaquest;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;

public class TriviaQuestTest {

    @Test
    public void questionConstructorTest() {
        // Now passing 6 strings + 1 int, matching your entity
        TriviaQuestions questions = new TriviaQuestions(
                "What is the smallest continent?",
                "Australia",   // optionA
                "Asia",        // optionB
                "Africa",      // optionC
                "Europe",      // optionD
                "Australia",   // correctAnswer
                0              // categoryId (dummy for the test)
        );

        // verify all getters
        assertEquals("What is the smallest continent?", questions.getQuestionText());
        assertEquals("Australia", questions.getOptionA());
        assertEquals("Asia", questions.getOptionB());
        assertEquals("Africa", questions.getOptionC());
        assertEquals("Europe", questions.getOptionD());
        assertEquals("Australia", questions.getCorrectAnswer());
        assertEquals(0, questions.getCategoryId());
    }

    @Test
    public void categoryConstructorTest() {
        Category category = new Category("Fast Food");
        assertEquals("Fast Food", category.getName());
    }
}
