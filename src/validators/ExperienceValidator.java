package validators;

import models.Resume;
import models.ValidationResult;
import java.util.Calendar;

/**
 * ExperienceValidator - Validates years of experience for inconsistencies
 * Detects impossible work history
 */
public class ExperienceValidator extends BaseValidator {
    
    private static final int MAX_REASONABLE_YEARS = 70;
    private static final int MIN_WORKING_AGE = 18;
    
    public ExperienceValidator(Resume resume, ValidationResult result) {
        super(resume, result);
    }
    
    @Override
    public void validate() {
        System.out.println("Running Experience Validation...");
        
        checkYearsOfExperience();
        checkUnrealisticClaims();
    }
    
    /**
     * Validates years of experience
     */
    private void checkYearsOfExperience() {
        int years = resume.getYearsOfExperience();
        
        if (years < 0) {
            flagAsRedFlag("Negative years of experience detected: " + years);
        } else if (years > MAX_REASONABLE_YEARS) {
            flagAsRedFlag("Unrealistic years of experience: " + years + 
                         " (more than " + MAX_REASONABLE_YEARS + " years)");
        } else if (years > 50) {
            flagAsRedFlag("Unusually high years of experience: " + years);
        }
    }
    
    /**
     * Checks for unrealistic claims in content
     */
    private void checkUnrealisticClaims() {
        String content = resume.getContent().toLowerCase();
        
        if (content.contains("50 years") || content.contains("60 years")) {
            flagAsRedFlag("Claiming unrealistic work duration");
        }
        
        if (content.contains("started at age 5") || content.contains("started at age 8")) {
            flagAsRedFlag("Unrealistic start age for career");
        }
    }
}
