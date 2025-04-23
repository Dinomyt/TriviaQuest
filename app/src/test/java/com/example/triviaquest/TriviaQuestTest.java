package com.example.triviaquest;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;

public class TriviaQuestTest {

    @Test
    public void questionConstructorTest() {
        TriviaQuestions questions = new TriviaQuestions(
                "What is the smallest continent?",
                "Australia",
                "Asia",
                "Africa",
                "Europe",
                0
        );

        assertEquals("What is the smallest continent?", questions.getQuestionText());
        assertEquals("Australia", questions.getOptionA());
        assertEquals("Asia", questions.getOptionB());
        assertEquals("Africa", questions.getOptionC());
        assertEquals("Europe", questions.getOptionD());
    }

    @Test
    public void categoryConstructorTest() {
        Category category = new Category("Fast Food");
        assertEquals("Fast Food", category.getName());
    }
}