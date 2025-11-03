package validators;

import models.Resume;
import models.ValidationResult;
import java.util.*;

/**
 * RuleBasedValidator - Orchestrates all rule-based validators
 * Uses composition to combine multiple validators
 * Demonstrates Strategy Pattern
 */
public class RuleBasedValidator {
    
    private Resume resume;
    private ValidationResult result;
    private List<BaseValidator> validators;
    
    public RuleBasedValidator(Resume resume, ValidationResult result) {
        this.resume = resume;
        this.result = result;
        this.validators = new ArrayList<>();
        initializeValidators();
    }
    
    /**
     * Initializes all validators
     */
    private void initializeValidators() {
        validators.add(new KeywordValidator(resume, result));
        validators.add(new ExperienceValidator(resume, result));
        validators.add(new DegreeValidator(resume, result));
    }
    
    /**
     * Executes all validators and computes final score
     */
    public void validate() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("STARTING RULE-BASED VALIDATION");
        System.out.println("=".repeat(60));
        
        // Execute all validators
        for (BaseValidator validator : validators) {
            validator.validate();
        }
        
        // Calculate rule-based score
        calculateRuleScore();
        
        System.out.println("Rule-Based Validation Complete");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Calculates the rule-based detection score
     */
    private void calculateRuleScore() {
        int redFlagCount = result.getRedFlags().size();
        int keywordCount = result.getSuspiciousKeywords().size();
        
        // Score calculation: more red flags = higher fake probability
        double score = Math.min((redFlagCount * 0.1 + keywordCount * 0.05), 1.0);
        
        result.setRuleScore(score);
        
        // Determine if likely fake based on rules
        if (score > 0.3) {
            result.setFake(true);
        }
    }
}
