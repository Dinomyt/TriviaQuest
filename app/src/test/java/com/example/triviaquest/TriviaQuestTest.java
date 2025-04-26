package com.example.triviaquest;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.triviaquest.database.entities.Category;
import com.example.triviaquest.database.entities.TriviaQuestions;
import com.example.triviaquest.database.entities.User;

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

    @Test
    public void categoryToStringTest() {
        Category category = new Category("Fast Food");
        // toString() should return the name (so Spinners show it properly)
        assertEquals("Fast Food", category.toString());
        // changing name should be reflected too
        category.setName("Cuisine");
        assertEquals("Cuisine", category.toString());
    }

    @Test
    public void triviaQuestionsEqualsHashCodeTest() {
        TriviaQuestions q1 = new TriviaQuestions(
                "Q", "A1", "B1", "C1", "D1", "A1", 42
        );
        TriviaQuestions q2 = new TriviaQuestions(
                "Q", "A1", "B1", "C1", "D1", "A1", 42
        );
        TriviaQuestions q3 = new TriviaQuestions(
                "Q2", "A2", "B2", "C2", "D2", "C2", 42
        );

        // reflexive, symmetric, and same hashCode
        assertTrue(q1.equals(q1));
        assertTrue(q1.equals(q2));
        assertTrue(q2.equals(q1));
        assertEquals(q1.hashCode(), q2.hashCode());

        // not equal to a different question
        assertFalse(q1.equals(q3));
        // also not equal to null or other class
        assertFalse(q1.equals(null));
        assertFalse(q1.equals("foo"));
    }

    @Test
    public void testRegisterUser() {
        User testUser = new User("testUsername", "testPassword");
        assertEquals("testUsername", testUser.getUsername());
        assertEquals("testPassword", testUser.getPassword());
        assertFalse(testUser.isAdmin());
    }

    @Test
    public void testLeaderboardEntry() {
        TriviaQuestions question = new TriviaQuestions(
                "What is the capital of Spain?", "Madrid", "Barcelona", "Valencia", "Seville", "Madrid", 1
        );
        assertEquals("Madrid", question.getOptionA());
        assertEquals("What is the capital of Spain?", question.getQuestionText());
    }

}
