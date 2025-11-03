package validators;

import models.Resume;
import models.ValidationResult;

/**
 * Abstract base class for all validators
 * Demonstrates inheritance and abstract method pattern
 */
public abstract class BaseValidator {
    protected Resume resume;
    protected ValidationResult result;
    
    public BaseValidator(Resume resume, ValidationResult result) {
        this.resume = resume;
        this.result = result;
    }
    
    /**
     * Abstract method to be implemented by child classes
     */
    public abstract void validate();
    
    /**
     * Protected helper method for common validation tasks
     */
    protected void flagAsRedFlag(String message) {
        result.addRedFlag(message);
    }
    
    protected void flagAsKeyword(String keyword) {
        result.addSuspiciousKeyword(keyword);
    }
    
    /**
     * Converts string to lowercase for case-insensitive comparison
     */
    protected boolean contentContains(String text) {
        return resume.getContent().toLowerCase().contains(text.toLowerCase());
    }
}
