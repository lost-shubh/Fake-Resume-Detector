package storage;

import models.ValidationResult;
import java.io.*;
import java.util.*;

/**
 * ResultDatabase - Manages storage and retrieval of validation results
 * Uses file-based storage (JSON format)
 */
public class ResultDatabase {
    
    private String databaseDir;
    private List<ValidationResult> results;
    
    public ResultDatabase(String databaseDir) {
        this.databaseDir = databaseDir;
        this.results = new ArrayList<>();
        initializeDatabase();
    }
    
    /**
     * Initializes database directory
     */
    private void initializeDatabase() {
        File dir = new File(databaseDir);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("[DB] Database directory created: " + databaseDir);
            }
        }
        loadAllResults();
    }
    
    /**
     * Saves a validation result
     */
    public void saveResult(ValidationResult result) {
        results.add(result);
        
        String fileName = sanitizeFileName(result.getResumeFileName()) + ".txt";
        String filePath = databaseDir + File.separator + fileName;
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(result.generateReport());
            System.out.println("[DB] Result saved: " + filePath);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save result: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves all results
     */
    public List<ValidationResult> getAllResults() {
        return new ArrayList<>(results);
    }
    
    /**
     * Retrieves result by file name
     */
    public ValidationResult getResultByFileName(String fileName) {
        return results.stream()
            .filter(r -> r.getResumeFileName().equals(fileName))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets all fake resumes detected
     */
    public List<ValidationResult> getFakeResumes() {
        List<ValidationResult> fakeResumes = new ArrayList<>();
        for (ValidationResult result : results) {
            if (result.isFake()) {
                fakeResumes.add(result);
            }
        }
        return fakeResumes;
    }
    
    /**
     * Gets statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        long fakeCount = results.stream().filter(ValidationResult::isFake).count();
        long genuineCount = results.size() - fakeCount;
        
        stats.put("totalProcessed", results.size());
        stats.put("fakeCount", fakeCount);
        stats.put("genuineCount", genuineCount);
        stats.put("fakePercentage", results.size() > 0 ? 
                 (fakeCount * 100.0 / results.size()) : 0);
        
        return stats;
    }
    
    /**
     * Clears all results
     */
    public void clearResults() {
        // Delete all files in database directory
        File dir = new File(databaseDir);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
        results.clear();
        System.out.println("[DB] Database cleared");
    }
    
    /**
     * Loads all results from database
     */
    private void loadAllResults() {
        File dir = new File(databaseDir);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                System.out.println("[DB] Loaded " + files.length + " previous results");
            }
        }
    }
    
    /**
     * Sanitizes file name
     */
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
