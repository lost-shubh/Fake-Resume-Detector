package ui;

import models.Resume;
import models.ValidationResult;
import validators.RuleBasedValidator;
import ml.MLPredictor;
import ml.TextPreprocessor;
import parser.ResumeParser;
import storage.ResultDatabase;

import java.util.*;
import java.io.*;

/**
 * CLIInterface - Terminal user interface for the fake resume detector
 * Provides menu-driven interaction with the system
 */
public class CLIInterface {
    
    private Scanner scanner;
    private ResultDatabase database;
    private MLPredictor mlPredictor;
    private boolean running;
    
    public CLIInterface() {
        this.scanner = new Scanner(System.in);
        this.database = new ResultDatabase("data/results");
        this.mlPredictor = new MLPredictor("python");
        this.running = true;
    }
    
    /**
     * Starts the CLI application
     */
    public void start() {
        displayWelcome();
        mainMenu();
    }
    
    /**
     * Displays welcome message
     */
    private void displayWelcome() {
        System.out.println("\n");
        System.out.println("╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(58) + "║");
        System.out.println("║" + centerText("FAKE RESUME DETECTOR", 58) + "║");
        System.out.println("║" + centerText("Using Java OOPs & Machine Learning", 58) + "║");
        System.out.println("║" + " ".repeat(58) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
        System.out.println();
    }
    
    /**
     * Main menu loop
     */
    private void mainMenu() {
        while (running) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("MAIN MENU");
            System.out.println("=".repeat(60));
            System.out.println("1. Analyze Resume (File)");
            System.out.println("2. Analyze Resume (Text Input)");
            System.out.println("3. View Detection History");
            System.out.println("4. View Statistics");
            System.out.println("5. Clear Database");
            System.out.println("6. Test with Sample Data");
            System.out.println("7. Exit");
            System.out.println("=".repeat(60));
            System.out.print("Enter your choice (1-7): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    analyzeResumeFromFile();
                    break;
                case "2":
                    analyzeResumeFromInput();
                    break;
                case "3":
                    viewDetectionHistory();
                    break;
                case "4":
                    viewStatistics();
                    break;
                case "5":
                    clearDatabase();
                    break;
                case "6":
                    testWithSampleData();
                    break;
                case "7":
                    exit();
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Analyzes resume from file
     */
    private void analyzeResumeFromFile() {
        System.out.print("\nEnter resume file path: ");
        String filePath = scanner.nextLine().trim();
        
        try {
            String content = ResumeParser.loadResumeFromFile(filePath);
            String fileName = new File(filePath).getName();
            analyzeResume(fileName, content);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to load file: " + e.getMessage());
        }
    }
    
    /**
     * Analyzes resume from user input
     */
    private void analyzeResumeFromInput() {
        System.out.println("\nEnter resume name (or press Enter for default):");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) {
            fileName = "resume_" + System.currentTimeMillis() + ".txt";
        }
        
        System.out.println("Enter resume text (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;
        
        while (true) {
            line = scanner.nextLine();
            if (line.equals("END")) {
                break;
            }
            content.append(line).append("\n");
        }
        
        if (content.length() > 0) {
            analyzeResume(fileName, content.toString());
        } else {
            System.out.println("[ERROR] No content provided.");
        }
    }
    
    /**
     * Core analysis method - combines rule-based and ML detection
     */
    private void analyzeResume(String fileName, String content) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ANALYZING RESUME: " + fileName);
        System.out.println("=".repeat(60));
        
        // Parse resume
        Resume resume = ResumeParser.parseResume(fileName, content);
        
        // Create validation result
        ValidationResult result = new ValidationResult(fileName);
        
        // Rule-based validation
        System.out.println("\n[VALIDATION] Starting Rule-Based Detection...");
        RuleBasedValidator ruleValidator = new RuleBasedValidator(resume, result);
        ruleValidator.validate();
        
        // ML-based validation
        System.out.println("\n[VALIDATION] Starting ML-Based Detection...");
        if (mlPredictor.isModelLoaded()) {
            String preprocessedText = TextPreprocessor.preprocessText(content);
            double mlScore = mlPredictor.predictFakeScore(preprocessedText);
            double anomalyScore = mlPredictor.getAnomalyScore(preprocessedText);
            
            result.setMlScore((mlScore + anomalyScore) / 2.0);
            System.out.println("  ML Score: " + String.format("%.4f", mlScore));
            System.out.println("  Anomaly Score: " + String.format("%.4f", anomalyScore));
        } else {
            System.out.println("  [WARNING] ML models not loaded. Using rule-based only.");
            result.setMlScore(0.0);
        }
        
        // Combine scores
        calculateFinalScore(result);
        
        // Save result
        database.saveResult(result);
        
        // Display result
        System.out.println(result.generateReport());
    }
    
    /**
     * Calculates final detection score combining both methods
     */
    private void calculateFinalScore(ValidationResult result) {
        double ruleScore = result.getRuleScore();
        double mlScore = result.getMlScore();
        
        // Weighted average: 60% ML + 40% Rule-based
        double finalScore = (mlScore * 0.6) + (ruleScore * 0.4);
        
        result.setConfidenceScore(finalScore);
        result.setValidationType("HYBRID");
        
        // Determine if fake
        if (finalScore > 0.5) {
            result.setFake(true);
        }
    }
    
    /**
     * Views detection history
     */
    private void viewDetectionHistory() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DETECTION HISTORY");
        System.out.println("=".repeat(60));
        
        List<ValidationResult> results = database.getAllResults();
        
        if (results.isEmpty()) {
            System.out.println("No results in database yet.");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            ValidationResult result = results.get(i);
            System.out.printf("%d. %s - %s (Score: %.2f%%)%n",
                i + 1,
                result.getResumeFileName(),
                result.isFake() ? "FAKE" : "GENUINE",
                result.getConfidenceScore() * 100
            );
        }
    }
    
    /**
     * Views statistics
     */
    private void viewStatistics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DETECTION STATISTICS");
        System.out.println("=".repeat(60));
        
        Map<String, Object> stats = database.getStatistics();
        
        System.out.println("Total Resumes Processed: " + stats.get("totalProcessed"));
        System.out.println("Fake Resumes Detected: " + stats.get("fakeCount"));
        System.out.println("Genuine Resumes: " + stats.get("genuineCount"));
        System.out.printf("Fake Percentage: %.2f%%%n", (double) stats.get("fakePercentage"));
        
        System.out.println();
    }
    
    /**
     * Clears database
     */
    private void clearDatabase() {
        System.out.print("\nAre you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("yes")) {
            database.clearResults();
            System.out.println("Database cleared.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }
    
    /**
     * Tests with sample data
     */
    private void testWithSampleData() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("RUNNING TEST WITH SAMPLE DATA");
        System.out.println("=".repeat(60));
        
        // Real resume sample
        String realResume = """
            John Smith
            Email: john.smith@email.com | Phone: +1-555-0101
            
            EDUCATION:
            B.Tech in Computer Science, IIT Delhi (2018)
            M.Tech in Artificial Intelligence, IIT Bombay (2020)
            
            EXPERIENCE:
            Senior Software Engineer, Google (2022 - Present, 2 years)
            Software Engineer, Microsoft (2020 - 2022, 2 years)
            Junior Developer, TCS (2018 - 2020, 2 years)
            
            SKILLS: Python, Java, Machine Learning, TensorFlow, Kubernetes
            """;
        
        // Fake resume sample
        String fakeResume = """
            Super Developer X
            Email: superhacker@fake.com | Phone: 999-999-9999
            
            EDUCATION:
            PhD in Everything from Fake University (2015)
            Master's in Unicorn Startup Management (2013)
            
            EXPERIENCE:
            Expert in Telekinesis Programming, Time Travel Corp (50 years)
            Guru Ninja 10x Developer, Imaginary Company (100 years)
            
            SKILLS: Time Travel, Telepathy Programming, Mind Reading, Expert in Everything
            """;
        
        System.out.println("\n[TEST 1] Analyzing REAL resume...");
        analyzeResume("test_real_resume.txt", realResume);
        
        System.out.println("\n[TEST 2] Analyzing FAKE resume...");
        analyzeResume("test_fake_resume.txt", fakeResume);
        
        System.out.println("\nTest completed!");
    }
    
    /**
     * Exits the application
     */
    private void exit() {
        System.out.println("\nThank you for using Fake Resume Detector!");
        System.out.println("Goodbye!");
        running = false;
        scanner.close();
    }
    
    /**
     * Helper method to center text
     */
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }
}
