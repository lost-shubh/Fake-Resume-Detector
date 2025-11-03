package models;

import java.io.Serializable;
import java.util.*;

/**
 * ValidationResult Model - Stores the results of resume validation
 * Encapsulates rule-based and ML-based detection results
 */
public class ValidationResult implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String resumeFileName;
    private boolean isFake;
    private double confidenceScore; // 0.0 to 1.0
    private List<String> redFlags;
    private List<String> suspiciousKeywords;
    private double mlScore; // ML model prediction
    private double ruleScore; // Rule-based detection score
    private Date validationDate;
    private String validationType; // "RULE_BASED", "ML_BASED", "HYBRID"
    
    public ValidationResult(String resumeFileName) {
        this.resumeFileName = resumeFileName;
        this.redFlags = new ArrayList<>();
        this.suspiciousKeywords = new ArrayList<>();
        this.validationDate = new Date();
        this.isFake = false;
        this.confidenceScore = 0.0;
        this.mlScore = 0.0;
        this.ruleScore = 0.0;
    }
    
    // Getters and Setters
    public String getResumeFileName() { return resumeFileName; }
    
    public boolean isFake() { return isFake; }
    public void setFake(boolean fake) { isFake = fake; }
    
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double score) { this.confidenceScore = score; }
    
    public List<String> getRedFlags() { return new ArrayList<>(redFlags); }
    public void addRedFlag(String flag) { this.redFlags.add(flag); }
    
    public List<String> getSuspiciousKeywords() { return new ArrayList<>(suspiciousKeywords); }
    public void addSuspiciousKeyword(String keyword) { this.suspiciousKeywords.add(keyword); }
    
    public double getMlScore() { return mlScore; }
    public void setMlScore(double score) { this.mlScore = score; }
    
    public double getRuleScore() { return ruleScore; }
    public void setRuleScore(double score) { this.ruleScore = score; }
    
    public Date getValidationDate() { return validationDate; }
    
    public String getValidationType() { return validationType; }
    public void setValidationType(String type) { this.validationType = type; }
    
    /**
     * Generates a detailed report of the validation
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n" + "=".repeat(60) + "\n");
        report.append("RESUME VALIDATION REPORT\n");
        report.append("=".repeat(60) + "\n");
        report.append("File: ").append(resumeFileName).append("\n");
        report.append("Date: ").append(validationDate).append("\n");
        report.append("Validation Type: ").append(validationType).append("\n");
        report.append("-".repeat(60) + "\n");
        report.append("STATUS: ").append(isFake ? "FAKE" : "GENUINE").append("\n");
        report.append("Confidence Score: ").append(String.format("%.2f%%", confidenceScore * 100)).append("\n");
        report.append("Rule-Based Score: ").append(String.format("%.2f%%", ruleScore * 100)).append("\n");
        report.append("ML-Based Score: ").append(String.format("%.2f%%", mlScore * 100)).append("\n");
        report.append("-".repeat(60) + "\n");
        
        if (!redFlags.isEmpty()) {
            report.append("RED FLAGS DETECTED:\n");
            for (String flag : redFlags) {
                report.append("  • ").append(flag).append("\n");
            }
        }
        
        if (!suspiciousKeywords.isEmpty()) {
            report.append("SUSPICIOUS KEYWORDS:\n");
            for (String keyword : suspiciousKeywords) {
                report.append("  • ").append(keyword).append("\n");
            }
        }
        
        report.append("=".repeat(60) + "\n");
        return report.toString();
    }
    
    @Override
    public String toString() {
        return "ValidationResult{" +
                "resumeFileName='" + resumeFileName + '\'' +
                ", isFake=" + isFake +
                ", confidenceScore=" + confidenceScore +
                ", validationType='" + validationType + '\'' +
                '}';
    }
}
