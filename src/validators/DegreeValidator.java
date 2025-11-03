package validators;

import models.Resume;
import models.ValidationResult;
import java.util.*;

/**
 * DegreeValidator - Validates educational qualifications
 * Checks for missing/incomplete degree information
 */
public class DegreeValidator extends BaseValidator {
    
    private static final Set<String> VALID_DEGREE_TYPES = new HashSet<>(Arrays.asList(
        "bsc", "ba", "msc", "ma", "phd", "mtech", "btech", "bca", "mca",
        "be", "me", "b.tech", "m.tech", "diploma", "associate", "bachelor",
        "master", "doctorate", "b.s.", "m.s.", "b.a.", "m.a."
    ));
    
    public DegreeValidator(Resume resume, ValidationResult result) {
        super(resume, result);
    }
    
    @Override
    public void validate() {
        System.out.println("Running Degree Validation...");
        
        checkDegreeCount();
        checkValidDegreeTypes();
    }
    
    /**
     * Checks if degree count is reasonable
     */
    private void checkDegreeCount() {
        int degrees = resume.getDegreeCount();
        
        if (degrees == 0 && resume.getYearsOfExperience() < 5) {
            flagAsRedFlag("No degrees listed for early-career candidate");
        } else if (degrees > 5) {
            flagAsRedFlag("Unusually high number of degrees: " + degrees);
        }
    }
    
    /**
     * Validates degree types mentioned
     */
    private void checkValidDegreeTypes() {
        String content = resume.getContent().toLowerCase();
        boolean hasValidDegree = false;
        
        for (String degree : VALID_DEGREE_TYPES) {
            if (content.contains(degree)) {
                hasValidDegree = true;
                break;
            }
        }
        
        if (!hasValidDegree && resume.getYearsOfExperience() > 0) {
            flagAsRedFlag("No recognizable degree type mentioned");
        }
    }
}
