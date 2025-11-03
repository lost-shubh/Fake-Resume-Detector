package validators;

import models.Resume;
import models.ValidationResult;
import java.util.*;

/**
 * KeywordValidator - Detects suspicious keywords and fake universities/skills
 * Implements the Single Responsibility Principle
 */
public class KeywordValidator extends BaseValidator {
    
    // Known fake/suspicious keywords
    private static final Set<String> FAKE_KEYWORDS = new HashSet<>(Arrays.asList(
        "time travel", "telekinesis", "mind reading", "x-ray vision", 
        "unicorn startup", "expert in everything", "knows all programming languages",
        "guru", "ninja developer", "rockstar engineer", "10x developer",
        "lorem ipsum", "dummy text", "filler", "placeholder"
    ));
    
    // List of well-known universities (real)
    private static final Set<String> KNOWN_UNIVERSITIES = new HashSet<>(Arrays.asList(
        "harvard", "stanford", "mit", "berkeley", "stanford", "yale", "princeton",
        "oxford", "cambridge", "iit", "delhi university", "delhi technological university",
        "jiit", "iit delhi", "iit bombay", "iit madras", "iit kharagpur",
        "moscow state", "tsinghua", "peking", "tokyo", "singapore"
    ));
    
    // Suspicious skills
    private static final Set<String> IMPOSSIBLE_SKILLS = new HashSet<>(Arrays.asList(
        "time management 200%", "expert in quantum computing", "artificial general intelligence",
        "telepathy programming", "telekinetic debugging", "mind upload", "consciousness transfer"
    ));
    
    public KeywordValidator(Resume resume, ValidationResult result) {
        super(resume, result);
    }
    
    @Override
    public void validate() {
        System.out.println("Running Keyword Validation...");
        
        checkFakeKeywords();
        checkFakeUniversities();
        checkImpossibleSkills();
    }
    
    /**
     * Checks for suspicious/fake keywords
     */
    private void checkFakeKeywords() {
        for (String keyword : FAKE_KEYWORDS) {
            if (contentContains(keyword)) {
                flagAsKeyword(keyword);
                flagAsRedFlag("Suspicious keyword detected: '" + keyword + "'");
            }
        }
    }
    
    /**
     * Validates universities against known list
     */
    private void checkFakeUniversities() {
        for (String university : resume.getUniversities()) {
            boolean isKnown = KNOWN_UNIVERSITIES.stream()
                .anyMatch(known -> university.toLowerCase().contains(known));
            
            if (!isKnown && !university.trim().isEmpty()) {
                flagAsRedFlag("Unknown university: '" + university + "'");
                flagAsKeyword(university);
            }
        }
    }
    
    /**
     * Checks for impossible skills
     */
    private void checkImpossibleSkills() {
        for (String skill : resume.getSkills()) {
            for (String impossibleSkill : IMPOSSIBLE_SKILLS) {
                if (skill.toLowerCase().contains(impossibleSkill.toLowerCase())) {
                    flagAsRedFlag("Impossible skill claimed: '" + skill + "'");
                    flagAsKeyword(skill);
                }
            }
        }
    }
}
