package ml;

import java.util.regex.*;

/**
 * TextPreprocessor - Prepares resume text for ML model
 * Handles text cleaning and normalization
 */
public class TextPreprocessor {
    
    /**
     * Cleans and normalizes resume text
     */
    public static String preprocessText(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        // Convert to lowercase
        String cleaned = text.toLowerCase();
        
        // Remove special characters but keep spaces
        cleaned = cleaned.replaceAll("[^a-z0-9\\s]", " ");
        
        // Remove extra whitespace
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        return cleaned;
    }
    
    /**
     * Extracts resume sections
     */
    public static String extractRelevantContent(String text) {
        String processed = preprocessText(text);
        
        // Focus on key sections
        StringBuilder relevant = new StringBuilder();
        
        String[] keywords = {"education", "experience", "skills", "qualification",
                            "university", "company", "work", "degree", "certification"};
        
        String[] lines = processed.split("\n");
        for (String line : lines) {
            for (String keyword : keywords) {
                if (line.contains(keyword)) {
                    relevant.append(line).append(" ");
                }
            }
        }
        
        return relevant.toString().isEmpty() ? processed : relevant.toString();
    }
}
