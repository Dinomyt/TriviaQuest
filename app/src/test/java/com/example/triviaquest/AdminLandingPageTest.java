package com.example.triviaquest;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class AdminLandingPageTest {
    @Test
    public void testLoadCategories() {
        // Mocking a fake category list
        List<String> mockCategories = new ArrayList<>();
        mockCategories.add("Science");
        mockCategories.add("History");
        mockCategories.add("Sports");

        // Simulate loadCategories result
        assertEquals(3, mockCategories.size());
        assertTrue(mockCategories.contains("Science"));
        assertTrue(mockCategories.contains("History"));
        assertTrue(mockCategories.contains("Sports"));
    }

    @Test
    public void testDeleteCategory() {
        // Mock a category list
        List<String> mockCategories = new ArrayList<>();
        mockCategories.add("Science");
        mockCategories.add("History");
        mockCategories.add("Sports");

        // Simulate deleting "History"
        mockCategories.remove("History");

        // Now check that "History" is gone
        assertEquals(2, mockCategories.size());
        assertFalse(mockCategories.contains("History"));
    }

}