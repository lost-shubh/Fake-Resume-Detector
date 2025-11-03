package ml;

import java.io.*;
import java.util.*;

/**
 * MLPredictor - Loads and uses trained ML models for fake resume detection
 * Calls Python subprocess to use scikit-learn models
 * Handles model predictions and scoring
 */
public class MLPredictor {
    
    private String pythonScriptPath;
    private boolean modelLoaded;
    
    public MLPredictor(String pythonScriptPath) {
        this.pythonScriptPath = pythonScriptPath;
        this.modelLoaded = false;
        initializeModels();
    }
    
    /**
     * Initializes and loads trained models
     */
    private void initializeModels() {
        try {
            // Check if models exist
            File modelsDir = new File("python/models");
            if (!modelsDir.exists()) {
                System.out.println("[WARNING] Models directory not found at: " + modelsDir.getAbsolutePath());
                System.out.println("[INFO] Please run: python python/train_model.py");
                return;
            }
            
            File vectorizer = new File("python/models/vectorizer.pkl");
            File classifier = new File("python/models/lr_classifier.pkl");
            
            if (!vectorizer.exists() || !classifier.exists()) {
                System.out.println("[WARNING] Trained models not found!");
                System.out.println("[INFO] Please run: python python/train_model.py");
                return;
            }
            
            modelLoaded = true;
            System.out.println("[ML] Models loaded successfully!");
            
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to initialize models: " + e.getMessage());
        }
    }
    
    /**
     * Predicts if a resume is fake using ML models
     * @param resumeText The resume text to analyze
     * @return ML prediction score (0.0 to 1.0)
     */
    public double predictFakeScore(String resumeText) {
        if (!modelLoaded) {
            System.out.println("[ML] Models not loaded. Returning default score.");
            return 0.5;
        }
        
        try {
            return callPythonPredictor(resumeText);
        } catch (Exception e) {
            System.out.println("[ERROR] ML prediction failed: " + e.getMessage());
            return 0.5;
        }
    }
    
    /**
     * Calls Python prediction service via subprocess
     */
    private double callPythonPredictor(String resumeText) throws Exception {
        // Create Python command
        ProcessBuilder pb = new ProcessBuilder(
            "python",
            "python/predict.py",
            resumeText
        );
        
        pb.directory(new File(System.getProperty("user.dir")));
        pb.redirectErrorStream(true);
        
        // Execute process
        Process process = pb.start();
        
        // Read output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        double score = 0.5;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            try {
                if (line.matches("^[0-9]\\.[0-9]+$")) {
                    score = Double.parseDouble(line);
                    break;
                }
            } catch (NumberFormatException e) {
                // Continue reading
            }
        }
        
        reader.close();
        process.waitFor();
        
        return Math.max(0.0, Math.min(1.0, score)); // Clamp between 0.0 and 1.0
    }
    
    /**
     * Gets anomaly score using Isolation Forest
     */
    public double getAnomalyScore(String resumeText) {
        if (!modelLoaded) {
            return 0.5;
        }
        
        try {
            ProcessBuilder pb = new ProcessBuilder(
                "python",
                "python/anomaly_predict.py",
                resumeText
            );
            
            pb.directory(new File(System.getProperty("user.dir")));
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            double score = 0.5;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                try {
                    if (line.matches("^[0-9]\\.[0-9]+$")) {
                        score = Double.parseDouble(line);
                        break;
                    }
                } catch (NumberFormatException e) {
                    // Continue
                }
            }
            
            reader.close();
            process.waitFor();
            
            return Math.max(0.0, Math.min(1.0, score));
        } catch (Exception e) {
            return 0.5;
        }
    }
    
    /**
     * Checks if models are loaded
     */
    public boolean isModelLoaded() {
        return modelLoaded;
    }
}
